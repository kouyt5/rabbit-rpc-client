package com.chenc.amqptest.base.status;

/**
 * http状态码类
 * @author kouyt5
 * @since 21.11.24
 */
public enum BaseStatus {
    SUCCESS(200, "success"),
    TIMEOUT(400, "内部服务调用超时"),
    INTERNALREEOR(500, "服务内部未知错误");

    private final int value;
    private final String reason;

    BaseStatus(int value, String resason){
        this.value = value;
        this.reason = resason;
    };
    public int value(){return this.value;}
    public String reason(){return this.reason;}
}
