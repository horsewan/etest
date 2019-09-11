package com.eningqu.common.enums;
/**
 *
 * 描    述：  TODO   
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2019/2/28 20:06
 *
 */
public enum EngineStatusEnum {

    OFF("0", "关闭"),
    ON("1", "开启")
    ;

    private String code;
    private String desc;

    EngineStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
