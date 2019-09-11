package com.eningqu.common.enums;

/**
 *
 * 描    述：  TODO  密保类型
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/29 14:38
 *
 */
public enum  SecretTypeEnum {

    PHONE("1", "手机号码找回"),
    SECRET("2", "密保问题找回")
    ;

    private String key;
    private String val;

    SecretTypeEnum(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    public static SecretTypeEnum getType(String key){
        for (SecretTypeEnum obj : values()) {
            if(key != null && obj.getKey().equals(key)){
                return obj;
            }
        }
        return null;
    }
}
