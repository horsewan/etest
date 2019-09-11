package com.eningqu.common.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.util.Date;

/**
 *
 * @desc TODO  数据公共字段基础类
 * @author     Yanghuangping
 * @date       2018/4/18 9:56
 * @version    1.0
 *
 **/
public abstract class DataEntity <T extends Model> extends BaseEntity<T> {

    /**创建者*/
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    protected Long createId;

    /**创建日期*/
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;

    /**更新者*/
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
    protected Long updateId;

    /**更新日期*/
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
