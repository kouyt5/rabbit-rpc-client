package com.chenc.amqptest.module.asr.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.amqp.core.Message;

import com.chenc.amqptest.base.exception.TimeoutException;
import com.chenc.amqptest.module.asr.pojo.AsrVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 语音识别算法服务层
 * @author cc
 * @since 21.11.26
 */
@AllArgsConstructor
@Service
@Slf4j
public class AsrService {
    private final AlgoService algoService;
    private ObjectMapper objectMapper;
    
    /**
     * 获取算法结果
     * @param audio 音频
     * @param format 音频格式 wav, mp3 ...etc
     * @return
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws IOException
     * @throws ExecutionException
     */
    public AsrVO getCnResult(MultipartFile audio, String format) throws TimeoutException, InterruptedException, IOException, ExecutionException{
        Future<Message> message = algoService.getAsrCnMQ(audio, format);
        Message mqResult = message.get();
        if(mqResult==null) throw new TimeoutException();
        AsrVO asrVO = objectMapper.readValue(mqResult.getBody(), AsrVO.class);
        log.info("算法结果: "+ asrVO.toString());
        return asrVO;
    }

    public AsrVO getEnResult(MultipartFile audio, String format) throws TimeoutException, InterruptedException, IOException, ExecutionException{
        Future<Message> message = algoService.getAsrEnMQ(audio, format);
        Message mqResult = message.get();
        if(mqResult==null) throw new TimeoutException();
        AsrVO asrVO = objectMapper.readValue(mqResult.getBody(), AsrVO.class);
        log.info("算法结果: "+ asrVO.toString());
        return asrVO;
    }
}
