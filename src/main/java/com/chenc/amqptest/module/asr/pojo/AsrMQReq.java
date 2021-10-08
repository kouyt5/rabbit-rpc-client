package com.chenc.amqptest.module.asr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AsrMQReq {
    private byte[] audio;
    private String format;
}
