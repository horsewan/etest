package com.eningqu.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @date 2018/4/18 19:36
 **/
@TableName("sys_log")
public class  SysLog extends BaseEntity<SysLog> {

    /**
     * 日志类型
     */
    @TableField("log_type")
    private String logType;
    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;
    /**
     * 操作用户
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 操作时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 操作功能
     */
    @TableField("func")
    private String func;
    /**
     * 请求URI
     */
    @TableField("url")
    private String url;
    /**
     * 请求参数
     */
    @TableField("params")
    private String params;
    /**
     * 操作结果(成功 或 失败)
     */
    @TableField("result")
    private String result;
    /**
     * 异常信息
     */
    @TableField("exception")
    private String exception;


    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
