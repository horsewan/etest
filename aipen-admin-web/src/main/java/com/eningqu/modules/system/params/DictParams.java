package com.eningqu.modules.system.params;

import com.eningqu.common.kit.ValidationKit;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/25 16:22
 * @version    1.0
 *
 **/

public class DictParams {

    @NotNull(message = "ID不能为空")
    private Long id;
    @NotBlank(message = "字典组名不能为空", groups = {ValidationKit.GroupValid.class})
    private String groupDesc;
    @NotBlank(message = "字典组码不能为空", groups = {ValidationKit.GroupValid.class})
    private String groupCode;
    @NotBlank(message = "字典名不能为空", groups = {ValidationKit.EditValid.class})
    private String labelName;
    @NotBlank(message = "字典值不能为空", groups = {ValidationKit.EditValid.class})
    private String valueCode;
    @NotNull(message = "PID不能为空", groups = {ValidationKit.EditValid.class})
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
