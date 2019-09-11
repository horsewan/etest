package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @desc TODO  用户日志实体类
 * @author     Yanghuangping
 * @since      2018/9/10 11:06
 * @version    1.0
 *
 **/

@TableName("nq_user_log")
public class UserLogInfo extends BaseEntity<UserLogInfo> {

    @TableField("agent")
    private String agent;

    @TableField("udid")
    private String udid;

    @TableField("token")
    private String token;

    @TableField("appversion")
    private String appversion;

    @TableField("cmd")
    private String cmd;

    @TableField("osversion")
    private String osversion;

    @TableField("appid")
    private String appid;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected Date createTime;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
