package com.chenc.amqptest.module.asr.service;


import com.chenc.amqptest.base.exception.TimeoutException;
import com.chenc.amqptest.module.asr.pojo.AsrMQReq;
import com.chenc.amqptest.pojo.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Future;

@Slf4j
@Component
public class AlgoService {

    private final RabbitTemplate amqpTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public AlgoService(RabbitTemplate amqpTemplate, @Qualifier("msgpackObjectMapper") ObjectMapper objectMapper){
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
    }

    @Async(value = "algo")
    public Future<Test> getTest(String a, String b) throws InterruptedException, IOException {
        Test test = new Test(a, b);
        byte[] bytes = objectMapper.writeValueAsBytes(test);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        messageproperties.setCorrelationId(uuid);
        Message message = new Message(bytes, messageproperties);
        // amqpTemplate.sendAndReceive(message);
        Message receiveMessage = amqpTemplate.sendAndReceive("asrExchange", "rpc", message, new CorrelationData(uuid));
        System.out.println("return2 : "+ objectMapper.readValue(receiveMessage.getBody(), Test.class));
        System.out.println("receive "+receiveMessage.getMessageProperties());
        return new AsyncResult<Test>(test);
    }

    @Async(value = "algo")
    public Future<Message> getAsrCnMQ(MultipartFile audio, String format) throws InterruptedException, IOException, TimeoutException {
        AsrMQReq asrMQReq = new AsrMQReq(audio.getBytes(), format);
        byte[] bytes = objectMapper.writeValueAsBytes(asrMQReq);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        messageproperties.setCorrelationId(uuid);
        Message message = new Message(bytes, messageproperties);
        log.info("ready to send to rabbitmq cn");
        Message receiveMessage = amqpTemplate.sendAndReceive("asrExchange", "rpc", message, new CorrelationData(uuid));
        return new AsyncResult<Message>(receiveMessage);
    }
    @Async(value = "algo")
    public Future<Message> getAsrEnMQ(MultipartFile audio, String format) throws InterruptedException, IOException, TimeoutException {
        AsrMQReq asrMQReq = new AsrMQReq(audio.getBytes(), format);
        byte[] bytes = objectMapper.writeValueAsBytes(asrMQReq);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        messageproperties.setCorrelationId(uuid);
        Message message = new Message(bytes, messageproperties);
        log.info("ready to send to rabbitmq en");
        Message receiveMessage = amqpTemplate.sendAndReceive("asrExchange", "rpc-en", message);
        return new AsyncResult<Message>(receiveMessage);
    }
}
