package com.eningqu.common.exception;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.kit.WebKit;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @desc TODO  全局异常处理
 * @author     Yanghuangping
 * @date       2018/5/7 17:13
 * @version    1.0
 *
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * TODO 业务逻辑异常处理
     */
    @ExceptionHandler({ AipenException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void aipenException(HttpServletRequest request, HttpServletResponse response, AipenException ex) {
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(ex.getMessage()).setData("/login");
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/login");
        }
    }

    /**
     * TODO 登录认证异常处理
     */
    @ExceptionHandler({ AuthenticationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void authenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg("用户名或密码错误").setData("/login");
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/login");
        }
    }

    /**
     * TODO 权限认证异常处理
     */
    @ExceptionHandler({AuthorizationException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void authorizationException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) {
        logger.error("", e.getMessage());
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.UNAUTHORIZED).setMsg(RJson.DEFAULT_UNAUTHORIZED_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/403");
        }
    }

    /**
     * TODO 请求参数验证错误
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void methodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(RJson.DEFAULT_FAIL_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/404");
        }
    }

    /**
     * TODO 请求参数不存在
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void missingRequestParameterException(HttpServletRequest request, HttpServletResponse response, MissingServletRequestParameterException e) {
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(RJson.DEFAULT_FAIL_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/404");
        }
    }

    /**
     * TODO 请求URL错误
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void httpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(RJson.DEFAULT_FAIL_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/404");
        }
    }

    @ExceptionHandler({ConversionNotSupportedException.class,HttpMessageNotWritableException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void server500(HttpServletRequest request, HttpServletResponse response, RuntimeException e){
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(RJson.DEFAULT_FAIL_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/500");
        }
    }

    /**
     * TODO 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void runtimeException(HttpServletRequest request, HttpServletResponse response, RuntimeException e) {
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(RJson.DEFAULT_FAIL_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/500");
        }
    }

    /**
     * TODO 其他异常
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error("", e);
        if (WebKit.isAjaxRequest(request)) {
            RJson rJson = RJson.error().setCode(RJson.FAIL).setMsg(RJson.DEFAULT_FAIL_TIPS);
            WebKit.writeJson(response, rJson);
        } else {
            WebKit.redirectURL(response, "/500");
        }
    }
}
