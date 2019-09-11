package com.eningqu.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @desc TODO  系统角色实体类
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 16:26
 **/
@TableName("sys_role")
public class SysRole extends DataEntity<SysRole> {

    @TableField(value = "role_name")
    private String roleName;

    @TableField(value = "role_code")
    private String roleCode;

    @TableField(value = "del_status")
    private String delStatus;

    @TableField(value = "remarks")
    private String remarks;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /***菜单列表*/
    @TableField(exist = false)
    private List<SysMenu> sysMenus;

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<SysMenu> sysMenus) {
        this.sysMenus = sysMenus;
    }

    public List<String> getPermList() {
        List<String> perms = Lists.newArrayList();
        if (getSysMenus() != null && getSysMenus().size() > 0) {
            for (SysMenu sysMenu : getSysMenus()) {
                perms.add(sysMenu.getPerm());
            }
        }
        return perms;
    }
}
