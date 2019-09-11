package com.eningqu.common.enums;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/25 17:10
 * @version    1.0
 *
 **/

public enum SysMenuTypeEnum {

    DIRECTORY("目录", "0"),
    PAGE("页面", "1"),
    BUTTON("按钮", "2");

    private String key;
    private String value;

    SysMenuTypeEnum(String key, String value) {
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
        for (SysMenuTypeEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (SysMenuTypeEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }
}
