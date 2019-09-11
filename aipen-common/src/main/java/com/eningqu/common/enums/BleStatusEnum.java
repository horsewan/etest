package com.eningqu.common.enums;
/**
 *
 * 描    述：  TODO   蓝牙设备状态  主要用于蓝牙设备状态检测
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/29 19:00
 *
 */
public enum BleStatusEnum {

    VERIFY(1001, "校验错误"),
    UNAVAILABLE(1002, "不可用"),
    UNBIND(1003, "未绑定"),
    BIND_ERROR(1004, "设备绑定不一致"),
    EXCEPTION(500, "服务器异常"),
    ;

    private int key;
    private String val;

    BleStatusEnum(int key, String val) {
        this.key = key;
        this.val = val;
    }

    public int getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }
}
