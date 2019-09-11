package com.eningqu.modules.system.vo;
/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @since      2018/5/30 11:22
 * @version    1.0
 *
 **/

public class MenuSelectVO {

    private Long id;
    private String title;
    private String menuType;
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
