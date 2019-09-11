package com.eningqu.common.enums;
/**
 *
 * @desc TODO  导入日志类型枚举
 * @author     Yanghuangping
 * @since      2018/11/22 10:48
 * @version    1.0
 *
 **/

public enum SysImportTypeEnum {

    IMEI("1", "导入IMEI"),
    MAC("2", "导入MAC地址")

    ;

    private String code;
    private String msg;

    SysImportTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
