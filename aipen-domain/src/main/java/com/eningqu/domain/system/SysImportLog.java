package com.eningqu.domain.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @desc TODO  导入实体类
 * @author     Yanghuangping
 * @since      2018/11/22 10:02
 * @version    1.0
 *
 **/

@TableName("sys_import_log")
public class SysImportLog extends BaseEntity<SysImportLog>{

    @TableField("import_type")
    private String importType;
    @TableField("import_msg")
    private String importMsg;
    @TableField("op_name")
    private String opName;
    @TableField(value = "op_time")
    private Date opTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getImportMsg() {
        return importMsg;
    }

    public void setImportMsg(String importMsg) {
        this.importMsg = importMsg;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }
}
