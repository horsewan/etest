package com.eningqu.params;


import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 *
 * @desc TODO  登录参数
 * @author     Yanghuangping
 * @date       2018/5/8 15:16
 * @version    1.0
 *
 **/
public class LoginParams implements Serializable{

    @NotBlank(message = "openId不能为空")
    private String openId;
    private String nickName;
    private String headImg;
    private String sex;
    @NotBlank(message = "app包名不能为空")
    private String pkgName;
    @NotBlank(message = "用户类型不能为空")
    @Pattern(regexp = "1|2|3|4|5", message = "用户类型匹配不正确")
    private String userType;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
