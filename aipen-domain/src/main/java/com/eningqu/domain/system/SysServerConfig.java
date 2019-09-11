package com.eningqu.domain.system;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.BaseEntity;

@TableName("sys_server_config")
public class SysServerConfig extends BaseEntity<SysServerConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2181171969108916030L;

	/**
     * 主机记录：IP或域名
     */
	@TableField(value="domain")
    private String domain;
    /**
     * 版本号 只能为数值
     */
	@TableField(value="version")
    private Integer version;
    /**
     * 权重值 数值越小权重越高 针对主机
     */
	@TableField(value="priority")
    private Integer priority;
    /**
     * 是否最优版本 1 是 2 否
     */
	@TableField(value="avaliable")
    private Integer avaliable;
    /**
     * 区域编码
     */
	@TableField(value="area")
    private String area;
    /**
     * 服务器类型 1 语音服务器 2 鉴权服务器
     */
	@TableField(value="type")
    private Integer type;
    /**
     * 服务器状态 1 正常 2 已停止
     */
	@TableField(value="status")
    private Integer status;


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(Integer avaliable) {
        this.avaliable = avaliable;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
