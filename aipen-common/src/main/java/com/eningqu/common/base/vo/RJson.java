package com.eningqu.common.base.vo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @desc TODO  JSON数据返回模型
 * @author     Yanghuangping
 * @date       2018/4/18 10:10
 * @version    1.0
 *
 **/
public class RJson extends HashMap<String, Object> {

    /**成功状态码*/
    public static final int OK = 1;
    /**失败状态码*/
    public static final int FAIL = 2;
    /**未授权状态码*/
    public static final int UNAUTHORIZED = 0;
    /**未登录*/
    public static final int UN_LOGIN = -1;
    /**异地登录*/
    public static final int CAS_LOGIN = 3;


    private final static String CODE = "code";
    private final static String MSG = "msg";
    private final static String SUCCESS = "success";
    private final static String DATA = "data";

    public final static String DEFAULT_SUCCESS_TIPS = "请求成功";
    public final static String DEFAULT_FAIL_TIPS = "请求失败";
    public final static String DEFAULT_UNAUTHORIZED_TIPS = "未授权的请求";
    public final static String DEFAULT_UNLOGIN_TIPS = "用户未登录";
    public final static String DEFAULT_CATLOGIN_TIPS = "该账号已在其他机器登录，您已被强制退出";

    private RJson(){}

    private static RJson bulid(){
        return new RJson();
    }

    public static RJson ok(){
        return ok(DEFAULT_SUCCESS_TIPS);
    }

    public static RJson ok(String msg){
        return ok(OK, msg);
    }

    public static RJson ok(int code, String msg){
        return RJson.bulid().setCode(code).setSuccess(true).setMsg(msg);
    }

    public static RJson error(){
        return error(DEFAULT_FAIL_TIPS);
    }

    public static RJson error(String msg){
        return error(FAIL, msg);
    }

    public static RJson error(int code, String msg){
        return RJson.bulid().setCode(code).setSuccess(false).setMsg(msg);
    }

    public static RJson unAuth(){
        return RJson.bulid().setCode(UNAUTHORIZED).setSuccess(false).setMsg(DEFAULT_UNAUTHORIZED_TIPS);
    }

    public static RJson unLogin(){
        return RJson.bulid().setCode(UN_LOGIN).setSuccess(false).setMsg(DEFAULT_UNLOGIN_TIPS);
    }

    public RJson setSuccess(boolean flag) {
        super.put(SUCCESS, flag);
        return this;
    }

    public RJson setCode(int code) {
        super.put(CODE, code);
        return this;
    }

    public RJson setMsg(String msg) {
        super.put(MSG, msg);
        return this;
    }

    public RJson setData(Object object){
        super.put(DATA, object);
        return this;
    }

    @Override
    public RJson put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public RJson putAlls(Map<? extends String, ?> map) {
        super.putAll(map);
        return this;
    }
}
