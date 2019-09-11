package com.eningqu.common.base.vo;
/**
 *
 * @desc TODO  参数验证结果VO
 * @author     Yanghuangping
 * @date       2018/5/4 10:41
 * @version    1.0
 *
 **/
public class ValidJson {

    private boolean hasErrors = false;

    private String message;

    public boolean hasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
