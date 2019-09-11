package com.eningqu.domain.api;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

/**
 *
 * @desc TODO  产品实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
@TableName("dy_product")
public class Product extends DataEntity<Product> {

    /**
     * 产品编号
     */
    @TableField("product_no")
    private String productNo;
    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;
    /**
     * 产品描述
     */
    @TableField("product_desc")
    private String productDesc;
    /**
     * 产品详情
     */
    @TableField("product_detail")
    private String productDetail;
    /**
     * 产品一级分类
     */
    @TableField("one_class")
    private String oneClass;
    /**
     * 产品二级分类
     */
    @TableField("two_class")
    private String twoClass;
    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;
    /**
     * 促销价
     */
    @TableField("sale_price")
    private BigDecimal salePrice;
    /**
     * 缩略图
     */
    @TableField("thumb_pic_url")
    private String thumbPicUrl;
    /**
     * 产品状态  1：未发布 2：待审核 3：已上架  4：已下架
     */
    @TableField("status")
    private String status;
    /**
     * 库存数量
     */
    @TableField("stock_quantity")
    private Integer stockQuantity;
    /**
     * 已售数量
     */
    @TableField("sold_quantity")
    private Integer soldQuantity;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    @TableField(exist = false)
    private List<ProductDetail> detailList;

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

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getOneClass() {
        return oneClass;
    }

    public void setOneClass(String oneClass) {
        this.oneClass = oneClass;
    }

    public String getTwoClass() {
        return twoClass;
    }

    public void setTwoClass(String twoClass) {
        this.twoClass = twoClass;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getThumbPicUrl() {
        return thumbPicUrl;
    }

    public void setThumbPicUrl(String thumbPicUrl) {
        this.thumbPicUrl = thumbPicUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<ProductDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ProductDetail> detailList) {
        this.detailList = detailList;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
