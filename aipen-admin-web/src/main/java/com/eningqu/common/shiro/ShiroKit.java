package com.eningqu.common.shiro;
import com.eningqu.modules.system.vo.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/25 19:52
 * @version    1.0
 *
 **/
public class ShiroKit {

    public static Long id() {
        return loginInfo().getId();
    }

    public static String loginName() {
        ShiroUser shiroUser = loginInfo();
        return shiroUser != null ? shiroUser.getLoginName() : "";
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static ShiroUser loginInfo() {
        Subject subject = SecurityUtils.getSubject();
        return (ShiroUser) subject.getPrincipal();
    }

    public static void logout() {
        if(getSubject() != null){
            getSubject().logout();
        }
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSubject().getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSubject().getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return getSubject().getPrincipal() != null;
    }
}
