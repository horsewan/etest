package com.eningqu.modules.system.vo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @desc TODO  菜单VO
 * @author     Yanghuangping
 * @since      2018/5/16 11:39
 * @version    1.0
 *
 **/

public class MenuVo implements Serializable{

    private static final long serialVersionUID = -1L;

    private Long id;
    private String title;
    private String icon;
    private String href;
    private Long pid;
    private String target;
    private List<MenuVo> children;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
