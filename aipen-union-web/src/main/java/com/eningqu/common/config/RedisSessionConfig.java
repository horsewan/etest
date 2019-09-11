package com.eningqu.common.config;

import com.eningqu.common.constant.Global;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/5/2 19:45
 * @version    1.0
 *
 **/
@Configuration
@EnableRedisHttpSession( maxInactiveIntervalInSeconds = Global.SSESSION_EXPIRE_TIME)
public class RedisSessionConfig {

}
