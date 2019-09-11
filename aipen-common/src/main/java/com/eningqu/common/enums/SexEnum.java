package com.eningqu.common.enums;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/25 17:10
 * @version    1.0
 *
 **/

public enum SexEnum {

    MAN("男", "M"),
    WOMAN("女", "F");

    private String key;
    private String value;

    SexEnum(String key, String value) {
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
        for (SexEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (SexEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }
}
