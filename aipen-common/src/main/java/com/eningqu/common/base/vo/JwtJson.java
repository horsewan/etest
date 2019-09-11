package com.eningqu.common.base.vo;


import io.jsonwebtoken.Claims;

/**
 *
 * @desc TODO  JWT验证结果集
 * @author     Yanghuangping
 * @date       2018/4/28 10:30
 * @version    1.0
 *
 **/
public class JwtJson{

    private int code;
    private boolean success;
    private String msg;
    private Claims claims;

    public int getCode() {
        return code;
    }

    public JwtJson setCode(int code) {
        this.code = code;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public JwtJson setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JwtJson setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Claims getClaims() {
        return claims;
    }

    public JwtJson setClaims(Claims claims) {
        this.claims = claims;
        return this;
    }

    private JwtJson(){}

    private static JwtJson bulid(){
        return new JwtJson();
    }

    public static JwtJson ok(){
        return bulid().setSuccess(true);
    }

    public static JwtJson error(){
        return bulid().setSuccess(false);
    }

    @Override
    public String toString() {
        return "JwtJson{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", claims=" + claims +
                '}';
    }
}
