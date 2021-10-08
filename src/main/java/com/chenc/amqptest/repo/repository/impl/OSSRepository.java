package com.chenc.amqptest.repo.repository.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.net.URL;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.chenc.amqptest.config.OSSConfig;
import com.chenc.amqptest.repo.repository.FileStorageRespsitory;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云对象储存仓库
 */
@Slf4j
@AllArgsConstructor
@Repository
public class OSSRepository implements FileStorageRespsitory{
    
    public final OSS ossClient;
    public final OSSConfig ossConfig;

    @Override
    public Boolean isAvailable() {
        // 如果凭据无效，但是endpoint有效且存在Bucket也能够返回true。如果endpoint无效报错。
        return ossClient.doesBucketExist(ossConfig.bucketName);
    }

    @Override
    public boolean saveFile(byte[] file, String path) {
        try{
            ossClient.putObject(ossConfig.bucketName, path, new BufferedInputStream(new ByteArrayInputStream(file)));
        }catch (OSSException e){
            log.error("OSS服务器端异常", e);
            return false;
        }catch (ClientException e){
            log.error("OSS客户端异常", e);
            return false;
        } catch (Exception e){
            log.error("OSS未知异常", e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteFile(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getURL(String path) {
        // 设置签名URL过期时间为3600秒（1小时）。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(ossConfig.bucketName, path, expiration);
        String urlS = url.toExternalForm();
        return urlS;
    }
    
    
}
