package com.eningqu.common.exp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @desc TODO  快递接口配置类
 * @author     Yanghuangping
 * @since      2018/7/17 19:30
 * @version    1.0
 *
 **/

@Configuration
public class ExpConfig {

    @Value("${exp.appId}")
    private String appId;
    @Value("${exp.appKey}")
    private String appKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
