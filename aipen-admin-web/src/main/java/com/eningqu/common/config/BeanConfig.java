package com.eningqu.common.config;

import com.eningqu.common.kit.ValidateCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/26 14:02
 * @version    1.0
 *
 **/
@Configuration
public class BeanConfig {

    /**
     * 验证码
     * @return
     */
    @Bean
    @Primary
    public ValidateCode validateCode(){
        return new ValidateCode();
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper;
    }
}
