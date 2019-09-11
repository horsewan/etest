package com.eningqu.common.enums;
/**
 *
 * @desc TODO  第三方授权登录用户类型
 * @author     Yanghuangping
 * @since      2018/7/16 13:54
 * @version    1.0
 *
 **/

public enum OauthUserTypeEnum {

    WEI_XIN("1", "微信"),
    QQ("2", "QQ"),
    FACEBOOK("3", "facebook"),
    TWITTER("4", "twitter"),
    WEI_BO("5", "新浪微博");

    private String key;
    private String value;

    OauthUserTypeEnum(String key, String value) {
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
        for (OauthUserTypeEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (OauthUserTypeEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }

}
