package com.zhuy.entity.enums;

public enum OrderStatus {
    INIT("初始化"),
    PROCESS("处理中"),
    SUCCESS("成功"),
    FAIL("失败");
    private String status;

    public String getStatus() {
        return status;
    }

    OrderStatus(String status) {

        this.status = status;
    }
}
