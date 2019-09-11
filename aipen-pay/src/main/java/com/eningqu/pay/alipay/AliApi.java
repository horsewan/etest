package com.eningqu.pay.alipay;
/**
 *
 * @desc TODO  阿里支付APi接口
 * @author     Yanghuangping
 * @since      2018/7/30 11:45
 * @version    1.0
 *
 **/

public class AliApi {

    /** 默认网关 */
    public static final String GATE_WAY = "https://openapi.alipay.com/gateway.do";
    /** 支持格式 */
    public static final String FORMAT = "JSON";
    /** 请求使用的编码格式，如utf-8,gbk,gb2312等 */
    public static final String CHARSET = "utf-8";

    /**-----TODO API 接口------*/

    /** app支付接口 */
    public static final String TRADE_APP_PAY = "alipay.trade.app.pay";
    /** 统一收单线下交易查询 */
    public static final String TRADE_QUERY = "alipay.trade.query";
    /** 支付宝订单信息同步接口 */
    public static final String TRADE_ORDERINFO_SYNC = "alipay.trade.orderinfo.sync";
    /** 统一收单交易退款接口 */
    public static final String TRADE_REFUND = "alipay.trade.refund";


    /**-----TODO 状态------*/

    /** 触发条件是商户签约的产品支持退款功能的前提下，买家付款成功 */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    /** 触发条件是商户签约的产品不支持退款功能的前提下，买家付款成功；或者，商户签约的产品支持退款功能的前提下，交易已经成功并且已经超过可退款期限 */
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
}
