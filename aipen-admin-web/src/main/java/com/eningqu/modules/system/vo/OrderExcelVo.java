package com.eningqu.modules.system.vo;

import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.PayWayStatusEnum;

import java.io.Serializable;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO  导出订单信息
 * @date 2019/9/7 15:49
 **/
public class OrderExcelVo implements Serializable {
    /*
    订单号
     */
    private String orderNumber;
    private String orderStatus;
    private String payMoney;
    private String money;
    private String payWay;
    private String orderTime;
    private String payTime;
    private String name;
    private String phone;
    private String address;
    private String expNo;

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
        this.orderStatus = OrderStatusEnum.getKey(orderStatus);;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = PayWayStatusEnum.getKey(payWay);;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }
}
