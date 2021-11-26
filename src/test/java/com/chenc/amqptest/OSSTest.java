package com.chenc.amqptest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.chenc.amqptest.config.OSSConfig;
import com.chenc.amqptest.repo.repository.FileStorageRespsitory;
import com.chenc.amqptest.utils.OSSUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


@SpringBootTest
public class OSSTest {

    @Autowired
    private OSS ossClient;

    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private FileStorageRespsitory fileStorageRespsitory;
    
    @Test
    public void testPut() throws ServerException, ClientException{
        
        ossClient.putObject("chencong-free", "test.wav", new File("/data/chenc/asr/rabbit-rpc-client/output.wav"));
        // 设置签名URL过期时间为3600秒（1小时）。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl("chencong-free", "test.wav", expiration);
        String urlS = url.toExternalForm();
        Credentials credential = OSSUtils.getTemporaryAccessKey(ossConfig.endpoint, 
                                    ossConfig.accessKeyId, 
                                    ossConfig.accessKeySecret, 
                                    ossConfig.roleArn, 
                                    "cn-qingdao","roleSessionName",  "productName", 60*15L);
        OSS ossClient2 = new OSSClientBuilder().build(ossConfig.endpoint, credential.getAccessKeyId(), 
                            credential.getAccessKeySecret(), credential.getSecurityToken());
        // List<Bucket> buckets = ossClient2.listBuckets();
        ossClient2.putObject("chencong-free", "test3.wav", new File("/data/chenc/asr/rabbit-rpc-client/output.wav"));
    }

    @Test
    public void testStream() throws IOException{
        FileInputStream fileInputStream = new FileInputStream(new File("/data/chenc/asr/rabbit-rpc-client/output.wav"));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.mark(bufferedInputStream.available() + 1);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bufferedInputStream.readAllBytes());
        bufferedInputStream.reset();
        ossClient.putObject("chencong-free", "fileInputStream.wav", bufferedInputStream);
        ossClient.putObject("chencong-free", "byteArrayInputStream.wav", byteArrayInputStream);
    }

    @Test
    public void testExist(){
        Assert.isTrue(fileStorageRespsitory.isAvailable(), "判断文件储存是否可用");
    }
}
