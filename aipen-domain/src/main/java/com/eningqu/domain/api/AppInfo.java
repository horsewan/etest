package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity2;

import java.io.Serializable;

/**
 *
 * @desc TODO  团代实体类
 * @author     Yanghuangping
 * @since      2018/9/10 11:06
 * @version    1.0
 *
 **/

@TableName("dy_team_agent")
public class AppInfo extends DataEntity2<AppInfo> {

    /** 应用名称 */
    @TableField("app_name")
    private String appName;
    /** 包名 */
    @TableField("app_pkg")
    private String appPkg;
    /** 应用签名 */
    @TableField("signature")
    private String signature;
    /** 应用开关*/
    @TableField("enable_status")
    private String enableStatus;
    @Override
    protected Serializable pkVal() {
        return id;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public void setAppPkg(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }
}
