package com.eningqu.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("sys_menu")
public class SysMenu extends DataEntity<SysMenu> {

    @TableField(value = "pid")
    private Long pid;

    @TableField(value = "title")
    private String title;

    @TableField(value = "menu_type")
    private String menuType;

    @TableField(value = "href")
    private String href;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "perm")
    private String perm;

    @TableField(value = "del_status")
    private String delStatus;

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

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "pid=" + pid +
                ", title='" + title + '\'' +
                ", menuType='" + menuType + '\'' +
                ", href='" + href + '\'' +
                ", icon='" + icon + '\'' +
                ", sort=" + sort +
                ", perm='" + perm + '\'' +
                ", delStatus='" + delStatus + '\'' +
                ", createId=" + createId +
                ", createTime=" + createTime +
                ", updateId=" + updateId +
                ", updateTime=" + updateTime +
                ", id=" + id +
                '}';
    }
}
