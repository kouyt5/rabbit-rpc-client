package com.chenc.amqptest.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * 用于阿里云OSS的配置文件
 * @author chenc
 * @since 2021/10/4
 */
@Configuration
@PropertySource("classpath:oss.properties")
public class OSSConfig {
    
    @Value("${endpoint}")
    public String endpoint;

    @Value("${accessKeyID}")
    public String accessKeyId;

    @Value("${accessKeySecret}")
    public String accessKeySecret;

    @Value("${maxConnections}")
    public int maxConnections;

    @Value("${connectionTimeout}")
    public int connectionTimeout;

    @Value("${idleConnectionTimeout}")
    public int idleConnectionTimeout;

    @Value("${maxErrorRetry}")
    public int maxErrorRetry;

    @Value("${roleArn}")
    public String roleArn;

    @Value("${bucketName}")
    public String bucketName;

    /**
     * OSS 的配置文件
     * @return
     */
    @Bean
    public ClientBuilderConfiguration ClientBuilderConfiguration(){
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(maxConnections);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(connectionTimeout);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(connectionTimeout);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(-1);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(idleConnectionTimeout);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(5);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
        conf.setSupportCname(true);
        // 设置是否开启二级域名的访问方式，默认不开启。
        conf.setSLDEnabled(false);
        // 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP。
        conf.setProtocol(Protocol.HTTP);
        // 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
        conf.setUserAgent("aliyun-sdk-java");
        // 设置代理服务器端口。
        conf.setProxyHost(null);
        // 设置代理服务器验证的用户名。
        conf.setProxyUsername(null);
        // 设置代理服务器验证的密码。
        conf.setProxyPassword(null);
        // 设置是否开启HTTP重定向，默认开启。
        conf.setRedirectEnable(true);
        // 设置是否开启SSL证书校验，默认开启。
        conf.setVerifySSLEnable(true);
        return conf;
    }

    /**
     * 阿里云OSS对象，用于文件操作的关键对象
     * @param conf 配置文件 @see {@link ClientBuilderConfiguration}
     * @return {@link OSS}
     */
    @Bean
    public OSS ossClient(ClientBuilderConfiguration conf){
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
    }
}
