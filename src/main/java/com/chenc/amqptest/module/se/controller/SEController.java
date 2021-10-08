package com.chenc.amqptest.module.se.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.chenc.amqptest.base.BaseResponse;
import com.chenc.amqptest.module.se.pojo.SEMQResp;
import com.chenc.amqptest.module.se.pojo.SEVo;
import com.chenc.amqptest.module.se.service.SEService;
import com.chenc.amqptest.repo.repository.FileStorageRespsitory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;


@RequestMapping("/api")
@AllArgsConstructor
@RestController
public class SEController {
    
    private final SEService seService;
    private final FileStorageRespsitory fileStorageRespsitory;


    @PostMapping("/se")
    public BaseResponse<SEVo> seBase(MultipartFile audio, @RequestParam(defaultValue = "wav") String format) throws InterruptedException, IOException, ExecutionException{

        Future<SEMQResp> fseMQResp = seService.getSE(audio.getBytes(), format);
        SEMQResp seMQResp = fseMQResp.get();
        SEVo seVo = new SEVo("", seMQResp.getDuration(), seMQResp.getEnhance());
        
        // 保存音频到仓库中 service.saveFile(byte[]);
        LocalDateTime localDateTime = LocalDateTime.now();
        String folder = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String namePrefix = localDateTime.format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        String path = "audio/"+folder+"/"+namePrefix+"-"+UUID.randomUUID().toString().substring(0, 10)+".wav";
        fileStorageRespsitory.saveFile(seVo.getEnhance(), path);
        String url = fileStorageRespsitory.getURL(path);
        seVo.setUrl(url);
        // FileOutputStream outputStream = new FileOutputStream("output.wav");
        // outputStream.write(seVo.getEnhance());
        // outputStream.close();
        return new BaseResponse.Builder<SEVo>()
                    .setData(seVo)
                    .setMessage("success")
                    .setStatus(200)
                    .build();
    }


}
