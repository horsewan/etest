package com.eningqu.modules.system.vo;

import java.io.Serializable;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO   用户分布统计
 * @date 2019/9/9 13:38
 **/
public class CityGroupVo implements Serializable {

    /**
    * 地区
    */
    private String city;
    /**
     * 人数
     */
    private Integer nums;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }
}
