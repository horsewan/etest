package com.eningqu.common.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * 描    述：  TODO  Shiro属性类
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/21 17:15
 *
 */
@Configuration
@ConfigurationProperties(prefix="shiro")
public class ShiroProperties {

    private List<String> anonUrls;

    public void setAnonUrls(List<String> anonUrls) {
        this.anonUrls = anonUrls;
    }
    public List<String> getAnonUrls() {
        return anonUrls;
    }
}
