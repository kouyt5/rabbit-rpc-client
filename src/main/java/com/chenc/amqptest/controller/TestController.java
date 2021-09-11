package com.chenc.amqptest.controller;

import com.chenc.amqptest.poolreq.AlgoService;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.chenc.amqptest.pojo.AsrVO;
import com.chenc.amqptest.pojo.Test;


@Slf4j
@RequestMapping("/")
@RestController
public class TestController {

    private final AmqpTemplate amqpTemplate;
    private final AlgoService algoService;
    

    @Autowired
    public TestController(AmqpTemplate amqpTemplate, AlgoService algoService){
        this.algoService = algoService;
        this.amqpTemplate = amqpTemplate;
    }

    @PostMapping("/test")
    public Test test(@RequestParam String a,@RequestParam String b) throws AmqpException, InterruptedException, IOException, ExecutionException{
        // amqpTemplate.setReplyTimeout(18000);
        // Future<Test> result = algoService.getTest(a, b);
        // Test test = result.get();
        return new Test();
    }
    @PostMapping("/asr")
    public AsrVO asr(@RequestParam("audio") MultipartFile audio,@RequestParam("format") String format) throws AmqpException, InterruptedException, IOException, ExecutionException{
        // amqpTemplate.setReplyTimeout(18000);
        log.info("收到请求");
        Future<AsrVO> result = algoService.getAsr(audio, format);
        Future<AsrVO> resultEn = algoService.getAsrEn(audio, format);
        AsrVO test = result.get();
        AsrVO testEn = resultEn.get();
        log.info(testEn.toString());
        return test;
    }

    @PostMapping("/asrtest")
    public Test asrt(@RequestParam("audio") MultipartFile audio,@RequestParam("format") String format) throws AmqpException, InterruptedException, IOException, ExecutionException{
        // amqpTemplate.setReplyTimeout(18000);
        // Future<AsrVO> result = algoService.getAsr(audio, format);
        Test test = new Test("1","2");
        return test;
    }
}
