package com.eningqu.modules.system.vo;

import java.io.Serializable;

/**
 *
 * @desc TODO  系统用户登录信息
 * @author     Yanghuangping
 * @date       2018/4/25 19:53
 * @version    1.0
 *
 **/
public class ShiroUser implements Serializable{

    private static final long serialVersionUID = -1L;

    public Long id;
    public String loginName;
    public String userType;

    public ShiroUser(Long id, String loginName, String userType) {
        this.id = id;
        this.loginName = loginName;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
