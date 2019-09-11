package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO  用户订单地址实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("nq_user_order_address")
public class UserOrderAddress extends DataEntity<UserOrderAddress> {

    @TableField("order_number")
    private String orderNumber;

    @TableField("adid")
    private Long adid;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Long getAdid() {
        return adid;
    }

    public void setAdid(Long adid) {
        this.adid = adid;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
