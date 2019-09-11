package com.eningqu.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("sys_city")
public class SysCity extends BaseEntity<SysCity> {

    @TableField(value = "city_no")
    private String cityNo;
    @TableField(value = "city_name")
    private String cityName;
    @TableField(value = "sheng_no")
    private String shengNo;
    @TableField(value = "sheng_name")
    private String shengName;
    @TableField(value = "agent_no")
    private String agentNo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getCityNo() {
        return cityNo;
    }

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getShengNo() {
        return shengNo;
    }

    public void setShengNo(String shengNo) {
        this.shengNo = shengNo;
    }

    public String getShengName() {
        return shengName;
    }

    public void setShengName(String shengName) {
        this.shengName = shengName;
    }
}
