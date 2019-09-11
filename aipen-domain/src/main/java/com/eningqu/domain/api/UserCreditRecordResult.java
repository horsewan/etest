package com.eningqu.domain.api;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @desc TODO  用户积分实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
public class UserCreditRecordResult{

    private Long id;

    private long vipHongbao;

    private BigDecimal vipCredit;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVipHongbao() {
        return vipHongbao;
    }

    public void setVipHongbao(long vipHongbao) {
        this.vipHongbao = vipHongbao;
    }

    public BigDecimal getVipCredit() {
        return vipCredit;
    }

    public void setVipCredit(BigDecimal vipCredit) {
        this.vipCredit = vipCredit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
