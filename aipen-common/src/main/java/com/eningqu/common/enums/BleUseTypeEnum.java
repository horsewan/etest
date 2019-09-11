package com.eningqu.common.enums;
/**
 *
 * 描    述：  TODO  蓝牙使用类型枚举类
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2019/1/2 11:02
 *
 */
public enum BleUseTypeEnum {

    CONNECT("1", "连接"),
    BIND("2", "绑定"),
    UNBIND("3", "解绑"),
    ;

    private String code;
    private String val;

    BleUseTypeEnum(String code, String val) {
        this.code = code;
        this.val = val;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
