package com.chenc.amqptest.utils;

import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class OSSUtils {
    
    /**
     * 获取到临时文件访问的相关key
     * @param endpoint 区域endpoint
     * @param accessKeyID id
     * @param accessKeySecret 密钥
     * @param roleArn 角色id
     * @param regionId 区域id
     * @param roleSessionName 角色名字，可自定义
     * @param productName 产品名字，可自定义
     * @param duration 过期时间
     * @return
     * @throws ServerException
     * @throws ClientException
     */
    public static Credentials getTemporaryAccessKey(String endpoint, String accessKeyID,
                                                String accessKeySecret, String roleArn,
                                                String regionId, String roleSessionName,
                                                String productName, Long duration) throws ServerException, ClientException{
        DefaultProfile.addEndpoint(regionId, productName, endpoint);
        // 构造default profile。
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyID, accessKeySecret);
        // 构造client。
        DefaultAcsClient client = new DefaultAcsClient(profile);
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysMethod(MethodType.POST);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);  // 自定义角色会话名称，用来区分不同的令牌
        // request.setPolicy(null); // 如果policy为空，则用户将获得该角色下所有权限。
        request.setDurationSeconds(duration); // 设置临时访问凭证的有效时间为3600秒。3600L
        final AssumeRoleResponse response = client.getAcsResponse(request);
        return response.getCredentials();
    }
}
