package com.eningqu.common.exception;

import com.eningqu.common.base.vo.RJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @desc TODO  全局异常处理
 * @author     Yanghuangping
 * @date       2018/5/7 17:13
 * @version    1.0
 *
 **/

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * TODO 业务逻辑异常
     */
    @ExceptionHandler({AipenException.class})
    @ResponseStatus(HttpStatus.OK)
    public RJson aipenException(AipenException e) {
        logger.error("", e.getMessage());
        return RJson.error(e.getCode(), e.getMessage());
    }

    /**
     * TODO 业务逻辑异常
     */
    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RJson serviceException(ServiceException e) {
        logger.error("", e);
        return RJson.error().setMsg("操作失败");
    }

    /**
     * TODO 请求参数验证错误
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RJson methodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("", e);
        return RJson.error().setMsg("请求参数验证错误");
    }

    /**
     * TODO 请求参数不存在
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RJson missingRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("", e);
        return RJson.error().setMsg("请求参数错误");
    }

    /**
     * TODO 请求URL错误
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RJson httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        logger.error("请求地址：{} 请求方式错误, 不支持{}方法", request.getServletPath(), e.getMethod());
        return RJson.error().setMsg("请求URL错误");
    }


    /**
     * TODO 其他异常
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RJson exception(Exception e) {
        logger.error("", e);
        return RJson.error().setMsg("请求失败");
    }
}
