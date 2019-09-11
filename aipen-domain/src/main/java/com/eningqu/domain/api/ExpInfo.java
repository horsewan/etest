package com.eningqu.domain.api;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

/**
 *
 * @desc TODO  收件地址实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
@TableName("nq_exp_info")
public class ExpInfo extends BaseEntity<ExpInfo> {

    /**
     * 用户UID
     */
    @TableField("uid")
    private Long uid;
    /**
     * 收件人
     */
    @TableField("sign_name")
    private String signName;
    /**
     * 联系方式
     */
    @TableField("sign_phone")
    private String signPhone;
    /**
     * 收货地址
     */
    @TableField("address")
    private String address;
    /**
     * 是否为默认地址
     */
    @TableField("is_default")
    private String isDefault;
    /**
     * 删除标记
     */
    @TableField("del_status")
    private String delStatus;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
