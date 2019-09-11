package com.eningqu.modules.system.vo;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/25 10:40
 * @version    1.0
 *
 **/

public class UserRolesVO {
    private String name;
    private String value;
    private boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
