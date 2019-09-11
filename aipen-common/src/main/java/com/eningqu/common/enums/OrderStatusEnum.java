package com.eningqu.common.enums;
/**
 *
 * @desc TODO  订单状态
 * @author     Yanghuangping
 * @since      2018/7/7 16:58
 * @version    1.0
 *
 **/

public enum  OrderStatusEnum {

    UNPAID("待支付", "1"),
    CANCELLED("已取消", "2"),
    PAID("已支付", "3"),
    SHIPPED("已发货", "4"),
    COMPLETED("线下付", "5");
    /*REVIEWED("已评价", "6"),
    REFUNDING("退款中", "7"),
    REFUNDED("已退款", "8"),
    DELETED("已删除", "9");*/

    private String key;
    private String value;

    OrderStatusEnum(String key, String value) {
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
        for (OrderStatusEnum obj : values()) {
            if(obj.getValue().equalsIgnoreCase(value)){
                return obj.getKey();
            }
        }
        return "";
    }

    public static String getValue(String key){
        for (OrderStatusEnum obj : values()) {
            if(obj.getKey().equalsIgnoreCase(key)){
                return obj.getValue();
            }
        }
        return "";
    }
}
