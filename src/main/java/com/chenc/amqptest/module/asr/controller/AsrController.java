package com.chenc.amqptest.module.asr.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import com.chenc.amqptest.base.BaseResponse;
import com.chenc.amqptest.base.exception.TimeoutException;
import com.chenc.amqptest.module.asr.pojo.AsrVO;
import com.chenc.amqptest.module.asr.service.AsrService;


@Slf4j
@RequestMapping("/")
@RestController
public class AsrController {

    private final AsrService asrService;
    
    @Autowired
    public AsrController(AsrService asrService){
        this.asrService = asrService;
    }

    @PostMapping("/asr")
    public BaseResponse<AsrVO> asr(@RequestParam("audio") MultipartFile audio,@RequestParam("format") String format) throws AmqpException, InterruptedException, IOException, ExecutionException, TimeoutException{
        // amqpTemplate.setReplyTimeout(18000);
        log.info("收到请求");
        AsrVO asrVO = asrService.getCnResult(audio, format);
        // Future<AsrVO> resultEn = algoService.getAsrEn(audio, format);
        // AsrVO testEn = resultEn.get();
        return new BaseResponse.Builder<AsrVO>()
                    .setData(asrVO)
                    .setStatus(200)
                    .setMessage("success")
                    .build();
    }

}
