package com.zhuy.entity.enums;

public enum OrderType {
    APPLY("申购"),
    REDEEM("赎回");

    private String type;

    OrderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
