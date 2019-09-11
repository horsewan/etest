package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @desc TODO  用户信息实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("nq_user_info")
public class UserInfo extends BaseEntity<UserInfo> {

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 用户头像
     */
    @TableField("head_img")
    private String headImg;
    /**
     * 性别
     */
    @TableField("sex")
    private String sex;
    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;
    /**
     * 密码凭证
     */
    @TableField("credential")
    private String credential;
    /**
     * 出生年月
     */
    @TableField("birthday")
    private Date birthday;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 删除标记(Y:未删除；N:已删除)
     */
    @TableField("del_status")
    private String delStatus;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    @TableField("agent_no")
    private String agentNo;

    @TableField("agent_level")
    private String agentLevel;

    @TableField("address_x")
    private String addressX;

    @TableField("address_y")
    private String addressY;

    @TableField("address_d")
    private String addressD;

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentLevel() {
        return agentLevel;
    }

    public UserInfo setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
        return this;
    }

    /***
     * 注册时间
     * */
    @TableField(value = "register_time", fill = FieldFill.INSERT)
    private Date registerTime;
    /***
     * 更新时间
     * */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getAddressD() {
        return addressD;
    }

    public void setAddressD(String addressD) {
        this.addressD = addressD;
    }
}
