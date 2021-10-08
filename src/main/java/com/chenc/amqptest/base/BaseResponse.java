package com.chenc.amqptest.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private int status;
    private String message;
    private T data;

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * 禁止直接构造
     */
    private BaseResponse(){
        this.message = "未知";
        this.status = 200;
    }

    /**
     * 构造者模式构造{@link BaseResponse <T>} 禁止 new
     * @param <T> 算法参数
     */
    public final static class Builder<T>{
        BaseResponse<T> baseResponseVO = new BaseResponse<T>();

        public Builder<T> setMessage(String message){
            this.baseResponseVO.setMessage(message);
            return this;
        }
        public Builder<T> setStatus(int status){
            this.baseResponseVO.setStatus(status);
            return this;
        }
        public Builder<T> setData(T data){
            this.baseResponseVO.setData(data);
            return this;
        }
        public BaseResponse<T> build(){
            return this.baseResponseVO;
        }
    }
}
