package com.eningqu.common.shiro;

import com.eningqu.common.kit.ServletKit;
import org.apache.shiro.session.Session;
import org.crazycake.shiro.RedisSessionDAO;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/27 15:28
 * @version    1.0
 *
 **/
public class ShiroRedisSessionDAO extends RedisSessionDAO{

    private final static List<String> EXCLUDE_URLS = new ArrayList<String>(){
        {
            add("/css/");
            add("/js/");
            add("/images/");
            add("/fonts/");
            add("/layui/");
            add("/druid/");
        }
    };

    @Override
    protected Session doReadSession(Serializable sessionId) {
        //判断是否是静态资源
        if(isStatisUrl(ServletKit.getRequest())){
            return null;
        }
        return super.doReadSession(sessionId);
    }

    private boolean isStatisUrl(HttpServletRequest request){
        if(request == null){
            return true;
        }
        String staticUrl = request.getServletPath();
        for (String url : EXCLUDE_URLS) {
            if(staticUrl.contains(url)){
                return true;
            }
        }
        return false;
    }
}
