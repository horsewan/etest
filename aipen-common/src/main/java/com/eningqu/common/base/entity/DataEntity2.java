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
public abstract class DataEntity2<T extends Model> extends BaseEntity<T> {

    /**创建日期*/
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
