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
@TableName("nq_user_vip_record")
public class UserCreditRecord extends BaseEntity<UserCreditRecord> {

    /**
     * 用户UID
     */
    @TableField("uid")
    private Long uid;

    @TableField("vip_hongbao")
    private long vipHongbao;

    @TableField("vip_status")
    private int vipStatus;
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

    @TableField("fid")
    private Long fid;


    @TableField("remarks")
    private String remarks;

    @TableField("namount")
    private BigDecimal namount;

    @TableField("hbcount")
    private Long hbcount;

    @TableField("ncount")
    private Long ncount;

    @TableField("pvid")
    private Long pvid;

    @TableField("source")
    private String  source;


    private String balance;


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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public long getVipHongbao() {
        return vipHongbao;
    }

    public void setVipHongbao(long vipHongbao) {
        this.vipHongbao = vipHongbao;
    }

    public int getVipStatus() {
        return vipStatus;
    }

    public void setVipStatus(int vipStatus) {
        this.vipStatus = vipStatus;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public BigDecimal getNamount() {
        return namount;
    }

    public void setNamount(BigDecimal namount) {
        this.namount = namount;
    }

    public Long getHbcount() {
        return hbcount;
    }

    public void setHbcount(Long hbcount) {
        this.hbcount = hbcount;
    }

    public Long getNcount() {
        return ncount;
    }

    public void setNcount(Long ncount) {
        this.ncount = ncount;
    }

    public Long getPvid() {
        return pvid;
    }

    public void setPvid(Long pvid) {
        this.pvid = pvid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
