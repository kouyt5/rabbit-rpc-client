package com.chenc.amqptest.base;

import lombok.Getter;
import lombok.Setter;

/**
 *消息队列的数据封装
 * @param <T> 封装的数据
 */
@Getter
@Setter
public class BaseMQResponseDTO<T>{

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
    private BaseMQResponseDTO(){
        this.message = "未知";
        this.status = 200;
    }

    /**
     * 构造者模式构造{@link BaseMQResponseDTO <T>} 禁止 new
     * @param <T> 算法参数
     */
    public final static class Builder<T>{
        BaseMQResponseDTO<T> baseResponseVO = new BaseMQResponseDTO<T>();

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
        public BaseMQResponseDTO<T> build(){
            return this.baseResponseVO;
        }
    }
}
