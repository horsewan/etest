package com.eningqu.vo;

import com.eningqu.common.constant.Global;
import com.eningqu.common.kit.WebKit;

public class LoginInfoHelper {
    // 获取session中的用户id
    public static Long getUserID() {
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        if(loginInfo!=null){
            return loginInfo.getId();
        }else{
            return null;
        }
    }

    // 获取session中的手机号
    public static String getUserMobile() {
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        if(loginInfo!=null){
            return loginInfo.getMobile();
        }else{
            return null;
        }
    }
}
