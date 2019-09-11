package com.eningqu.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/9 15:21
 * @version    1.0
 *
 **/

public class OrdersListVO {

    private Long id;

    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 流水号
     */
    private String serialNumber;
    /**
     * 配送方式
     */
//    private String expWay;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

//    public String getExpWay() {
//        return expWay;
//    }
//
//    public void setExpWay(String expWay) {
//        this.expWay = expWay;
//    }

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


}
