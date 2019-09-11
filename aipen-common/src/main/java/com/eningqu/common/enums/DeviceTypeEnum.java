package com.eningqu.common.enums;
/**
 *
 * @desc TODO  设备类型
 * @author     Yanghuangping
 * @since      2018/11/20 16:07
 * @version    1.0
 *
 **/

public enum DeviceTypeEnum {


    TRANSLATOR("1", "翻译机"),
    BLUETOOTH("2", "蓝牙设备"),
    BLE_KEYBOARD("3", "蓝牙键盘")
    ;

    private String type;
    private String desc;

    DeviceTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
