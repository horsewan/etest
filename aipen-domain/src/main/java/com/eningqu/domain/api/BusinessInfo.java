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
 * @desc TODO  商户实体类
 * @author     Yanghuangping
 * @since      2018/8/20 16:50
 * @version    1.0
 *
 **/

@TableName("dy_business_info")
public class BusinessInfo extends BaseEntity<BusinessInfo> {
    @TableField("b_name")
    private String bName;

    @TableField("b_ticket")
    private String bTicket;

    @TableField("b_single_no")
    private String bSingleNo;

    @TableField("b_person")
    private String bPerson;

    @TableField("b_phone")
    private String bPhone;

    @TableField("b_address")
    private String bAddress;

    @TableField("b_type")
    private Long bType;

    @TableField("b_sole_price")
    private BigDecimal bSolePrice;

    @TableField("b_price")
    private BigDecimal bPrice;

    @TableField("remarks")
    private String remarks;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

    @TableField("b_sign")
    private String bSign;

    @TableField("b_cdn_qr")
    private String bCdnQr;

    @TableField("address_x")
    private String addressX;

    @TableField("address_y")
    private String addressY;

    @TableField("mch_id")
    private String mchId;

    @TableField("mch_key")
    private String mchKey;

    @TableField("mch_sta")
    private String mchSta;

    @TableField("del_status")
    private String delStatus;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbTicket() {
        return bTicket;
    }

    public void setbTicket(String bTicket) {
        this.bTicket = bTicket;
    }

    public String getbSingleNo() {
        return bSingleNo;
    }

    public void setbSingleNo(String bSingleNo) {
        this.bSingleNo = bSingleNo;
    }

    public String getbPerson() {
        return bPerson;
    }

    public void setbPerson(String bPerson) {
        this.bPerson = bPerson;
    }

    public String getbPhone() {
        return bPhone;
    }

    public void setbPhone(String bPhone) {
        this.bPhone = bPhone;
    }

    public String getbAddress() {
        return bAddress;
    }

    public void setbAddress(String bAddress) {
        this.bAddress = bAddress;
    }

    public Long getbType() {
        return bType;
    }

    public void setbType(Long bType) {
        this.bType = bType;
    }

    public BigDecimal getbSolePrice() {
        return bSolePrice;
    }

    public void setbSolePrice(BigDecimal bSolePrice) {
        this.bSolePrice = bSolePrice;
    }

    public BigDecimal getbPrice() {
        return bPrice;
    }

    public void setbPrice(BigDecimal bPrice) {
        this.bPrice = bPrice;
    }

    public String getbSign() {
        return bSign;
    }

    public void setbSign(String bSign) {
        this.bSign = bSign;
    }

    public String getbCdnQr() {
        return bCdnQr;
    }

    public void setbCdnQr(String bCdnQr) {
        this.bCdnQr = bCdnQr;
    }

    public String getAddressX() {
        return addressX;
    }

    public void setAddressX(String addressX) {
        this.addressX = addressX;
    }

    public String getAddressY() {
        return addressY;
    }

    public void setAddressY(String addressY) {
        this.addressY = addressY;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getMchSta() {
        return mchSta;
    }

    public void setMchSta(String mchSta) {
        this.mchSta = mchSta;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }
}
