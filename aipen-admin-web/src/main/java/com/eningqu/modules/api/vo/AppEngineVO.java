package com.eningqu.modules.api.vo;
/**
 *
 * 描    述：  TODO   
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2019/2/25 16:35
 *
 */
public class AppEngineVO {

    private Long engineId;
    private String engineName;
    /** 是否选中 */
    private Boolean selected;

    public Long getEngineId() {
        return engineId;
    }

    public void setEngineId(Long engineId) {
        this.engineId = engineId;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
