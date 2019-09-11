package com.eningqu.pay.wxpay;
/**
 *
 * @desc TODO  微信APi接口
 * @author     Yanghuangping
 * @since      2018/7/14 10:08
 * @version    1.0
 *
 **/

public class WXApi {

    /** 微信统一下单URL */
    public static final String PAY_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /** 申请退款 注意：申请退款就是发起退款请求，并不没有真正的退款 */
    public static final String REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";


    /** 通过code获取access_token的接口 */
    public static final String  OAUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /** 获取用户个人信息（UnionID机制）的接口 */
    public static final String  OAUTH_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";



}
