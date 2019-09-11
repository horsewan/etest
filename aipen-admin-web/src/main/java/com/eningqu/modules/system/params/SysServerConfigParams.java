package com.eningqu.modules.system.params;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SysServerConfigParams {
	
    @NotNull(message = "主机记录不能为空")
    private String domain;
    @NotNull(message = "版本号不能为空")
    @Min(value=1,message="版本号只能为1-99的整数值")  
    @Max(value=99,message="版本号只能为1-99的整数值")  
    private Integer version;
    @NotNull(message = "服务器权重值不能为空")
    @Min(value=1,message="服务器权重值只能为1-99的整数值")  
    @Max(value=99,message="服务器权重值只能为1-99的整数值")
    private Integer priority;
    @NotNull(message = "是否最优版本值不能为空")
    @Min(value=1,message="是否最优版本值只能为1或2的整数值")  
    @Max(value=2,message="是否最优版本值只能为1或2的整数值")
    private Integer avaliable;
    @NotNull(message="语音类型不能为空")
    @Min(value=1,message="语音类型只能为1,2,3的整数值")
    @Max(value=3,message="语音类型只能为1,2,3的整数值")
    private Integer type;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
    
    

}
