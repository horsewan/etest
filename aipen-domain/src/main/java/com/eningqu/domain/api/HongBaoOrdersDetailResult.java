package com.eningqu.domain.api;

import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @desc TODO  红包订单详情实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
public class HongBaoOrdersDetailResult extends BaseEntity<HongBaoOrdersDetailResult> {

    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 快递公司
     */
    private String expCompany;
    /**
     * 快递公司编码
     */
    private String expCode;
    /**
     * 快递单号
     */
    private String expNo;
    /**
     * 收件人
     */
    private String signName;
    /**
     * 联系方式
     */
    private String signPhone;
    /**
     * 收件地址
     */
    private String signAddress;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 支付支付
     */
    private String payWay;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 应付金额
     */
    private BigDecimal amountPayable;
    /**
     * 实收金额
     */
    private BigDecimal amountRealpay;

    /**
     * 备注
     */
    private String remarks;


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getExpCompany() {
        return expCompany;
    }

    public void setExpCompany(String expCompany) {
        this.expCompany = expCompany;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSignPhone() {
        return signPhone;
    }

    public void setSignPhone(String signPhone) {
        this.signPhone = signPhone;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(BigDecimal amountPayable) {
        this.amountPayable = amountPayable;
    }

    public BigDecimal getAmountRealpay() {
        return amountRealpay;
    }

    public void setAmountRealpay(BigDecimal amountRealpay) {
        this.amountRealpay = amountRealpay;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
