package com.eningqu.domain.api;

import java.math.BigDecimal;

/**
 *
 * @desc TODO  产品实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
public class HongbaoResult {

    private Long pId;
    /**
     * 产品编号
     */
    private String productNo;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品描述
     */
    private String productDesc;
    /**
     * 原价
     */
    private BigDecimal hongbaoPrice;

    private String thumbPicUrl;

    private String hongbaoSta;


    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getThumbPicUrl() {
        return thumbPicUrl;
    }

    public void setThumbPicUrl(String thumbPicUrl) {
        this.thumbPicUrl = thumbPicUrl;
    }

    public BigDecimal getHongbaoPrice() {
        return hongbaoPrice;
    }

    public void setHongbaoPrice(BigDecimal hongbaoPrice) {
        this.hongbaoPrice = hongbaoPrice;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getHongbaoSta() {
        return hongbaoSta;
    }

    public void setHongbaoSta(String hongbaoSta) {
        this.hongbaoSta = hongbaoSta;
    }
}
