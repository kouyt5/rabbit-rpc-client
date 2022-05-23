package com.chenc.amqptest.componet;

import com.chenc.amqptest.base.BaseResponse;
import com.chenc.amqptest.base.exception.BaseException;

import com.chenc.amqptest.base.exception.TimeoutException;
import com.chenc.amqptest.base.status.BaseStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(TimeoutException.class)
    public BaseResponse<Object> responseTimeoutExceptionHandler(BaseException e){

        log.error(e.toString(), e);
        return new BaseResponse.Builder<>()
                .setData(null)
                .setMessage(e.getReason())
                .setStatus(e.getValue())
                .build();
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> responseBaseExceptionHandler(BaseException e){

        log.error(e.toString(), e);
        return new BaseResponse.Builder<>()
                    .setData(null)
                    .setMessage(e.getReason())
                    .setStatus(e.getValue())
                    .build();
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<Object> responseMissingExceptionHandler(MissingServletRequestParameterException e){

        log.error(e.toString(), e);
        String ignoreParam = e.getParameterName();
        String ignoreType = e.getParameterType();
        String message = "参数缺失: name=" + ignoreParam+" type="+ ignoreType;
        return new BaseResponse.Builder<>()
                .setData(null)
                .setMessage(message)
                .setStatus(BaseStatus.INTERNALREEOR.value())
                .build();
    }
    @ExceptionHandler(MissingServletRequestPartException.class)
    public BaseResponse<Object> responseMissingPartExceptionHandler(MissingServletRequestPartException e){

        log.error(e.toString(), e);
        String ignoreParam = e.getRequestPartName();
        String message = "参数缺失: name=" + ignoreParam;
        return new BaseResponse.Builder<>()
                .setData(null)
                .setMessage(message)
                .setStatus(BaseStatus.INTERNALREEOR.value())
                .build();
    }
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> responseExceptionHandler(Exception e){

        log.error(e.toString(), e);
        return new BaseResponse.Builder<>()
                    .setData(null)
                    .setMessage("程序内部未知错误")
                    .setStatus(500)
                    .build();
    }
}
