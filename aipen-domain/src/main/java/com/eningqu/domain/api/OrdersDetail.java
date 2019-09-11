package com.eningqu.domain.api;

import java.io.Serializable;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

/**
 *
 * @desc TODO  订单详情实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
@TableName("dy_orders_detail")
public class OrdersDetail extends BaseEntity<OrdersDetail> {

    @TableField("uid")
    private Long uid;
    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;
    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 下单价格
     */
    @TableField("order_price")
    private BigDecimal orderPrice;
    /**
     * 下单数量
     */
    @TableField("order_quantity")
    private Integer orderQuantity;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrdersDetail{" +
        "orderNumber=" + orderNumber +
        ", productId=" + productId +
        ", orderPrice=" + orderPrice +
        ", orderQuantity=" + orderQuantity +
        ", remarks=" + remarks +
        "}";
    }


}
