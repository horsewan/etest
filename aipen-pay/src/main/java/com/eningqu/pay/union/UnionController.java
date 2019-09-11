package com.eningqu.pay.union;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.common.kit.IdWorkerKit;
import com.eningqu.domain.api.*;
import com.eningqu.pay.common.union.config.SwiftpassConfig;
import com.eningqu.pay.common.union.uitl.MD5;
import com.eningqu.pay.common.union.uitl.SignUtils;
import com.eningqu.pay.common.union.uitl.XmlUtils;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.service.IBusinessService;
import com.eningqu.service.IOrderPayService;
import com.eningqu.service.IOrderService;
import com.eningqu.service.IUserCreditService;
import com.eningqu.vo.LoginInfoHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * TODO union聚合支付
 */
@RestController
@RequestMapping("/api/union")
public class UnionController{

    @Autowired
    private IBusinessService businessService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderPayService orderPayService;

    @Autowired
    private IUserCreditService userCreditService;

    /**
     * TODO 获取Union生成的聚合二维码支付数据
     * @return
     */
    @PostMapping("/getUnionBusinessQRVal")
    @ResponseBody
    @Log("识别商户支付二维码信息")
    public RJson getUnionBusinessQRVal(@RequestParam String bqrval){
        Long uId = LoginInfoHelper.getUserID();
        if(bqrval==null||"".equals(bqrval))
            return RJson.error("商家二维码数据错误");

        String tempQrval = bqrval.replace("& lt;","<").replace("& gt;",">");
        //解析密文 3>ticket>1>bId>2<<singleNo
        String[] oneArr = tempQrval.split("<<");
        if(oneArr==null||oneArr.length==0)
            return RJson.error("商家二维码数据错误");

        String[] twoArr = oneArr[0].split(">");
        if(twoArr==null||twoArr.length==0)
            return RJson.error("商家二维码数据错误");

        StringBuffer sbBusiness = new StringBuffer();
        sbBusiness.append(twoArr[2]);
        sbBusiness.append(twoArr[4]);
        sbBusiness.append(twoArr[0]);

        //检验解析得到商家数据是否合法
        BusinessInfo businessInfo = businessService.selectByTicketAndSingleNo(sbBusiness.toString(),oneArr[1]);
        if(businessInfo==null){
            return RJson.error("商家二维码不合法");
        }

        BusinessOrderResult businessOrderResult = new BusinessOrderResult();
        //创建订单（基础信息）
        Orders orders = new Orders();
        orders.setUid(uId);
        //自动生成订单号，流水号，
        String uuid = System.currentTimeMillis()+"";
        orders.setOrderNumber(IdWorkerKit.uniqueId());
        orders.setSerialNumber(new StringBuilder(uuid).reverse().toString());
        orders.setOrderStatus(OrderStatusEnum.UNPAID.getValue());//待支付
        orders.setOrderTime(new Date(System.currentTimeMillis()));
        orders.setBusinessId(businessInfo.getId());//商户id
        orderService.insert(orders);

        //返回商家基本信息和订单信息
        businessOrderResult.setbId(businessInfo.getId());
        businessOrderResult.setoId(orders.getId());
        businessOrderResult.setbName(businessInfo.getbName());
        businessOrderResult.setbOrderNumber(orders.getOrderNumber());
        businessOrderResult.setbOrderStatus(orders.getOrderStatus());
        businessOrderResult.setbOrderTime(orders.getOrderTime());
        businessOrderResult.setbSolePrice(businessInfo.getbSolePrice());

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(uId);
        if(mUserCredit!=null){
            businessOrderResult.setbCreditTotal(mUserCredit.getVipCredit()==null?new BigDecimal(0) :mUserCredit.getVipCredit());
        }else{
            businessOrderResult.setbCreditTotal(new BigDecimal(0));
        }
        return RJson.ok("商户二维码订单创建成功").setData(businessOrderResult);
    }

    /**
     * TODO 生成Union生成的聚合二维码支付数据
     * @return
     */
    @PostMapping("/payUnionBusinessQRVal")
    @ResponseBody
    @Log("生成商户支付二维码信息")
    public RJson payUnionBusinessQRVal(@RequestParam String orderNumber,@RequestParam String body,@RequestParam String orderMoney,@RequestParam String payWay){
        Long uId = LoginInfoHelper.getUserID();
        if(orderNumber==null)
            return RJson.error("未查询到该订单号");
        if(!payWay.equalsIgnoreCase(PayWayStatusEnum.ALI_PAY.getValue())&&!payWay.equalsIgnoreCase(PayWayStatusEnum.UNION_PAY.getValue())){
            return RJson.error("不支持该支付方式");
        }

        Orders orders = orderService.queryOrderByUid(orderNumber, uId);
        if (orders == null) {
            return RJson.error("未查询到该订单号，支付异常");
        }

        BusinessInfo businessInfo = businessService.selectById(orders.getBusinessId());
        if(businessInfo==null){
            return RJson.error("该订单商家已解绑，支付异常");
        }
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("service", SwiftpassConfig.unionService);
        paraMap.put("version", SwiftpassConfig.unionVersion);
        paraMap.put("charset", SwiftpassConfig.unionCharset);
        paraMap.put("sign_type", SwiftpassConfig.unionSignType);
        paraMap.put("out_trade_no", orderNumber);
        paraMap.put("body", body);
        paraMap.put("total_fee", orderMoney);
        paraMap.put("mch_create_ip", SwiftpassConfig.mchCreateIp);

        DateTime dateTime = DateUtil.date();

        SortedMap<String,String> map = XmlUtils.getParameterMap(paraMap);
        map.put("mch_id", businessInfo.getMchId());
        map.put("notify_url", SwiftpassConfig.notify_url);
        map.put("nonce_str", String.valueOf(new Date().getTime()));

        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        map.put("sign",  MD5.sign(buf.toString(), "&key=" + businessInfo.getMchKey(), "utf-8"));

        //TODO 金额、时间
        orders.setAmountRealpay(new BigDecimal(orderMoney).divide(new BigDecimal(100)));//实收金额
        orders.setPayTime(DateUtil.parse(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN), DatePattern.PURE_DATETIME_PATTERN));
        orders.setPayWay(payWay);//云闪付
        orderService.updateById(orders);

//        System.out.println("reqUrl：" + SwiftpassConfig.req_url);
//        System.out.println("reqParams:" + XmlUtils.parseXML(map));
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
//        Map<String,String> orderResult = new HashMap<>(); //用来存储订单的交易状态(key:订单号，value:状态(0:未支付，1：已支付))  ---- 这里可以根据需要存储在数据库中
//        String res = null;
        try {
            HttpPost httpPost = new HttpPost(SwiftpassConfig.req_url);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            //httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
//                res = XmlUtils.toXml(resultMap);
//                System.out.println("请求结果：" + res);
                if(resultMap.containsKey("sign")){
                    if(!SignUtils.checkParam(resultMap, businessInfo.getMchKey())){
                        return RJson.error().setData("验证签名不通过");
                    }else{
                        if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
//                            if(orderResult == null){
//                                orderResult = new HashMap<String,String>();
//                            }
//                            orderResult.put(map.get("out_trade_no"), "0");//初始状态

//                            String code_img_url = resultMap.get("code_img_url");
//                            String code_url = resultMap.get("code_url");
//                            System.out.println("code_img_url"+code_img_url+"   code_url "+code_url);
//                            req.setAttribute("code_img_url", code_img_url);
//                            req.setAttribute("out_trade_no", map.get("out_trade_no"));
//                            req.setAttribute("total_fee", map.get("total_fee"));
//                            req.setAttribute("body", map.get("body"));
                            OrderPay tempOrderPay = orderPayService.selectByWhere(orderNumber);
                            if(tempOrderPay!=null){
                                tempOrderPay.setAppid(map.get("mch_id"));//公众号id
                                tempOrderPay.setNoncestr(map.get("nonce_str"));//
                                tempOrderPay.setOrderNumber(orderNumber);
                                tempOrderPay.setPackagePay("UNION_PAY");
                                tempOrderPay.setPartnerid(map.get("mch_id"));//商户id
                                tempOrderPay.setSign(map.get("sign"));
                                tempOrderPay.setTimestamp(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
                                tempOrderPay.setPrepayid(resultMap.get("code_url"));//支付URL
                                orderPayService.updateById(tempOrderPay);
                            }else{
                                OrderPay orderPay = new OrderPay();
                                orderPay.setAppid(map.get("mch_id"));//公众号id
                                orderPay.setNoncestr(map.get("nonce_str"));//
                                orderPay.setOrderNumber(orderNumber);
                                orderPay.setPackagePay("UNION_PAY");
                                orderPay.setPartnerid(map.get("mch_id"));//商户id
                                orderPay.setSign(map.get("sign"));//
                                orderPay.setTimestamp(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
                                orderPay.setPrepayid(resultMap.get("code_url"));//支付URL
                                orderPayService.insert(orderPay);
                            }
                            return RJson.ok("支付成功").setData(resultMap.get("code_url"));
                        }else{
                            return RJson.error().setData("支付失败");
                        }
                    }
                }
            }else{
                return RJson.error().setData("操作失败，请重新支付");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RJson.error().setData("系统异常");
        } finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return RJson.ok("OK");
    }

    /**
     * TODO 支付通知回调 http://192.168.1.149:9191/api/union/notify
     * @return
     */
    @PostMapping("/notify")
    @ResponseBody
    @Log("生成商户支付二维码信息")
    public void initUnionBusinessQRVal(HttpServletRequest req, HttpServletResponse resp){
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "text/html;charset=UTF-8");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            String FEATURE = null;
            try {
                // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
                // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
                FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
                dbf.setFeature(FEATURE, true);

                // If you can't completely disable DTDs, then at least do the following:
                // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
                // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
                // JDK7+ - http://xml.org/sax/features/external-general-entities
                FEATURE = "http://xml.org/sax/features/external-general-entities";
                dbf.setFeature(FEATURE, false);

                // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
                // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
                // JDK7+ - http://xml.org/sax/features/external-parameter-entities
                FEATURE = "http://xml.org/sax/features/external-parameter-entities";
                dbf.setFeature(FEATURE, false);

                // Disable external DTDs as well
                FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
                dbf.setFeature(FEATURE, false);

                // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
                dbf.setXIncludeAware(false);
                dbf.setExpandEntityReferences(false);

                // And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
                // ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
                // (http://cwe.mitre.org/data/definitions/918.html) and denial
                // of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

                // remaining parser logic
            } catch (ParserConfigurationException e) {
                // This should catch a failed setFeature feature
                System.out.println("ParserConfigurationException was thrown. The feature '" +
                        FEATURE + "' is probably not supported by your XML processor.");
            }
            /*catch (SAXException e) {
            	// On Apache, this should be thrown when disallowing DOCTYPE
            	System.out.println("A DOCTYPE was passed into the XML document");
            }
            catch (IOException e) {
            	// XXE that points to a file that doesn't exist
            	System.out.println("IOException occurred, XXE may still possible: " + e.getMessage());
            }*/

            String resString = XmlUtils.parseRequst(req);
            //System.out.println("通知内容：" + resString);

            String respString = "fail";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
                if(map.containsKey("sign")){
//                    if(!SignUtils.checkParam(map, SwiftpassConfig.key)){
//                        respString = "fail";
//                        System.out.println("respString+++++"+respString);
//                    }else{
                        String status = map.get("status");
                        if(status != null && "0".equals(status)){
                            String result_code = map.get("result_code");
                            if(result_code != null && "0".equals(result_code)){
                                String out_trade_no = map.get("out_trade_no");
//                                orderResult.put(out_trade_no, "1");
                                //System.out.println(TestPayServlet.orderResult);
                                if(out_trade_no!=null&&!"".equals(out_trade_no)){
                                    orderService.updateUnionPayNotifyOrder(out_trade_no);
                                }
                            }
                        }
                        respString = "success";
//                    }
                }
            }
            resp.getWriter().write(respString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
