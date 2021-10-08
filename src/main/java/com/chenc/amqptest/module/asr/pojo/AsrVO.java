package com.chenc.amqptest.module.asr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AsrVO {
    private String sentence;
    private int other;
}
