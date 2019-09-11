package com.eningqu.domain.api;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

import java.io.Serializable;

/**
 *
 * @desc TODO  定时任务
 * @author     Yanghuangping
 * @since      2018/8/16 11:28
 * @version    1.0
 *
 **/
@TableName("sys_cron_triggers")
public class TaskJob extends DataEntity<TaskJob> {

    /**
     * 任务描述
     */
    @TableField("triggers_name")
    private String triggersName;

    /**
     * 定时任务 执行类名
     */
    @TableField("class_name")
    private String className;

    /**
     * cron表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * 任务状态
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remarks")
    private Long remarks;


    @Override
    protected Serializable pkVal() {
        return id;
    }

    public String getTriggersName() {
        return triggersName;
    }

    public void setTriggersName(String triggersName) {
        this.triggersName = triggersName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getRemarks() {
        return remarks;
    }

    public void setRemarks(Long remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
