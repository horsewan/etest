package com.eningqu.common.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 *
 * @desc TODO  创蓝短信配置类
 * @author     Yanghuangping
 * @since      2018/8/28 16:06
 * @version    1.0
 *
 **/
@Configuration
@ConfigurationProperties(prefix = "sms")
public class CLSmsConfig {

    @NotNull
    private Map<String, CLSmsProperties> tag;

    public Map<String, CLSmsProperties> getTag() {
        return tag;
    }

    public void setTag(Map<String, CLSmsProperties> tag) {
        this.tag = tag;
    }
}
