package com.eningqu.pay.alipay.api;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.eningqu.pay.common.tools.AliTools;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.pay.alipay.AliApi;
import com.eningqu.pay.alipay.AliConfig;
import com.eningqu.service.IOrderService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.SortedMap;

/**
 *
 * @desc TODO  支付宝支付
 * @author     Yanghuangping
 * @since      2018/7/28 18:04
 * @version    1.0
 *
 **/
@RestController
@RequestMapping("/api/ali")
public class AliPayController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AliConfig aliConfig;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private IOrderService orderService;

    @PostMapping("/app/pay")
    public RJson appay(){

        // TODO 公共请求参数
        SortedMap<String, String> keyVal = Maps.newTreeMap();
        keyVal.put("app_id", aliConfig.getAppId());
        keyVal.put("charset", AliApi.CHARSET);
        keyVal.put("method", AliApi.TRADE_APP_PAY);
        keyVal.put("sign_type", aliConfig.getSignType());
        keyVal.put("timestamp", DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_PATTERN));
        keyVal.put("version", "1.0");
        // 回调通知地址
        keyVal.put("notify_url", aliConfig.getNotifyUrl());

        // TODO 请求参数
        SortedMap<String, Object> contentKeyVal = Maps.newTreeMap();
        // 交易过期时间
        contentKeyVal.put("timeout_express", "30m");
        // 交易金额
        contentKeyVal.put("total_amount", "100");
        // 商户订单号
        contentKeyVal.put("out_trade_no", RandomUtil.randomNumbers(10));
        // 具体描述信息
        contentKeyVal.put("subject", "道域科技-智能笔");
        // 商品的标题/交易标题/订单标题/订单关键字等
        contentKeyVal.put("body", "购买道域科技智能笔产品，用于智能书写，方便于用户");
        // 商品主类型 :0-虚拟类商品,1-实物类商品
        contentKeyVal.put("goods_type", "1");
        // 可用支付渠道 余额，余额宝，花呗，信用卡，信用卡快捷，借记卡快捷
        contentKeyVal.put("enable_pay_channels", "balance,moneyFund,pcredit,creditCard,creditCardExpress,debitCardExpress");
        // 请求参数的集合
        keyVal.put("biz_content", JSONUtil.toJsonStr(contentKeyVal));

        StringBuilder payInfo = new StringBuilder();
        payInfo.append(AliTools.mapToStr(keyVal, true))
                .append("&sign=")
                .append(URLUtil.encode(AliTools.SHA256WithRSA(AliTools.mapToStr(keyVal, false), aliConfig.getAppPrivateKey(), aliConfig.getSignType()), AliTools.DEFAULT_CHARSET));

        return RJson.ok().setData(payInfo.toString());
    }

    /**
     * TODO 支付宝支付 异步通知
     */
    @PostMapping("/pay/notify")
    public String asyncNotify(HttpServletRequest request) {

        boolean result = false;

        // 获取异步通知数据
        SortedMap<String, String> params = getParams(request);
        logger.info("支付宝支付 异步通知结果：{}", JSONUtil.toJsonStr(params));

        // TODO 交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，才会认定为付款成功
        String tradeStatus = params.get("trade_status");
        if(!StrUtil.isBlank(tradeStatus) &&
                (StrUtil.equalsIgnoreCase(AliApi.TRADE_SUCCESS, tradeStatus)
                        || StrUtil.equalsIgnoreCase(AliApi.TRADE_FINISHED, tradeStatus))){

            // TODO 验签  用支付宝公钥 验证返回数据是否签名正确
            boolean signVerified = false;
            try {
                signVerified  = AlipaySignature.rsaCheckV1(params, aliConfig.getAlipayPublicKey(), AliTools.DEFAULT_CHARSET, params.get("sign_type"));
            } catch (AlipayApiException e) {
                logger.error("支付宝支付 异步通知 验签异常，{}", e);
                return "error";
            }
            if(!signVerified ){
                logger.error("支付宝支付 异步通知结果验签失败，签名不正确");
                return "error";
            }

            // TODO 校验  通知数据的正确性 是否为本商户号的数据

            String appId = params.get("app_id");
            String sellerId = params.get("seller_id");
            String sellerEmail = params.get("seller_email");
            // 商户订单号
            String outRradeNo = params.get("out_trade_no");
            // 支付金额
            String buyerPayAmount = params.get("buyer_pay_amount");
            // 支付时间
            String payTime = params.get("gmt_payment");
            // 支付宝支付订单号
            String serialNumber = params.get("trade_no");

            if(StrUtil.equalsIgnoreCase(aliConfig.getAppId(), appId)
                    && StrUtil.equalsIgnoreCase(aliConfig.getSellerId(), sellerId)
                    && StrUtil.equalsIgnoreCase(aliConfig.getSellerEmail(), sellerEmail)
                    && !StrUtil.isBlank(outRradeNo) && !StrUtil.isBlank(buyerPayAmount)){

                // TODO 更新订单状态
                boolean flag = orderService.updatePayNotifyOrder(outRradeNo, serialNumber, buyerPayAmount, payTime, PayWayStatusEnum.ALI_PAY.getValue());
                if(flag){
                    result = true;
                }
            }
        }

        if(result){
            return "success";
        }else{
            return "failure";
        }
    }

    @PostMapping("/refund")
    public void refund(String orderNumber){

        /*Orders orders = orderService.queryOrderByOrderNumber(orderNumber);
        if (orders == null) {
            logger.error("微信app支付，未查询到该订单号:{}的订单记录", orderNumber);
            throw new AipenException("支付异常");
        }

        // 若订单状态不是【待支付】状态，不能支付
        if (!StrUtil.equalsIgnoreCase(OrderStatusEnum.UNPAID.getValue(), orders.getOrderStatus())) {
            logger.error("微信app支付，该订单号：{} 记录不是[待支付]的状态，不能支付", orderNumber);
            throw new AipenException("支付异常");
        }*/

        // TODO 公共请求参数
        /*SortedMap<String, String> keyVal = Maps.newTreeMap();
        keyVal.put("app_id", aliConfig.getAppId());
        keyVal.put("charset", AliApi.CHARSET);
        keyVal.put("method", AliApi.TRADE_REFUND);
        keyVal.put("sign_type", aliConfig.getSignType());
        keyVal.put("timestamp", DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_PATTERN));
        keyVal.put("version", "1.0");*/

        // TODO 请求参数
        SortedMap<String, Object> contentKeyVal = Maps.newTreeMap();
        // 商户订单号
        contentKeyVal.put("out_trade_no", RandomUtil.randomNumbers(10));
        // 支付宝交易号
        contentKeyVal.put("trade_no", RandomUtil.randomNumbers(10));
        // 退款金额
        contentKeyVal.put("refund_amount", "30.11");
        // 退款原因
        contentKeyVal.put("refund_reason", "不想要了");
        // 退款订单号
        contentKeyVal.put("out_request_no", RandomUtil.randomNumbers(10));

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                AliApi.GATE_WAY,
                aliConfig.getAppId(),
                aliConfig.getAppPrivateKey(),
                AliApi.FORMAT,
                AliApi.CHARSET,
                aliConfig.getAlipayPublicKey(),
                aliConfig.getSignType());

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(JSONUtil.toJsonStr(contentKeyVal));

        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            logger.error("支付宝，退款异常：{}", e);
        }

        // 执行成功
        if(response != null && response.isSuccess()){
            // 退款是否发生了资金变化
            if(StrUtil.equalsIgnoreCase("Y", response.getFundChange())){
                // orderService.updateRefundNotify();
            }
        }
    }

    /**
     * TODO 授权回调地址
     */
    @RequestMapping("/authRedirect")
    public void authRedirect(HttpServletRequest request){

    }

    /**
     * TODO 支付宝应用网关
     */
    @RequestMapping("/gateway")
    public void gateway(HttpServletResponse response){

    }


    /**
     * TODO 从request获取所有参数
     * @param request
     * @return
     */
    private SortedMap<String, String> getParams(HttpServletRequest request){
        SortedMap<String, String> params = Maps.newTreeMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = (String) paramNames.nextElement();
            params.put(key, request.getParameter(key));
            /*String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length >0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    params.put(paramName, paramValue);
                }
            }*/
        }
        return params;
    }
}
