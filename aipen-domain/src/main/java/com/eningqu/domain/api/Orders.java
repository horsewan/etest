package com.eningqu.domain.api;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.eningqu.common.base.entity.BaseEntity;

/**
 *
 * @desc TODO  订单实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
@TableName("dy_orders")
public class Orders extends BaseEntity<Orders> {

    /**
     * 用户UID
     */
    @TableField("uid")
    private Long uid;
    /**
     * 商户id
     */
    @TableField("business_id")
    private Long businessId;
    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;
    /**
     * 订单状态
     */
    @TableField("order_status")
    private String orderStatus;
    /**
     * 流水号
     */
    @TableField("serial_number")
    private String serialNumber;
    /**
     * 快递公司
     */
    @TableField("exp_company")
    private String expCompany;
    /**
     * 快递公司编码
     */
    @TableField("exp_code")
    private String expCode;
    /**
     * 快递单号
     */
    @TableField("exp_no")
    private String expNo;
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
     * 收件地址
     */
    @TableField("sign_address")
    private String signAddress;
    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;
    /**
     * 支付支付
     */
    @TableField("pay_way")
    private String payWay;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 应付金额
     */
    @TableField("amount_payable")
    private BigDecimal amountPayable;
    /**
     * 实收金额
     */
    @TableField("amount_realpay")
    private BigDecimal amountRealpay;

    /**
     * 更新人
     */
    @TableField("update_name")
    private String updateName;

    /**更新日期*/
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getExpCompany() {
        return expCompany;
    }

    public void setExpCompany(String expCompany) {
        this.expCompany = expCompany;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
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

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(BigDecimal amountPayable) {
        this.amountPayable = amountPayable;
    }

    public BigDecimal getAmountRealpay() {
        return amountRealpay;
    }

    public void setAmountRealpay(BigDecimal amountRealpay) {
        this.amountRealpay = amountRealpay;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
