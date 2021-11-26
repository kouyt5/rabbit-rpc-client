package com.chenc.amqptest.base.exception;

import com.chenc.amqptest.base.status.BaseStatus;
import com.rabbitmq.client.AMQP.Confirm.Select;

import lombok.val;

/**
 * Base异常，程序可控异常应该继承此类型
 * @author kouyt5
 * @since 21.11.24
 */
public class BaseException extends Exception{
    private final int status;
    private final String reason;

    BaseException(int status, String reason){
        this.status = status;
        this.reason = reason;
    }

    public int getValue(){
        return this.status;
    }
    public String getReason(){
        return this.reason;
    }
}
