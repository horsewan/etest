package com.eningqu.domain.api;

import java.sql.Timestamp;

public class PayInfoResult {
    //商户号	partnerid
    //预支付交易会话ID	prepayid
    //扩展字段	package
    //随机字符串	noncestr
    //时间戳	timestamp	String(10)
    //签名	sign
    private String partNerid;

    private String prePayid;

    private String payPackage;

    private String nonceStr;

    private Timestamp payTime;

    private String paySign;

    public String getPartNerid() {
        return partNerid;
    }

    public void setPartNerid(String partNerid) {
        this.partNerid = partNerid;
    }

    public String getPrePayid() {
        return prePayid;
    }

    public void setPrePayid(String prePayid) {
        this.prePayid = prePayid;
    }

    public String getPayPackage() {
        return payPackage;
    }

    public void setPayPackage(String payPackage) {
        this.payPackage = payPackage;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
