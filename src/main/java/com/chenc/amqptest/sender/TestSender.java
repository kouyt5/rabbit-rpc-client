package com.chenc.amqptest.sender;


import com.chenc.amqptest.poolreq.AlgoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chenc.amqptest.config.Queues;
import com.chenc.amqptest.config.Shuffle;
import com.chenc.amqptest.pojo.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Slf4j
@Component
public class TestSender {


    private final RabbitTemplate amqpTemplate;
    private final AlgoService algoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("queue2")
    private Queue queue2;

    @Autowired
    @Qualifier("queueTimeOut")
    private Queue timeoutQuene;
    

    @Autowired
    public TestSender(RabbitTemplate amqpTemplate, AlgoService algoService){
        this.amqpTemplate = amqpTemplate;
        this.algoService = algoService;
    }


    // @Scheduled(fixedDelay = 10000, initialDelay = 1000)
    public void sendTimeOut(){
        System.out.println("send a message");
        // Test test = new Test("1", "2");
        Test test = new Test("a", "b");
        String message = (String) amqpTemplate.convertSendAndReceive(timeoutQuene.getName(), test);
        if (message != null){
            System.out.println(message);
        }else {
            System.out.println("wait timeout ...");
        }
    }

    // @Scheduled(fixedDelay = 50*1000, initialDelay = 500)
    public void testAsync() throws ExecutionException, InterruptedException, IOException {
        log.info("test async start");
        Long time = System.currentTimeMillis();
        for(int i=0;i<100;i++){
            algoService.getTest("1", "2");
            Thread.sleep(500);
        }
        log.info("创建100个线程完成"+ (System.currentTimeMillis()-time));
    }
    
    // @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void testRpc() throws IOException{
        Test test = new Test();
        byte[] bytes = objectMapper.writeValueAsBytes(test);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        // messageproperties.setReplyTo("amq.rabbitmq.reply-to.cc");
        messageproperties.setCorrelationId(uuid);

        // 测试混乱id
        Shuffle.setId("1", uuid);
        Message message = new Message(bytes, messageproperties);
        // amqpTemplate.sendAndReceive(message);
        Message receiveMessage = amqpTemplate.sendAndReceive("asrExchange", "rpc", message);
        System.out.println("return1 : "+ objectMapper.readValue(receiveMessage.getBody(), Test.class));
        System.out.println("receive "+receiveMessage.getMessageProperties());
    }
    // @Scheduled(fixedDelay = 500, initialDelay = 1000)
    public void testRpc2() throws IOException{
        Test test = new Test("2", "2");
        byte[] bytes = objectMapper.writeValueAsBytes(test);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        // 测试混乱id
        Shuffle.setId("2", uuid);
        messageproperties.setCorrelationId(uuid);
        // messageproperties.setReplyTo("amq.rabbitmq.reply-to.cc");
        // messageproperties.setCorrelationId(uuid);
        Message message = new Message(bytes, messageproperties);
        // amqpTemplate.sendAndReceive(message);
        Message receiveMessage = amqpTemplate.sendAndReceive("asrExchange", "rpc", message);
        System.out.println("return2 : "+ objectMapper.readValue(receiveMessage.getBody(), Test.class));
        System.out.println("receive "+receiveMessage.getMessageProperties());
    }
}
