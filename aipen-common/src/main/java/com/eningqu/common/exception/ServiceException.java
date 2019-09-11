package com.eningqu.common.exception;/**
 *
 * @desc TODO  service异常类
 * @author     Yanghuangping
 * @since      2018/5/12 17:23
 * @version    1.0
 *
 **/
public class ServiceException extends RuntimeException{
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
