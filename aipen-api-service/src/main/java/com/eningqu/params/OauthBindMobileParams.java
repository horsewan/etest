package com.eningqu.params;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/26 14:37
 * @version    1.0
 *
 **/

public class OauthBindMobileParams {

    @NotBlank(message = "openid不能为空")
    private String openId;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "应用包名不能为空")
    private String pkgName;
    @NotBlank(message = "验证码不能为空")
    private String code;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
