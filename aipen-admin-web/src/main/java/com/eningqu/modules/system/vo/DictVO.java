package com.eningqu.modules.system.vo;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/19 15:13
 * @version    1.0
 *
 **/

public class DictVO {

    private Long id;
    private String labelName;
    private String valueCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }
}
