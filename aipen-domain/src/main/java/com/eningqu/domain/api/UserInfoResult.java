package com.eningqu.domain.api;

import java.math.BigDecimal;

/**
 *
 * @desc TODO  用户信息返回实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
public class UserInfoResult {
    private Long uid;
    private String token;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String headImg;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 个性签名
     */
    private String remarks;

    /**
     * 个代编号
     */
    private String agentNo;

    /**
     * 地址
     */
    private String addressD;

    /**
     * 用户类型
     */
    private Long type;

    /**
     * 二维码密文
     */
    private String QRVal;//个人二维码
    private String payQRVal;//个人收付款二维码

    private BigDecimal creditTotal;

    private String paySign;

    /**
     * 邀请码
     */
    private String invateCode;

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAddressD() {
        return addressD;
    }

    public void setAddressD(String addressD) {
        this.addressD = addressD;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getQRVal() {
        return QRVal;
    }

    public void setQRVal(String QRVal) {
        this.QRVal = QRVal;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(BigDecimal creditTotal) {
        this.creditTotal = creditTotal;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPayQRVal() {
        return payQRVal;
    }

    public void setPayQRVal(String payQRVal) {
        this.payQRVal = payQRVal;
    }

    public String getInvateCode() {
        return invateCode;
    }

    public void setInvateCode(String invateCode) {
        this.invateCode = invateCode;
    }
}
