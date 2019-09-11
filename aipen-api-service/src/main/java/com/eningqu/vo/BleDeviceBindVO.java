package com.eningqu.vo;
/**
 *
 * 描    述：  TODO   
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/29 15:17
 *
 */
public class BleDeviceBindVO {

    /** 蓝牙设备ID */
    private Long bleId;
    /** 蓝牙设备MAC */
    private String bleMac;
    /** 蓝牙设备SN */
    private String bleSn;
    /** 蓝牙设备秘文 */
    private String bleAusn;
    /** 蓝牙设备可用状态 */
    private String enableStatus;
    /** 应用设备IMEI */
    private String phoneImei;
    /** 应用设备SN */
    private String phoneSn;

    public Long getBleId() {
        return bleId;
    }

    public void setBleId(Long bleId) {
        this.bleId = bleId;
    }

    public String getBleMac() {
        return bleMac;
    }

    public void setBleMac(String bleMac) {
        this.bleMac = bleMac;
    }

    public String getBleSn() {
        return bleSn;
    }

    public void setBleSn(String bleSn) {
        this.bleSn = bleSn;
    }

    public String getBleAusn() {
        return bleAusn;
    }

    public void setBleAusn(String bleAusn) {
        this.bleAusn = bleAusn;
    }

    public String getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getPhoneImei() {
        return phoneImei;
    }

    public void setPhoneImei(String phoneImei) {
        this.phoneImei = phoneImei;
    }

    public String getPhoneSn() {
        return phoneSn;
    }

    public void setPhoneSn(String phoneSn) {
        this.phoneSn = phoneSn;
    }
}
