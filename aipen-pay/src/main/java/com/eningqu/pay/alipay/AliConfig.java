package com.eningqu.pay.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @desc TODO  支付宝配置
 * @author     Yanghuangping
 * @since      2018/7/30 11:45
 * @version    1.0
 *
 **/
@Configuration
//@ConfigurationProperties(prefix = "config")
//@PropertySource("classpath:/config.properties")
public class AliConfig {

    /** 商家账号 */
    @Value("${ali.seller_email}")
    private String sellerEmail;
    /** 商户UID */
    @Value("${ali.seller_id}")
    private String sellerId;
    /** 应用appid */
    @Value("${ali.app_id}")
    private String appId;
    /** 应用私钥 */
    @Value("${ali.app_private_key}")
    private String appPrivateKey;
    /** 签名类型 */
    @Value("${ali.sign_type}")
    private String signType;
    /** 支付宝公钥 */
    @Value("${ali.alipay_public_key}")
    private String alipayPublicKey;
    /** 异步通知URL */
    @Value("${ali.notify_url}")
    private String notifyUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Bean
    @Primary
    public AlipayClient initAlipayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(
                AliApi.GATE_WAY,
                appId,
                appPrivateKey,
                AliApi.FORMAT,
                AliApi.CHARSET,
                alipayPublicKey,
                signType);
        return alipayClient;
    }
}
