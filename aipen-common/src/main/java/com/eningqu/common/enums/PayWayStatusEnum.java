package com.eningqu.common.enums;
/**
 *
 * @desc TODO  支付方式
 * @author     Yanghuangping
 * @since      2018/7/14 9:53
 * @version    1.0
 *
 **/

public enum PayWayStatusEnum {

    WEI_XIN_PAY("微信支付", "1"),
    ALI_PAY("支付宝", "2"),
    UNION_PAY("云闪付", "3"),
    HONGBAO_PAY("红包兑换", "4");

    private String key;
    private String value;

    PayWayStatusEnum(String key, String value) {
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
        for (PayWayStatusEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (PayWayStatusEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }
}
