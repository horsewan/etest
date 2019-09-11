package com.eningqu.domain.api;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO  商家
 * @date 2019/8/22 14:47
 **/
public class BusinessFinanceResult implements Serializable {

    /**
     * 用户ID
     */
    private Integer uId;
    /**
     * 个代编号
     */
    private String singleNo;

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户总成交量
     */
    private Integer nums;

    /**
     * 总成交金额
     */
    private BigDecimal amountRealpay;

    /**
     * 总成交用户
     */
    private Integer sumUser;


    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public BigDecimal getAmountRealpay() {
        return amountRealpay;
    }

    public void setAmountRealpay(BigDecimal amountRealpay) {
        this.amountRealpay = amountRealpay;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSingleNo() {
        return singleNo;
    }

    public void setSingleNo(String singleNo) {
        this.singleNo = singleNo;
    }

    public Integer getSumUser() {
        return sumUser;
    }

    public void setSumUser(Integer sumUser) {
        this.sumUser = sumUser;
    }
}
