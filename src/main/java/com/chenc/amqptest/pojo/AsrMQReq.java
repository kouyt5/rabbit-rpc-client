package com.chenc.amqptest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AsrMQReq {
    private byte[] audio;
    private String format;
}
