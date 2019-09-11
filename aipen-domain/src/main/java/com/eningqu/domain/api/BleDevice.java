package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @desc TODO  蓝牙设备实体类
 * @author     Yanghuangping
 * @since      2018/8/20 16:50
 * @version    1.0
 *
 **/

@TableName("dy_single_agent")
public class BleDevice extends BaseEntity<BleDevice> {

    /*** 蓝牙设备密文mac地址 */
    @TableField("mac")
    private String mac;
    /*** 蓝牙设备SN */
    @TableField("ble_sn")
    private String bleSn;
    /*** 蓝牙设备密文 */
    @TableField("ausn")
    private String ausn;
    /*** 可用状态 Y：可用  N：不可用*/
    @TableField("enable_status")
    private String enableStatus;
    /**创建日期*/
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;
    @Override
    protected Serializable pkVal() {
        return id;
    }

    public String getBleSn() {
        return bleSn;
    }

    public void setBleSn(String bleSn) {
        this.bleSn = bleSn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAusn() {
        return ausn;
    }

    public void setAusn(String ausn) {
        this.ausn = ausn;
    }

}
