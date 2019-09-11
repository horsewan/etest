package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO  api控制实体类
 * @author     Yanghuangping
 * @since      2018/9/10 11:06
 * @version    1.0
 *
 **/

@TableName("sys_api")
public class SysAPIInfo extends BaseEntity<SysAPIInfo> {

    //该状态为N则全部不可用，Y则可以正常使用。
    @TableField("sys_api")
    String sysApi;

    @TableField("sys_user_reg")
    String sysUserReg;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public String getSysApi() {
        return sysApi;
    }

    public void setSysApi(String sysApi) {
        this.sysApi = sysApi;
    }

    public String getSysUserReg() {
        return sysUserReg;
    }

    public void setSysUserReg(String sysUserReg) {
        this.sysUserReg = sysUserReg;
    }
}
