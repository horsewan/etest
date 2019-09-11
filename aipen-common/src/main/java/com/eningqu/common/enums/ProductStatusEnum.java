package com.eningqu.common.enums;
/**
 *
 * @desc TODO  产品状态
 * @author     Yanghuangping
 * @since      2018/7/7 16:35
 * @version    1.0
 *
 **/

public enum ProductStatusEnum {

    ON_SHELVE("未上架", "1"),
    SELLING("售卖中", "2"),
    OFF_SHELVE("已下架", "3");

    private String key;
    private String value;

    ProductStatusEnum(String key, String value) {
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
        for (ProductStatusEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (ProductStatusEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }
}
