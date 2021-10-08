package com.chenc.amqptest.mqautocheck.receiver;

import java.io.IOException;
import java.util.Map;

import com.chenc.amqptest.pojo.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;


@Component
public class TestReceiver {
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = {"timeoutQuene"})
    public String receive(Message bytes) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("received "+ bytes);
        return "received !!!";
    }
    // @RabbitListener(queues = {"cc1"})
    public Test receivecc2(Message bytes,@Headers Map<String,Object> headers)throws InterruptedException, JsonParseException, JsonMappingException, IOException {
        Thread.sleep(1000);
        System.out.println(bytes.getMessageProperties());
        byte[] x = bytes.getBody();
        Test test = objectMapper.readValue(x, Test.class);
        System.out.println("received "+ test);
        return test;
    }
    
}
