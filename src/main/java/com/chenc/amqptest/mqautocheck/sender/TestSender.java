package com.chenc.amqptest.mqautocheck.sender;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chenc.amqptest.module.asr.service.AlgoService;
import com.chenc.amqptest.pojo.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


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
    public void sendTimeOut() throws JsonProcessingException{
        System.out.println("send a message");
        // Test test = new Test("1", "2");
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        // messageproperties.setReplyTo("amq.rabbitmq.reply-to.cc");
        // messageproperties.setCorrelationId(uuid);
        Test test = new Test("a", "b");
        byte[] bytes = objectMapper.writeValueAsBytes(test);
        Message message = new Message(bytes, messageproperties);
        Message messages = amqpTemplate.sendAndReceive(timeoutQuene.getName(), message, new CorrelationData(uuid));
        if (messages != null){
            System.out.println(messages);
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

        Message message = new Message(bytes, messageproperties);
        // amqpTemplate.sendAndReceive(message);
        Message receiveMessage = amqpTemplate.sendAndReceive("asrExchange", "rpc", message);
        System.out.println("return1 : "+ objectMapper.readValue(receiveMessage.getBody(), Test.class));
        System.out.println("receive "+receiveMessage.getMessageProperties());
    }

    /**
     * 返回相同的消息
     * @throws IOException
     */
    // @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void testRpc2() throws IOException{
        Test test = new Test("2", "2");
        byte[] bytes = objectMapper.writeValueAsBytes(test);
        MessageProperties messageproperties = new MessageProperties();
        String uuid = UUID.randomUUID().toString();
        messageproperties.setCorrelationId(uuid);
        // messageproperties.setReplyTo("amq.rabbitmq.reply-to.cc");
        // messageproperties.setCorrelationId(uuid);
        Message message = new Message(bytes, messageproperties);
        // amqpTemplate.sendAndReceive(message);
        Message receiveMessage = amqpTemplate.sendAndReceive("test", "test", message);
        System.out.println("return2 : "+ objectMapper.readValue(receiveMessage.getBody(), Test.class));
        System.out.println("receive "+receiveMessage.getMessageProperties());
    }
}
