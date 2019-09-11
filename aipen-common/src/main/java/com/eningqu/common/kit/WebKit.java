package com.eningqu.common.kit;

import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/26 11:14
 * @version    1.0
 *
 **/
public class WebKit {

    private final static Logger logger = LoggerFactory.getLogger(WebKit.class);

    /**
     * TODO 获取 request
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * TODO 获取 session
     * @return
     */
    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession();
        return session;

    }

    /**
     * TODO 设置request值
     * @param key
     * @param value
     */
    public static void setRequestValue(String key, Object value){
        HttpServletRequest request = getRequest();
        request.setAttribute(key, value);
    }

    /**
     * TODO 获取reqeust值
     * @param key
     * @return
     */
    public static Object getRequestValue(String key){
        HttpServletRequest request = getRequest();
        return request.getAttribute(key);
    }

    /**
     * TODO 设置session值
     * @param key
     * @param value
     */
    public static void setSessionValue(String key, Object value){
        HttpSession session = getRequest().getSession();
        session.setAttribute(key, value);
    }

    /**
     * TODO 获取session值
     * @param key
     * @return
     */
    public static Object getSessionValue(String key){
        HttpSession session = getRequest().getSession();
        return session.getAttribute(key);
    }

    /**
     * 是否是ajax请求
     * @param request
     * @return
     */
    public static boolean   isAjaxRequest(HttpServletRequest request){
        return (!StringUtils.isBlank(request.getHeader("X-Requested-With"))
                && StringUtils.equalsIgnoreCase(request.getHeader("X-Requested-With"), "XMLHttpRequest"));
    }

    /**
     * 输出JSON
     * @param response
     */
    public static void writeJson(HttpServletResponse response, Object data) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSONUtil.toJsonStr(data));
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 响应重定向
     * @param response
     * @param url
     */
    public static void redirectURL(HttpServletResponse response, String url){
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * TODO 获取请求地址
     * @return
     */
    public static String baseURL(HttpServletRequest request){
        StringBuffer sb = new StringBuffer();
        sb.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort());
        logger.info("请求地址前缀=" + sb.toString());
        return sb.toString();
    }
}
