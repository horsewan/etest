package com.eningqu.domain.api;

import java.math.BigDecimal;

/**
 *
 * @desc TODO  商户实体类
 * @author     Yanghuangping
 * @since      2018/8/20 16:50
 * @version    1.0
 *
 **/

public class BusinessInfoNear {
    private Long id;
    private String bName;
    private Long bType;
    private BigDecimal bSolePrice;
    private String addressX;
    private String addressY;

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public Long getbType() {
        return bType;
    }

    public void setbType(Long bType) {
        this.bType = bType;
    }

    public BigDecimal getbSolePrice() {
        return bSolePrice;
    }

    public void setbSolePrice(BigDecimal bSolePrice) {
        this.bSolePrice = bSolePrice;
    }

    public String getAddressX() {
        return addressX;
    }

    public void setAddressX(String addressX) {
        this.addressX = addressX;
    }

    public String getAddressY() {
        return addressY;
    }

    public void setAddressY(String addressY) {
        this.addressY = addressY;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
