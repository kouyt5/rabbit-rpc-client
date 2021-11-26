package com.chenc.amqptest.base.exception;

import com.chenc.amqptest.base.status.BaseStatus;

/**
 * 服务调用超时时候抛出异常
 * @author kouyt5
 * @since 21.11.24
 */
public class TimeoutException extends BaseException{

    public TimeoutException(String reason) {
        super(BaseStatus.TIMEOUT.value(), reason);
    }
    public TimeoutException(){
        super(BaseStatus.TIMEOUT.value(), BaseStatus.TIMEOUT.reason());
    }
    
}
