package com.eningqu.common.sms;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/28 19:47
 * @version    1.0
 *
 **/

public class CLSmsProperties {

    @NotBlank
    private String account;
    @NotBlank
    private String password;
    @NotBlank
    private String url;
    @NotNull
    private List<String> tpl;
    /** 验证码过期时间 */
    @NotBlank
    private int timeout;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTpl() {
        return tpl;
    }

    public void setTpl(List<String> tpl) {
        this.tpl = tpl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
