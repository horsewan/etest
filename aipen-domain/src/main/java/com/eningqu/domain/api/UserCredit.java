package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @desc TODO  用户积分实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("nq_user_vip")
public class UserCredit extends BaseEntity<UserCredit> {

    /**
     * 用户UID
     */
    @TableField("uid")
    private Long uid;
    /**
     * 等级
     */
    @TableField("vip_level")
    private long vipLevel;

    /**
     * 积分
     */
    @TableField("vip_credit")
    private BigDecimal vipCredit;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    public long getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(long vipLevel) {
        this.vipLevel = vipLevel;
    }

    public BigDecimal getVipCredit() {
        return vipCredit;
    }

    public void setVipCredit(BigDecimal vipCredit) {
        this.vipCredit = vipCredit;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

}
