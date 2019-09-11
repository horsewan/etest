package com.eningqu.pay.common.enums;
/**
 *
 * @desc TODO  支付类型
 * @author     Yanghuangping
 * @since      2018/7/30 11:50
 * @version    1.0
 *
 **/

public enum PayTypeEnum {

    WEIXIN("1", "微信支付"),
    ALI("2", "支付宝支付");

    private String key;
    private String value;

    PayTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
