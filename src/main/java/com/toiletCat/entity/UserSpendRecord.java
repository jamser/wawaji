package com.toiletCat.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户消费记录
 */
public class UserSpendRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4954366162493302680L;

    /**
     * 用户编号
     */
    private String userNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 消费游戏币数
     */
    private Integer coin;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 交易类型描述
     */
    private String tradeTypeDesc;

    /**
     * 交易状态
     */
    private Integer tradeStatus;

    /**
     * 交易日期
     */
    private Integer tradeDate;

    /**
     * 交易时间
     */
    private String tradeTime;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeTypeDesc() {
        return tradeTypeDesc;
    }

    public void setTradeTypeDesc(String tradeTypeDesc) {
        this.tradeTypeDesc = tradeTypeDesc;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Integer getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Integer tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
}
