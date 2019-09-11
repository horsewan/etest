package com.eningqu.domain.api;

import java.math.BigDecimal;
import java.util.Date;

public class BusinessOrderResult {

    private Long bId;

    private Long oId;

    private String bName;

    private String bOrderNumber;

    private Date bOrderTime;

    private String bOrderStatus;

    private BigDecimal bSolePrice;

    private BigDecimal bCreditTotal;

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

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
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
