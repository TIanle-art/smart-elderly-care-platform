package com.lcyl.web.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {
    @Bean
    public OSS ossClient(@Value("${aliyun.oss.endpoint}")String endpoint,
                         @Value("${aliyun.oss.accessKeyId}")String accessKeyId,
                         @Value("${aliyun.oss.accessKeySecret}")String
                                 accessKeySecret){
        final OSS ossClient = new OSSClientBuilder ()
                .build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }
}