package com.eningqu.common.enums;

public enum OSType {
    Android("0","安卓"),
    IOS("1","ios"),;
    private String typeCode;
    private String des;
    OSType(String typeCode,String des){
        this.typeCode = typeCode;
        this.des = des;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getDes() {
        return des;
    }
}
