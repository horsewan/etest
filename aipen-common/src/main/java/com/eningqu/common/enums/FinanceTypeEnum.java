package com.eningqu.common.enums;

public enum FinanceTypeEnum {
    Normal(0,"normal"),
    Shop(1,"shop"),
    Team(2,"team"),
    City(3,"city");

    private Integer code;

    private String value;

    FinanceTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
