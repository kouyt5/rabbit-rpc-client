package com.chenc.amqptest.module.se.service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Future;

import com.chenc.amqptest.base.BaseMQResponseDTO;
import com.chenc.amqptest.config.RabbitMQConstant;
import com.chenc.amqptest.module.se.pojo.SEMQReq;
import com.chenc.amqptest.module.se.pojo.SEMQResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;


/**
 * 语音增强算法
 * @author chenc
 * @since 2021/10/4
 */
@Slf4j
@Service
public class SEService {
    private final ObjectMapper objectMapper;
    private final AmqpTemplate amqpTemplate;
    private final RabbitMQConstant rabbitMQConstant;

    public SEService(@Qualifier("msgpackObjectMapper") ObjectMapper objectMapper,
                     AmqpTemplate amqpTemplate, RabbitMQConstant rabbitMQConstant){
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
        this.rabbitMQConstant = rabbitMQConstant;
    }

    /**
     * 获取到语音增强算法的结果
     * @param audio 音频文件二进制数据
     * @param format 指明音频格式
     * @return @see {@link Future}
     * @throws InterruptedException
     * @throws IOException
     */
    @Async(value = "algo")
    public Future<SEMQResp> getSE(byte[] audio, String format) throws InterruptedException, IOException {
        SEMQReq asrMQReq = new SEMQReq(audio, format);
        byte[] bytes = objectMapper.writeValueAsBytes(asrMQReq);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        messageproperties.setCorrelationId(uuid);
        Message message = new Message(bytes, messageproperties);
        log.info("ready to send to rabbitmq， 算法为语音增强");
        Message receiveMessage = amqpTemplate.sendAndReceive(rabbitMQConstant.seExchange, rabbitMQConstant.bindingKeySe, message);
        //TODO  NPE异常处理
        BaseMQResponseDTO<SEMQResp> seMQResp = objectMapper.readValue(receiveMessage.getBody(), 
                                                new TypeReference<BaseMQResponseDTO<SEMQResp>>(){});
        log.info("return : "+ seMQResp.toString());
        // 检查算法端是否异常，根据状态码做相应的异常抛出
        if (seMQResp.getStatus()!=200) throw new IOException();
        return new AsyncResult<SEMQResp>(seMQResp.getData());
    }
}
