package com.toiletCat.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易状态
 */
public enum TradeStatus {

    INIT(0, "初始化"),
    SUCCESS(10, "成功"),
    FAIL(20, "失败"),
    PROCESSING(30,"处理中"),
    CANCEL(40, "订单取消");

    /**
     * 状态
     **/
    private int status;

    /**
     * 描述
     **/
    private String desc;

    private static Map<Integer,TradeStatus> valueMap = new HashMap<>();

    TradeStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private static void initValueMap() {
        if(valueMap.isEmpty()) {
            TradeStatus[] array = TradeStatus.values();
            for(TradeStatus tradeStatus : array) {
                valueMap.put(tradeStatus.getStatus(), tradeStatus);
            }
        }
    }

    public static Map<Integer,TradeStatus> getValueMap() {
        initValueMap();
        return valueMap;
    }

    public static TradeStatus getValueMapByKey(Integer key) {
        initValueMap();
        return valueMap.get(key);
    }
}
