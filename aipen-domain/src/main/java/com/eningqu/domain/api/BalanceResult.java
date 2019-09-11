package com.eningqu.domain.api;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO 余额明细
 * @date 2019/9/2 14:02
 **/
public class BalanceResult {

    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付金额
     */
    private BigDecimal money;

    /**
     * 支付方式
     */
    private Integer payWay;

    public Long getId() {
        return id;
    }

    public BalanceResult setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public BalanceResult setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public Date getPayTime() {
        return payTime;
    }

    public BalanceResult setPayTime(Date payTime) {
        this.payTime = payTime;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public BalanceResult setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public BalanceResult setPayWay(Integer payWay) {
        this.payWay = payWay;
        return this;
    }
}
