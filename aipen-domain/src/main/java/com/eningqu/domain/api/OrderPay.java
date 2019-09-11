package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO  订单支付实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("dy_orders_pay")
public class OrderPay extends BaseEntity<OrderPay> {

    @TableField("order_number")
    private String orderNumber;

    @TableField("appid")
    private String appid;

    @TableField("partnerid")
    private String partnerid;

    @TableField("package_pay")
    private String packagePay;

    @TableField("noncestr")
    private String noncestr;

    @TableField("timestamp")
    private String timestamp;

    @TableField("prepayid")
    private String prepayid;

    @TableField("sign")
    private String sign;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackagePay() {
        return packagePay;
    }

    public void setPackagePay(String packagePay) {
        this.packagePay = packagePay;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
