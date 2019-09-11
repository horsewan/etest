package com.eningqu.params;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 描    述：  TODO  蓝牙设备信息上传参数
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/28 14:06
 */
public class BleDeviceParams {

    @NotBlank(message = "MAC参数不能为空")
    @Pattern(regexp = "^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){5}$", message = "MAC参数格式错误")
    private String bleMac;

    @NotBlank(message = "bleSn参数不能为空")
    private String bleSn;

    private String bleAusn;

    @NotBlank(message = "pkgName参数不能为空")
    private String pkgName;

    @NotBlank(message = "phoneImeiI参数不能为空")
    private String phoneImei;

    @NotBlank(message = "phoneSn参数不能为空")
    private String phoneSn;

    private String phone;

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

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "BleDeviceParams{" +
                "bleMac='" + bleMac + '\'' +
                ", bleSn='" + bleSn + '\'' +
                ", bleAusn='" + bleAusn + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", phoneImei='" + phoneImei + '\'' +
                ", phoneSn='" + phoneSn + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
