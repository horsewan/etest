package com.eningqu.pay.common.enums;

/**
 *
 * @desc TODO  app类型 是andriod还是ios
 * @author     Yanghuangping
 * @since      2018/7/16 16:25
 * @version    1.0
 *
 **/

public enum  AppTypeEnum {

    ANDRIOD("1"),
    IOS("2");

    String value;

    AppTypeEnum(String value) {
        this.value = value;
    }

    public static AppTypeEnum getValue(String value){
        for (AppTypeEnum obj : values()) {
            if(obj.value.equalsIgnoreCase(value)){
                return obj;
            }
        }
        return null;
    }
}
