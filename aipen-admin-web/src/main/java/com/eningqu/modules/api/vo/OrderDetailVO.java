package com.eningqu.modules.api.vo;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/24 19:05
 * @version    1.0
 *
 **/

public class OrderDetailVO {

    private String productName;
    private String orderPrice;
    private String orderQuantity;
    private String productSalePrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getProductSalePrice() {
        return productSalePrice;
    }

    public void setProductSalePrice(String productSalePrice) {
        this.productSalePrice = productSalePrice;
    }
}
