package com.eningqu.modules.system.vo;

import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/24 11:46
 * @version    1.0
 *
 **/

public class RoleMenusVO {
    private Long id;
    private Long pid;
    private String title;
    private boolean checked;

    private List<RoleMenusVO> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<RoleMenusVO> getList() {
        return list;
    }

    public void setList(List<RoleMenusVO> list) {
        this.list = list;
    }
}
