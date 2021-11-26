package com.chenc.amqptest.componet;

import com.chenc.amqptest.base.BaseResponse;
import com.chenc.amqptest.base.exception.BaseException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> responseBaseExceptionHandler(BaseException e){

        log.error(e.toString(), e);
        return new BaseResponse.Builder<Object>()
                    .setData(null)
                    .setMessage(e.getReason())
                    .setStatus(e.getValue())
                    .build();
    }
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> responseExceptionHandler(Exception e){

        log.error(e.toString(), e);
        return new BaseResponse.Builder<Object>()
                    .setData(null)
                    .setMessage("程序内部未知错误")
                    .setStatus(500)
                    .build();
    }
}
