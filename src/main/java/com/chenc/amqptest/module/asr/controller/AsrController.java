package com.chenc.amqptest.module.asr.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
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

import com.chenc.amqptest.base.BaseResponse;
import com.chenc.amqptest.module.asr.pojo.AsrVO;
import com.chenc.amqptest.module.asr.service.AlgoService;


@Slf4j
@RequestMapping("/")
@RestController
public class AsrController {

    private final AmqpTemplate amqpTemplate;
    private final AlgoService algoService;
    

    @Autowired
    public AsrController(AmqpTemplate amqpTemplate, AlgoService algoService){
        this.algoService = algoService;
        this.amqpTemplate = amqpTemplate;
    }

    @PostMapping("/asr")
    public BaseResponse<AsrVO> asr(@RequestParam("audio") MultipartFile audio,@RequestParam("format") String format) throws AmqpException, InterruptedException, IOException, ExecutionException{
        // amqpTemplate.setReplyTimeout(18000);
        log.info("收到请求");
        Future<AsrVO> result = algoService.getAsrCn(audio, format);
        // Future<AsrVO> resultEn = algoService.getAsrEn(audio, format);
        AsrVO asrVO = result.get();
        // AsrVO testEn = resultEn.get();
        return new BaseResponse.Builder<AsrVO>()
                    .setData(asrVO)
                    .setStatus(200)
                    .setMessage("success")
                    .build();
    }

}
