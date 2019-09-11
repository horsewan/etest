package com.eningqu.pay.wxpay.api;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.eningqu.common.kit.AmountKit;
import com.eningqu.common.kit.IPKit;
import com.eningqu.pay.common.tools.WXCertHttpTools;
import com.eningqu.pay.common.tools.WeiXinTools;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.kit.WebKit;
import com.eningqu.domain.api.OrderPay;
import com.eningqu.domain.api.Orders;
import com.eningqu.pay.wxpay.WXApi;
import com.eningqu.pay.wxpay.WXConfig;
import com.eningqu.service.IOrderPayService;
import com.eningqu.service.IOrderService;
import com.eningqu.service.IUserCreditRecordService;
import com.eningqu.service.IUserCreditService;
import com.eningqu.vo.LoginInfoHelper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  微信支付
 * @since 2018/7/14 9:44
 **/
@RestController
@RequestMapping("/api/wx")
public class WxPayController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;
    @Autowired
    private WXConfig wxConfig;
    @Autowired
    private IUserCreditService userCreditService;
    @Autowired
    private IUserCreditRecordService userCreditRecordService;
    @Autowired
    private IOrderPayService orderPayService;

    /**
     * TODO 微信app支付 预支付下单接口
     *
     * @param orderNumber
     * @return
     */
    @PostMapping("/app/pay")
    public RJson appay(@RequestParam String orderNumber,@RequestParam String orderMoney) throws Exception {
        Long uId = LoginInfoHelper.getUserID();
        Orders orders = orderService.queryOrderByUid(orderNumber, uId);
        if (orders == null) {
            logger.error("微信app支付，未查询到该订单号:{}的订单记录", orderNumber);
            return RJson.error("未查询到该订单号，支付异常");
        }

        // 若订单状态不是【待支付】状态，不能支付
        if (!StrUtil.equalsIgnoreCase(OrderStatusEnum.COMPLETED.getValue(), orders.getOrderStatus())) {
            if (!StrUtil.equalsIgnoreCase(OrderStatusEnum.UNPAID.getValue(), orders.getOrderStatus())) {
                logger.error("微信app支付，该订单号：{} 记录不是[待支付]的状态，不能支付", orderNumber);
                return RJson.error("该订单号不是[待支付]状态，不能支付");
            }
        }
        logger.info("orderNumber：{} orderMoney：{} uid：{}", orderNumber,orderMoney,uId);
        SortedMap<String, String> params = Maps.newTreeMap();
        // 通用参数
        WeiXinTools.commonParams(wxConfig, params);
        // 商品描述
        params.put("body", "道域科技-商品支付测试");
        // 商户订单号
        params.put("out_trade_no", orderNumber);
        // 总金额
        //params.put("total_fee", orders.getAmountPayable().multiply(new BigDecimal(100)).toString());
        params.put("total_fee", orderMoney);
        // 发起人IP地址
        params.put("spbill_create_ip", IPKit.getIpAddr(WebKit.getRequest()));
        // 回调地址
        params.put("notify_url", wxConfig.getPayNotifyUrl());
        // 交易类型
        params.put("trade_type", "APP");
        // 交易失效时间 此处设置15分钟过期
        DateTime dateTime = DateUtil.date();
        params.put("time_start", DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        params.put("time_expire", DateUtil.format(DateUtil.offsetMinute(dateTime, 15), DatePattern.PURE_DATETIME_PATTERN));
        // 签名
//        String sign = ;
//        String sign2 = WeiXinTools.generateSignature(params, wxConfig.getMchKey());
//        params.put("sign", WeiXinTools.createSign(params, wxConfig.getMchKey(), "UTF-8"));
        params.put("sign",  WeiXinTools.createSign(params, wxConfig.getMchKey(), "UTF-8"));


//        logger.info("微信app支付，生成预支付prepay_id，请求参数：{}", params);
        // 返回xml结果
//        String requestXML = ;
        logger.info("微信app支付 生成预支付信息：\n{{}", WeiXinTools.createXml(params));
//        String resultXml = HttpUtil.post(WXApi.PAY_UNIFIEDORDER,WeiXinTools.createXml(params));

//        logger.info("微信app支付，生成预支付prepay_id，返回参数：\n{}", HttpUtil.post(WXApi.PAY_UNIFIEDORDER,WeiXinTools.createXml(params)));

        Map<String, String> map = WeiXinTools.xmlToMap(HttpUtil.post(WXApi.PAY_UNIFIEDORDER,WeiXinTools.createXml(params)));
        if(map == null){
            logger.error("微信支付 统一下单接口生成预下单prepay_id失败");
            throw new AipenException("微信支付异常");
        }

        String returnCode = map.get("return_code");
        String returnMsg = map.get("return_msg");

        if (!StrUtil.isBlank(returnCode) && StrUtil.equalsIgnoreCase("SUCCESS", returnCode)) {

            String resultCode = map.get("result_code");
//            String errCodeDes = map.get("err_code_des");

            if (!StrUtil.isBlank(resultCode) && StrUtil.equalsIgnoreCase("SUCCESS", resultCode)) {
                SortedMap<String, String> payParams = Maps.newTreeMap();
                //{"appid":"wxb4ba3c02aa476ea1","partnerid":"1900006771","package":"Sign=WXPay","noncestr":"9ccfbe4158fddf76a57621c6297b89fc","timestamp":1564562391,
                // "prepayid":"wx31163951433714c1162dfbcd1248035932","sign":"90044F75CD962D5DE75644DDA3F7FD03"}
                payParams.put("appid", params.get("appid"));
                payParams.put("partnerid", map.get("mch_id"));
                payParams.put("package", "Sign=WXPay");
                payParams.put("noncestr",map.get("nonce_str"));
                payParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                payParams.put("prepayid", map.get("prepay_id"));
                // 签名
                String sign3 = WeiXinTools.createSign(payParams, wxConfig.getMchKey(), "UTF-8");
                payParams.put("sign", sign3);

                logger.info("微信app支付 生成预支付信息   返回后的值：\n{}", payParams);

                OrderPay tempOrderPay = orderPayService.selectByWhere(orderNumber);
                if(tempOrderPay!=null){
                    tempOrderPay.setAppid(payParams.get("appid"));
                    tempOrderPay.setNoncestr(payParams.get("noncestr"));
                    tempOrderPay.setOrderNumber(orderNumber);
                    tempOrderPay.setPackagePay(payParams.get("package"));
                    tempOrderPay.setPartnerid(payParams.get("partnerid"));
                    tempOrderPay.setSign(payParams.get("sign"));
                    tempOrderPay.setTimestamp(payParams.get("timestamp"));
                    tempOrderPay.setPrepayid(payParams.get("prepayid"));
                    orderPayService.updateById(tempOrderPay);
                }else{
                    OrderPay orderPay = new OrderPay();
                    orderPay.setAppid(payParams.get("appid"));
                    orderPay.setNoncestr(payParams.get("noncestr"));
                    orderPay.setOrderNumber(orderNumber);
                    orderPay.setPackagePay(payParams.get("package"));
                    orderPay.setPartnerid(payParams.get("partnerid"));
                    orderPay.setSign(payParams.get("sign"));
                    orderPay.setTimestamp(payParams.get("timestamp"));
                    orderPay.setPrepayid(payParams.get("prepayid"));
                    orderPayService.insert(orderPay);
                }

                //更新订单价格相关参数
                orders.setAmountRealpay(new BigDecimal(orderMoney).divide(new BigDecimal(100)));//实收金额
                orders.setPayWay(PayWayStatusEnum.WEI_XIN_PAY.getValue());
                orders.setPayTime(DateUtil.parse(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN), DatePattern.PURE_DATETIME_PATTERN));
                orderService.updateById(orders);
//                }

                return RJson.ok().setData(payParams);
            } else {
//                logger.error("微信支付，订单号:{}, 统一下单接口生成预下单prepay_id失败, 失败信息：{}", orderNumber, errCodeDes);
                return RJson.error("微信支付，订单号："+orderNumber+", 统一下单接口生成预下单prepay_id失败, 失败信息："+returnMsg );
            }

        } else {
//            logger.error("微信支付，订单号：{}, 统一下单接口生成预下单prepay_id失败, 失败信息：{}", orderNumber, returnMsg);
            return RJson.error("微信支付，订单号："+orderNumber+", 统一下单接口生成预下单prepay_id失败, 失败信息："+returnMsg );

        }

        //商户号	partnerid
        //预支付交易会话ID	prepayid
        //扩展字段	package
        //随机字符串	noncestr
        //时间戳	timestamp	String(10)
        //签名	sign

    }

    /**
     * TODO 微信支付 异步通知
     *
     * @param response
     */
    @PostMapping("/pay/notify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {

        boolean result = false;
        SortedMap<String, String> resultMap = WeiXinTools.parseXml(request);
        logger.info("微信支付 异步通知结果，{}", resultMap);

        if(resultMap != null){
            // 业务结果状态码
            String returnCode = resultMap.get("return_code");
            if (StrUtil.equalsIgnoreCase(returnCode, "SUCCESS")) {

                // TODO 验签
                String sign1 = resultMap.get("sign");
                String sign2 = WeiXinTools.createSign(resultMap, wxConfig.getMchKey(), "UTF-8");

                if (StrUtil.equalsIgnoreCase(sign1, sign2)) {
                    // 商户订单号
                    String outTradeNo = resultMap.get("out_trade_no");
                    // 微信支付订单号
                    String serialNumber = resultMap.get("transaction_id");
                    // 商户号
                    String mchId = resultMap.get("mch_id");
                    // 支付金额
                    String totalFee = resultMap.get("total_fee");
                    // 支付时间
                    String payTime = resultMap.get("time_end");

                    if (StrUtil.equalsIgnoreCase(wxConfig.getMchId(), mchId)
                            && !StrUtil.isBlank(outTradeNo)
                            && !StrUtil.isBlank(payTime)
                            && !StrUtil.isBlank(totalFee)) {
                        // 分转元
                        totalFee = AmountKit.changeF2Y(totalFee, 2, BigDecimal.ROUND_HALF_UP);
                        logger.debug("totalFee：{} outTradeNo：{}",totalFee,outTradeNo);
                        boolean flag = orderService.updatePayNotifyOrder(outTradeNo, serialNumber, totalFee, payTime, PayWayStatusEnum.WEI_XIN_PAY.getValue());
                        if (flag) {
                            logger.debug("微信支付 异步通知 处理成功");
                        }
                        result = true;
                    } else {
                        logger.error("微信支付 异步通知, 返回结果中，商户号：{}不正确", mchId);
                    }

                }else{
                    logger.error("微信支付 异步通知, 返回结果签名校验错误");
                }
            } else {
                logger.error("微信支付 异步通知, 返回结果状态不是支付成功的状态");
                //未支付成功

            }
        } else {
            logger.error("微信支付 异步通知, 获取到返回结果");
        }

        // 响应微信服务器结果
        String resXml;
        if (result) {
            logger.debug("微信支付 异步通知 处理成功");
            resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } else {
            logger.error("微信支付 异步通知 处理错误");
            resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
        }

        BufferedOutputStream out = null;
        try {
            // 处理业务完毕 返回给微信结果
            out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
        } catch (IOException e) {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e1) {
                    logger.error("微信app支付，通知返回异常，{}", e);
                }
            }
            logger.error("微信app支付，通知返回异常，{}", e);
        }
    }

    @PostMapping("/refund")
    public void refund(@RequestParam String orderNumber) {

        // 请求参数
        SortedMap<String, String> requestParams = Maps.newTreeMap();
        requestParams.put("appid", wxConfig.getAppId());
        requestParams.put("mch_id", wxConfig.getMchId());
        requestParams.put("nonce_str", RandomUtil.randomString(32));
        requestParams.put("out_trade_no", orderNumber);
        requestParams.put("out_refund_no", DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_PATTERN));
        requestParams.put("total_fee", "201");
        requestParams.put("refund_fee", "201");
        requestParams.put("refund_desc", "购物产品有问题，不想要了");
        requestParams.put("notify_url", wxConfig.getRefundNotifyUrl());

        // 签名
        requestParams.put("sign", WeiXinTools.createSign(requestParams, wxConfig.getMchKey(), "UTF-8"));
        String xmlObj = WeiXinTools.createXml(requestParams);

        logger.info("申请退款 请求参数：{}", requestParams);

        String resultXml = null;
        try {
            resultXml = WXCertHttpTools.post(WXApi.REFUND, xmlObj, wxConfig.getMchId(), wxConfig.getMchCertPath());
        } catch (Exception e) {
            logger.error("申请退款异常，{}", e);
            throw new AipenException("申请退款失败");
        }

        logger.info("申请退款 返回参数：\n{}", resultXml);
        SortedMap<String, String> resultMap = WeiXinTools.xmlToMap(resultXml);
        if(resultMap == null){
            logger.error("微信申请退款异常");
            throw new AipenException("申请退款失败");
        }

        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");

        if(StrUtil.equalsIgnoreCase("SUCCESS", returnCode)){
            // TODO 验签
            String sign1 = resultMap.get("sign");
            String sign2 = WeiXinTools.createSign(resultMap, wxConfig.getMchKey(), "UTF-8");
            if(StrUtil.equalsIgnoreCase(sign1, sign2)){

                String mchId = resultMap.get("mch_id");
                String outTradeNo = resultMap.get("out_trade_no");
                String refundNo = resultMap.get("out_refund_no");
                String refundFee = resultMap.get("refund_fee");

                if(StrUtil.equalsIgnoreCase(mchId, wxConfig.getMchId())
                        && StrUtil.equalsIgnoreCase(orderNumber, outTradeNo)){


                }
            }
        }
    }

    /**
     * TODO 微信退款 异步通知
     * @param request
     * @param response
     */
    @PostMapping("/refund/notify")
    public String refundNotify(HttpServletRequest request, HttpServletResponse response) {

        boolean result = false;

        SortedMap<String, String> resultMap = WeiXinTools.parseXml(request);
        logger.info("微信退款 异步通知结果，{}", JSONUtil.toJsonStr(resultMap));

        if(resultMap != null){
            String returnCode = resultMap.get("return_code");
            String returnMsg = resultMap.get("return_msg");

            if(StrUtil.equalsIgnoreCase("SUCCESS", returnCode)){

                // 校验商户号
                String mchId = resultMap.get("mch_id");
                String req_info = resultMap.get("req_info");
                if(!StrUtil.isBlank(returnCode) && StrUtil.equalsIgnoreCase(wxConfig.getMchId(), returnCode)){

                }

            }

        }

        // 响应微信服务器结果
        if (result) {
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } else {
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
        }
    }


    @PostMapping("/order/query")
    public void query(@RequestParam String orderNumber){
        // 请求参数
        SortedMap<String, String> requestParams = Maps.newTreeMap();
        requestParams.put("appid", wxConfig.getAppId());
        requestParams.put("mch_id", wxConfig.getMchId());
        requestParams.put("nonce_str", RandomUtil.randomString(16));
        requestParams.put("out_trade_no", orderNumber);
        requestParams.put("sign", WeiXinTools.createSign(requestParams, wxConfig.getMchKey(), "UTF-8"));

        String reqeustXml = WeiXinTools.createXml(requestParams);
        logger.info("微信支付 查询订单，请求参数：\n{}", reqeustXml);
        String responseXml = HttpUtil.post(WXApi.ORDER_QUERY, reqeustXml);
        logger.info("微信支付 查询订单，响应参数：\n{}", responseXml);

        SortedMap<String, String> resultMap = WeiXinTools.xmlToMap(responseXml);



    }


    @GetMapping("/sandbox")
    public void sandbox(){
        SortedMap<String, String> request = Maps.newTreeMap();
        request.put("mch_id", wxConfig.getMchId());
        request.put("nonce_str", RandomUtil.randomString(16));
        request.put("sign", WeiXinTools.createSign(request, wxConfig.getMchKey(), "UTF-8"));

        String result = HttpUtil.post("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", WeiXinTools.createXml(request));
        logger.info(result);
    }
}
