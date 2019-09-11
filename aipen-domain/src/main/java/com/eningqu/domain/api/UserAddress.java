package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO  用户地址实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("nq_user_address")
public class UserAddress extends DataEntity<UserAddress> {

    @TableField("uid")
    private Long uId;

    @TableField("a_nick")
    private String aNick;

    @TableField("a_phone")
    private String aPhone;

    @TableField("a_address")
    private String aAddress;

    @TableField("is_def")
    private String isDef;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getaNick() {
        return aNick;
    }

    public void setaNick(String aNick) {
        this.aNick = aNick;
    }

    public String getaPhone() {
        return aPhone;
    }

    public void setaPhone(String aPhone) {
        this.aPhone = aPhone;
    }

    public String getaAddress() {
        return aAddress;
    }

    public void setaAddress(String aAddress) {
        this.aAddress = aAddress;
    }

    public String getIsDef() {
        return isDef;
    }

    public void setIsDef(String isDef) {
        this.isDef = isDef;
    }
}
