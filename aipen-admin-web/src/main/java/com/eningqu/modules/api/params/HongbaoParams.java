package com.eningqu.modules.api.params;

import com.eningqu.common.kit.ValidationKit;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/18 16:49
 * @version    1.0
 *
 **/

public class HongbaoParams {

    @NotNull(message = "ID不能为空", groups = {ValidationKit.EditValid.class})
    @Min(value = 1, message = "ID不正确")
    private Long id;
    @NotBlank(message = "产品编号不能为空", groups = {ValidationKit.EditValid.class})
    private String productNo;
    @NotBlank(message = "产品名称不能为空")
    private String productName;
//    @NotBlank(message = "产品简称不能为空")
    private String productDesc;
    @NotBlank(message = "一类分类不能为空")
    private String oneClass;
    private String twoClass;
//    @NotNull(message = "原价不能为空")
    private BigDecimal originalPrice;
//    @NotNull(message = "促销价不能为空")
    private BigDecimal salePrice;
//    @NotNull(message = "库存量不能为空")
//    @Min(value = 0, message = "库存量最小值为0")
    private Integer stockQuantity;
    @NotBlank(message = "缩略图不能为空")
    private String thumbPicUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getThumbPicUrl() {
        return thumbPicUrl;
    }

    public void setThumbPicUrl(String thumbPicUrl) {
        this.thumbPicUrl = thumbPicUrl;
    }
}
