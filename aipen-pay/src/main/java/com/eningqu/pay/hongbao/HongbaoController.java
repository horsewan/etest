package com.eningqu.pay.hongbao;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alipay.api.domain.OrderDetail;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.common.kit.IdWorkerKit;
import com.eningqu.domain.api.*;
import com.eningqu.pay.common.union.config.SwiftpassConfig;
import com.eningqu.pay.common.union.uitl.MD5;
import com.eningqu.pay.common.union.uitl.SignUtils;
import com.eningqu.pay.common.union.uitl.XmlUtils;
import com.eningqu.service.*;
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
 * TODO 红包兑换
 */
@RestController
@RequestMapping("/api/hongbao")
public class HongbaoController {

    private  volatile int soldQuantity = 0;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderPayService orderPayService;

    @Autowired
    private IUserCreditService userCreditService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserCreditRecordService userCreditRecordService;

    /**
     * TODO 红包兑换生成订单
     * @return
     */
    @PostMapping("/outhCredit")
    @ResponseBody
    @Log("红包兑换积分验证")
    public RJson outhCredit(){
        Long uId = LoginInfoHelper.getUserID();


        return RJson.ok();
    }

    /**
     * TODO 红包兑换生成订单
     * @return
     */
    @PostMapping("/createByUserCredit")
    @ResponseBody
    @Log("红包兑换生成订单")
    public RJson createByUserCredit(@RequestParam Long proId){
        Long uId = LoginInfoHelper.getUserID();
        if(proId<=0)
            return RJson.error("产品不存在");
        Product product = productService.selectById(proId);
        if(product==null)
            return RJson.error("产品不存在");
        int stockQuantity = product.getStockQuantity();
        soldQuantity = product.getSoldQuantity();
        System.out.println("++++++++++++++++++++++soldQuantity:"+soldQuantity);
        if(stockQuantity==0)
            return RJson.error("该产品已兑换完");
        if(stockQuantity==soldQuantity)
            return RJson.error("该产品已兑换完");
        //库存扣减
        soldQuantity++;
        if(soldQuantity>stockQuantity)
            return RJson.error("该产品已兑换完");

        product.setSoldQuantity(soldQuantity);
        productService.updateById(product);

        HongBaoOrderResult hongBaoOrderResult = new HongBaoOrderResult();
        //创建订单（基础信息）
        Orders orders = new Orders();
        orders.setUid(uId);
        //自动生成订单号，流水号，
        String uuid = System.currentTimeMillis()+"";
        orders.setOrderNumber(IdWorkerKit.uniqueId());
        orders.setSerialNumber(new StringBuilder(uuid).reverse().toString());
        orders.setOrderStatus(OrderStatusEnum.UNPAID.getValue());//待支付
        orders.setOrderTime(new Date(System.currentTimeMillis()));
        orders.setBusinessId(1L);//商户id
        DateTime dateTime = DateUtil.date();
        orders.setAmountRealpay(product.getOriginalPrice());//实收金额
        orders.setPayTime(DateUtil.parse(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN), DatePattern.PURE_DATETIME_PATTERN));
        orders.setPayWay(PayWayStatusEnum.HONGBAO_PAY.getValue());//红包兑换
        orders.setAmountPayable(product.getOriginalPrice());//应付金额
        orderService.insert(orders);

        //添加订单详情
        OrdersDetail ordersDetail = new OrdersDetail();
        ordersDetail.setUid(uId);
        ordersDetail.setProductId(proId);
        ordersDetail.setOrderNumber(orders.getOrderNumber());
        ordersDetail.setOrderPrice(product.getOriginalPrice());
        ordersDetail.setOrderQuantity(product.getStockQuantity());
        orderDetailService.insert(ordersDetail);

        //返回红包订单基本信息和订单信息
        hongBaoOrderResult.setbId(1L);
        hongBaoOrderResult.setoId(orders.getId());
        hongBaoOrderResult.setbOrderNumber(orders.getOrderNumber());
        hongBaoOrderResult.setbOrderStatus(orders.getOrderStatus());
        hongBaoOrderResult.setbOrderTime(orders.getOrderTime());
        hongBaoOrderResult.setbOriginalPrice(product.getOriginalPrice());

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(uId);
        if(mUserCredit!=null){
            hongBaoOrderResult.setbCreditTotal(mUserCredit.getVipCredit()==null?new BigDecimal(0) :mUserCredit.getVipCredit().multiply(new BigDecimal(100).setScale(2)));
        }else{
            hongBaoOrderResult.setbCreditTotal(new BigDecimal(0));
        }
        return RJson.ok("红包兑换订单创建成功").setData(hongBaoOrderResult);
    }

    /**
     * TODO 用户红包兑换
     * @return
     */
    @PostMapping("/payByUserCredit")
    @ResponseBody
    @Log("用户红包兑换")
    public RJson payByUserCredit(@RequestParam String orderNumber,@RequestParam String orderCredit){
        Long uId = LoginInfoHelper.getUserID();
        if(orderNumber==null)
            return RJson.error("未查询到该订单号");

        //库存验证
        OrdersDetail ordersDetail = orderDetailService.selectOrderDetailByNumber(orderNumber);
        if(ordersDetail==null)
            return RJson.error("该订单异常，请重新下单");

        Product product = productService.selectById(ordersDetail.getProductId());
        if(product==null)
            return RJson.error("产品不存在");
        int stockQuantity = product.getStockQuantity();
        soldQuantity = product.getSoldQuantity();
        System.out.println("++++++++++++++++++++++soldQuantity:"+soldQuantity);
        if(stockQuantity==0)
            return RJson.error("该产品已兑换完");

        Orders orders = orderService.queryOrderByUid(orderNumber, uId);
        if (orders == null) {
            return RJson.error("未查询到该订单号，支付异常");
        }

        //积分扣除
        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(uId);
        if(mUserCredit!=null){
            BigDecimal creditValReq = new BigDecimal(orderCredit);
            if(creditValReq!=null){
                creditValReq = creditValReq.setScale(2);
            }
            BigDecimal tempUserCreditVal = new BigDecimal(0);
            if(mUserCredit.getVipCredit()!=null){
                tempUserCreditVal = mUserCredit.getVipCredit().multiply(new BigDecimal(100)).setScale(2);
            }
            BigDecimal vipCredit = null;
            if(tempUserCreditVal.doubleValue()>=creditValReq.doubleValue()){
                vipCredit = new BigDecimal(tempUserCreditVal.doubleValue()-creditValReq.doubleValue());
                BigDecimal tempVipCredit = vipCredit.divide(new BigDecimal(100));
                mUserCredit.setVipCredit(tempVipCredit);

//                OrdersDetail ordersDetail = orderDetailService.selectOrderDetailByNumber(orderNumber);
                if(ordersDetail!=null){
                    UserCreditRecord userCreditRecord = new UserCreditRecord();
                    userCreditRecord.setUid(uId);
                    userCreditRecord.setVipCredit(creditValReq.divide(new BigDecimal(100)));
                    userCreditRecord.setVipHongbao(ordersDetail.getId());
                    userCreditRecord.setVipStatus(2);
                    userCreditRecordService.insert(userCreditRecord);
                }
                userCreditService.updateById(mUserCredit);
            }else{
                return RJson.error("该用户红包不够兑换");
            }
        }else{
            return RJson.error("该用户红包不够兑换");
        }

        DateTime dateTime = DateUtil.date();

        //TODO 金额、时间
        orders.setAmountRealpay(new BigDecimal(orderCredit).divide(new BigDecimal(100)));//实收金额
        orders.setPayTime(DateUtil.parse(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN), DatePattern.PURE_DATETIME_PATTERN));
        orders.setPayWay(PayWayStatusEnum.HONGBAO_PAY.getValue());//红包兑换
        orders.setOrderStatus(OrderStatusEnum.PAID.getValue());
        orderService.updateById(orders);

       /* OrderPay tempOrderPay = orderPayService.selectByWhere(orderNumber);
        if(tempOrderPay!=null){
            tempOrderPay.setAppid("mch_id");
            tempOrderPay.setNoncestr("nonce_str");
            tempOrderPay.setOrderNumber(orderNumber);
            tempOrderPay.setPackagePay("HONGBAO_PAY");
            tempOrderPay.setPartnerid("mch_id");
            tempOrderPay.setSign("sign");
            tempOrderPay.setTimestamp(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
            tempOrderPay.setPrepayid("code_url");
            orderPayService.updateById(tempOrderPay);
        }else{
            OrderPay orderPay = new OrderPay();
            orderPay.setAppid("mch_id");
            orderPay.setNoncestr("nonce_str");
            orderPay.setOrderNumber(orderNumber);
            orderPay.setPackagePay("HONGBAO_PAY");
            orderPay.setPartnerid("mch_id");
            orderPay.setSign("sign");
            orderPay.setTimestamp(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
            orderPay.setPrepayid("code_url");
            orderPayService.insert(orderPay);
        }*/

        //兑换成功


        return RJson.ok("OK");
    }


}
