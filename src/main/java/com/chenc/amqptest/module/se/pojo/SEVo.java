package com.chenc.amqptest.module.se.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SEVo {
    private String url;
    private String duration;
    private byte[] enhance;
}
