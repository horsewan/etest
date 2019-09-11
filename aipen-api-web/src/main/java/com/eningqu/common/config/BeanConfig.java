package com.eningqu.common.config;

import com.eningqu.common.constant.Global;
import com.eningqu.common.kit.RSAKit;
import com.eningqu.common.sms.SmsConfig;
import com.eningqu.common.sms.SmsTool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/5/10 11:56
 * @version    1.0
 *
 **/
@Configuration
public class BeanConfig {

    @Value("${file.upload-root}")
    public void setFileUploadRoot(String root){
        Global.FILE_UPLOAD_ROOT = root;
    }

    @Value("${keys.rsa.public-key}")
    private String publicKeyBase64;
    @Value("${keys.rsa.private-key}")
    private String privateKeyBase64;

    @Bean("RSAKit")
    @Primary
    public RSAKit getRSAKit(){
        return new RSAKit(publicKeyBase64, privateKeyBase64);
    }


    @Bean("smsTool")
    @Primary
    public SmsTool smsTool(@Qualifier("smsConfig") SmsConfig smsConfig){
        SmsTool smsTool = new SmsTool(smsConfig);
        return smsTool;
    }
}
