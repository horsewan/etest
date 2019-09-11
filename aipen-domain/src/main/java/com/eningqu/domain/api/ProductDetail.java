package com.eningqu.domain.api;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.eningqu.common.base.entity.DataEntity;

/**
 *
 * @desc TODO  产品详情实体类
 * @author     Yanghuangping
 * @since      2018/6/1 15:10
 * @version    1.0
 *
 **/
@TableName("dy_product_detail")
public class ProductDetail extends DataEntity<ProductDetail> {

    @TableField("pid")
    private Long pid;
    @TableField("show_pic_url")
    private String showPicUrl;
    @TableField("del_status")
    private String delStatus;
    @TableField("remarks")
    private String remarks;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getShowPicUrl() {
        return showPicUrl;
    }

    public void setShowPicUrl(String showPicUrl) {
        this.showPicUrl = showPicUrl;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
        "pid=" + pid +
        ", showPicUrl=" + showPicUrl +
        ", delStatus=" + delStatus +
        ", remarks=" + remarks +
        "}";
    }
}
