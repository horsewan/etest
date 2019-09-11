package com.eningqu.common.shiro;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.kit.WebKit;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @desc TODO  登录过滤器
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/24 15:42
 **/
public class LoginFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //未登录
        if (!ShiroKit.isLogin()) {
            //异步请求
            if (WebKit.isAjaxRequest((HttpServletRequest) request)) {
                RJson rJson = RJson.error().setCode(RJson.UN_LOGIN).setMsg(RJson.DEFAULT_UNLOGIN_TIPS).setData("/login");
                WebKit.writeJson((HttpServletResponse) response, rJson);
                return false;
            }
            WebKit.redirectURL((HttpServletResponse) response, "/login");
            return false;
        }
        return super.onAccessDenied(request, response);
    }
}
