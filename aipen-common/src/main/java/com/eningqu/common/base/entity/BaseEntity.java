package com.eningqu.common.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 *
 * @desc TODO  entity基础支持类
 * @author     Yanghuangping
 * @date       2018/4/18 9:51
 * @version    1.0
 *
 **/
public abstract class BaseEntity <T extends Model> extends Model<T>{

    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;

    public BaseEntity() {}

    public BaseEntity(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
}
