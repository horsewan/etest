package com.eningqu.common.enums;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/25 17:28
 * @version    1.0
 *
 **/

public enum SysUserTypeEnum {

    SUPER("超级管理员", "S"),
    ORDINARY("普通管理员", "O");

    private String key;
    private String value;

    SysUserTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getKey(String value){
        for (SysUserTypeEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (SysUserTypeEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }
}
