package com.eningqu.common.interceptor;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.kit.WebKit;
import com.eningqu.vo.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  JWT TOKEN验证拦截器
 * @date 2018/4/27 20:04
 **/
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisKit redisKit;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String request_url = request.getRequestURI();

        // 请求头中获取TOKEN
        String authorized_token = request.getHeader(Global.AUTH_TOKEN);
        if (StringUtils.isBlank(authorized_token)) {
            logger.info("请求URL:{},在请求头中未获取到TOKEN信息", request_url);
            RJson rJson = RJson.unLogin();
            WebKit.writeJson(response, rJson);
            return false;
        }

        // 缓存中获取用户信息
        LoginInfo loginInfo = (LoginInfo) redisKit.get(Global.REDIS_TOKEN_PREFIX + authorized_token);
        if (loginInfo == null) {
            logger.info("请求URL:{},TOKEN:{},未能获取到登录用户信息", request_url, authorized_token);
            RJson rJson = RJson.unLogin();
            WebKit.writeJson(response, rJson);
            return false;
        }

        // 延长缓存过期时间
        redisKit.expire(Global.REDIS_TOKEN_PREFIX + authorized_token, Global.SSESSION_EXPIRE_TIME);
        try {
            //刷新登录用户数
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String today = format.format(new Date());
            if (!StringUtils.isEmpty(loginInfo.getAgentNo())) {
                String agentNo = loginInfo.getAgentNo().substring(0, 2);
                redisKit.setBit(Global.API_LOGIN_INFO + agentNo + ":" + today, loginInfo.getId(), true);
            }
            redisKit.setBit(Global.API_LOGIN_INFO + today, loginInfo.getId(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把用户信息设置在request中
        request.setAttribute(Global.API_LOGIN_INFO, loginInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
