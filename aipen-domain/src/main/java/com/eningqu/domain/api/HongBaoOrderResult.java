package com.eningqu.domain.api;

import java.math.BigDecimal;
import java.util.Date;

public class HongBaoOrderResult {

    private Long bId;

    private Long oId;

    private String bOrderNumber;

    private Date bOrderTime;

    private String bOrderStatus;

    private BigDecimal bCreditTotal;

    private BigDecimal bOriginalPrice;

    public Long getbId() {
        return bId;
    }

    public void setbId(Long bId) {
        this.bId = bId;
    }

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

    public BigDecimal getbCreditTotal() {
        return bCreditTotal;
    }

    public void setbCreditTotal(BigDecimal bCreditTotal) {
        this.bCreditTotal = bCreditTotal;
    }

    public BigDecimal getbOriginalPrice() {
        return bOriginalPrice;
    }

    public void setbOriginalPrice(BigDecimal bOriginalPrice) {
        this.bOriginalPrice = bOriginalPrice;
    }
}
