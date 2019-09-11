package com.eningqu.vo;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/9 11:29
 * @version    1.0
 *
 **/

public class CartListVO {
    private int buyQuantity;
    private Long productId;
    private String productName;
    private String productDesc;
    private String thumbPicUrl;

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(int buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
}
