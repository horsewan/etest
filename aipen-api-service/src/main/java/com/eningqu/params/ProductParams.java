package com.eningqu.params;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/10 9:46
 * @version    1.0
 *
 **/

public class ProductParams {

    @NotNull(message = "产品ID不能为空")
    @Min(value = 1, message = "产品ID必须是大于0的整数")
    private Long id;

    @NotNull(message = "产品数量不能小于1")
    @Min(value = 1, message = "产品数量不能小于1")
    private int nums;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
}
