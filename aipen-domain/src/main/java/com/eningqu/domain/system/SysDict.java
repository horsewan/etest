package com.eningqu.domain.system;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
@TableName("sys_dict")
public class SysDict extends DataEntity<SysDict> {

    /**
     * 标签名
     */
    @TableField(value = "label_name")
    private String labelName;
    /**
     * 数据值
     */
    @TableField(value = "value_code")
    private String valueCode;
    /**
     * 字典组编码
     */
    @TableField(value = "group_code")
    private String groupCode;
    /**
     * 字典组描述
     */
    @TableField(value = "group_desc")
    private String groupDesc;
    /**
     * 排序（升序）
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 父标签ID
     */
    @TableField(value = "pid")
    private Long pid;
    /**
     * 备注信息
     */
    @TableField(value = "remarks")
    private String remarks;
    /**
     * 删除标记
     */
    @TableField(value = "del_status")
    private String delStatus;


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

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

}
