package com.eningqu.domain.api;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO 结算管理 - 商家列表
 * @date 2019/8/22 14:47
 **/
public class FinanceListResult implements Serializable {

    /**
     * 用户ID
     */
    private Integer uId;
    /**
     * 手机号
     */
    private String mobile;

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
     * 商家总成交量
     */
    private Integer shopNums;
    /**
     * 总成交金额
     */
    private BigDecimal amountRealpay;
    /**
     * 总省钱金额
     */
    private BigDecimal money;
    /**
     * 常去商家排名
     */
    private String shopSort;

    /**
     * 总分成金额
     */
    private BigDecimal dividedMoney;

    /**
     * 总成交用户
     */
    private Integer sumUser;

    /**
     * 用户提成
     */
    private BigDecimal userPercentage;

    /**
     * 商家提成
     */
    private BigDecimal shopPercentage;

    /**
     * 活跃用户
     */
    private Integer activeUser;

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getShopSort() {
        return shopSort;
    }

    public void setShopSort(String shopSort) {
        this.shopSort = shopSort;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getDividedMoney() {
        return dividedMoney;
    }

    public void setDividedMoney(BigDecimal dividedMoney) {
        this.dividedMoney = dividedMoney;
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

    public BigDecimal getUserPercentage() {
        return userPercentage;
    }

    public void setUserPercentage(BigDecimal userPercentage) {
        this.userPercentage = userPercentage;
    }

    public BigDecimal getShopPercentage() {
        return shopPercentage;
    }

    public void setShopPercentage(BigDecimal shopPercentage) {
        this.shopPercentage = shopPercentage;
    }

    public Integer getShopNums() {
        return shopNums;
    }

    public void setShopNums(Integer shopNums) {
        this.shopNums = shopNums;
    }

    public Integer getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(Integer activeUser) {
        this.activeUser = activeUser;
    }
}
