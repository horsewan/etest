package com.eningqu.domain.api;

import java.io.Serializable;
import java.util.Date;

/**
 * dy_user_mobilelist
 * @author 
 */
public class DyUserMobilelist implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 我的手机
     */
    private String myMobile;

    /**
     * 通讯录手机
     */
    private String listMobile;

    /**
     * 通讯录名称
     */
    private String listName;

    /**
     * 创建者
     */
    private Long createId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateId;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 说明
     */
    private String remarks;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMyMobile() {
        return myMobile;
    }

    public void setMyMobile(String myMobile) {
        this.myMobile = myMobile;
    }

    public String getListMobile() {
        return listMobile;
    }

    public void setListMobile(String listMobile) {
        this.listMobile = listMobile;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setId(Long id) {
        this.id = id;
    }


}