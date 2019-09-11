package com.eningqu.common.config;

import com.eningqu.common.kit.QiNiuKit;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @since      2018/5/14 11:07
 * @version    1.0
 *
 **/
@Configuration
public class QiNiuConfig {

    @Value("${qiniu.domain}")
    private String domain;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;


    @Bean("qiNiuKit")
    @Primary
    public QiNiuKit qiNiuKit(){
        QiNiuKit qiNiuKit = new QiNiuKit(Auth.create(accessKey, secretKey), domain, bucket);
        return qiNiuKit;
    }
}
