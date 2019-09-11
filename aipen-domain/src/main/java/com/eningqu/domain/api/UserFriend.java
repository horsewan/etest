package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;


@TableName("nq_user_friend")
public class UserFriend extends BaseEntity<UserFriend> {

    /**
     * 手机号
     */
    @TableField("friend_mobile")
    private String friendMobile;

    /**
     * 用户昵称
     */
    @TableField("friend_nickname")
    private String friendNickName;

    /**
     * 用户好友Id
     */
    @TableField("user_friend_id")
    private Long userFriendId;

    /**
     * 是否是AI助手
     */
    @TableField("isai")
    private String isai;

    /**
     * 黑名单
     */
    @TableField("blacklist")
    private String blacklist;

    /**
     * 好友类型
     */
    @TableField("ftype")
    private String ftype;



    @TableField("sex")
    private String sex;

    @TableField("head_img")
    private String headImg;

    @TableField("agent_no")
    private String agent_no;

    @TableField("address_x")
    private String address_x;

    @TableField("address_y")
    private String address_y;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @TableField("remarks")  // 个性签名
    private String remarks;


    @TableField("address_d")
    private String address_d;

    private String fremarks;



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


    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }


    public String getFriendMobile() {
        return friendMobile;
    }

    public void setFriendMobile(String friendMobile) {
        this.friendMobile = friendMobile;
    }


    public Long getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(Long userFriendId) {
        this.userFriendId = userFriendId;
    }



    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getAgent_no() {
        return agent_no;
    }

    public void setAgent_no(String agent_no) {
        this.agent_no = agent_no;
    }

    public String getAddress_x() {
        return address_x;
    }

    public void setAddress_x(String address_x) {
        this.address_x = address_x;
    }

    public String getAddress_y() {
        return address_y;
    }

    public void setAddress_y(String address_y) {
        this.address_y = address_y;
    }

    public String getAddress_d() {
        return address_d;
    }

    public void setAddress_d(String address_d) {
        this.address_d = address_d;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public String getFremarks() {
        return fremarks;
    }

    public void setFremarks(String fremarks) {
        this.fremarks = fremarks;
    }

    public String getIsai() {
        return isai;
    }

    public void setIsai(String isai) {
        this.isai = isai;
    }
}
