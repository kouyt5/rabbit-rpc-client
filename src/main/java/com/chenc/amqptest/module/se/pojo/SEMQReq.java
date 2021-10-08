package com.chenc.amqptest.module.se.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SEMQReq {
    public byte[] audio;
    public String format;
}
