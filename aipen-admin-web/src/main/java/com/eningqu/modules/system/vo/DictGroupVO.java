package com.eningqu.modules.system.vo;

import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/19 15:13
 * @version    1.0
 *
 **/

public class DictGroupVO {

    private Long id;
    private String GroupCode;
    private String GroupDesc;

    private List<DictVO> dicts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public String getGroupDesc() {
        return GroupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        GroupDesc = groupDesc;
    }

    public List<DictVO> getDicts() {
        return dicts;
    }

    public void setDicts(List<DictVO> dicts) {
        this.dicts = dicts;
    }
}
