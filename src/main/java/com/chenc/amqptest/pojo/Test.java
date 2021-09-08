package com.chenc.amqptest.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Test implements Serializable {
    private static final long serialVersionUID = 1L;
    private String a;
    private String b;

    public Test(String a, String b){
        this.a = a;
        this.b = b;
    }
    public Test(){};
}
