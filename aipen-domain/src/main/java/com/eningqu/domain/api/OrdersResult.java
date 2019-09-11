package com.eningqu.domain.api;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @desc TODO  订单返回实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
public class OrdersResult{

    private Long oId;

    private String bOrderNumber;

    private Date bOrderTime;

    private String bOrderStatus;

    private BigDecimal bOriginalPrice;

    private BigDecimal bSolePrice;

    private BigDecimal bCreditTotal;

    public Long getoId() {
        return oId;
    }

    public void setoId(Long oId) {
        this.oId = oId;
    }

    public String getbOrderNumber() {
        return bOrderNumber;
    }

    public void setbOrderNumber(String bOrderNumber) {
        this.bOrderNumber = bOrderNumber;
    }

    public Date getbOrderTime() {
        return bOrderTime;
    }

    public void setbOrderTime(Date bOrderTime) {
        this.bOrderTime = bOrderTime;
    }

    public String getbOrderStatus() {
        return bOrderStatus;
    }

    public void setbOrderStatus(String bOrderStatus) {
        this.bOrderStatus = bOrderStatus;
    }

    public BigDecimal getbOriginalPrice() {
        return bOriginalPrice;
    }

    public void setbOriginalPrice(BigDecimal bOriginalPrice) {
        this.bOriginalPrice = bOriginalPrice;
    }

    public BigDecimal getbSolePrice() {
        return bSolePrice;
    }

    public void setbSolePrice(BigDecimal bSolePrice) {
        this.bSolePrice = bSolePrice;
    }

    public BigDecimal getbCreditTotal() {
        return bCreditTotal;
    }

    public void setbCreditTotal(BigDecimal bCreditTotal) {
        this.bCreditTotal = bCreditTotal;
    }
}
