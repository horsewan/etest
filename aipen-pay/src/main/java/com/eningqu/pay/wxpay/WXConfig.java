package com.eningqu.pay.wxpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @desc TODO  微信app应用配置
 * @author     Yanghuangping
 * @since      2018/7/14 15:44
 * @version    1.0
 *
 **/
@Configuration
public class WXConfig {

    /** 商户号ID */
    @Value("${wx.mch_id}")
    private String mchId;

    /** 商户号秘钥 */
    @Value("${wx.mch_key}")
    private String mchKey;

    /** 商户证书路径 */
    @Value("${wx.mch_cert_path}")
    private String mchCertPath;

    /** 支付回调通知URL */
    @Value("${wx.pay_notify_url}")
    private String payNotifyUrl;

    /** 退款回调通知URL */
    @Value("${wx.refund_notify_url}")
    private String refundNotifyUrl;

    /** 应用appId */
    @Value("${wx.app.app_id}")
    private String appId;


    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }

    public String getMchCertPath() {
        return mchCertPath;
    }

    public void setMchCertPath(String mchCertPath) {
        this.mchCertPath = mchCertPath;
    }
}
