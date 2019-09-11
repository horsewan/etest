package com.eningqu.params;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/10 9:46
 * @version    1.0
 *
 **/

public class OrderParams {

    @NotBlank(message = "收件人不能为空")
    private String signName;
    @NotBlank(message = "联系电话不能为空")
    private String signPhone;
    @NotBlank(message = "收件地址不能为空")
    private String address;

    @Valid
    @NotNull(message = "购买的产品不能为空")
    private List<ProductParams> products;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSignPhone() {
        return signPhone;
    }

    public void setSignPhone(String signPhone) {
        this.signPhone = signPhone;
    }

    public List<ProductParams> getProducts() {
        return products;
    }

    public void setProducts(List<ProductParams> products) {
        this.products = products;
    }
}
