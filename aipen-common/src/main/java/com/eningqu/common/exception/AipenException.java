package com.eningqu.common.exception;

import com.eningqu.common.base.vo.RJson;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/5/7 19:09
 * @version    1.0
 *
 **/
public class AipenException extends RuntimeException{

    private int code = RJson.FAIL;

    public AipenException() {
        super();
    }

    public AipenException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AipenException(String message) {
        super(message);
    }

    public AipenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AipenException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }
}
