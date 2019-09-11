package com.eningqu.common.redis;

import com.eningqu.common.kit.RedisKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;

/**
 * @desc TODO   继承CachingConfigurerSupport并重写方法,配合该注解实现spring缓存框架的启用
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/24 10:17
 **/
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        initRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        // 设置缓存过期时间 （秒）
        redisCacheManager.setDefaultExpiration(3600);
        return redisCacheManager;
    }

    /**
     * TODO 设置数据存入 redis 的序列化方式
     *
     * @param redisTemplate
     * @param factory
     */
    private void initRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
        // 设置key的序列化
        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(jdkSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(jdkSerializer);
    }

    /**
     * TODO 重写缓存的key生成策略,可根据自身业务需要进行自己的配置生成条件
     *
     * @see org.springframework.cache.annotation.CachingConfigurerSupport#
     * keyGenerator()
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder key = new StringBuilder();
                key.append(target.getClass().getName());
                key.append(method.getName());
                for (Object param : params) {
                    key.append(param.toString());
                }
                return key.toString();
            }
        };
    }

    @Bean
    public RedisKit getRedisKit(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        RedisKit redisKit = new RedisKit();
        redisKit.setRedisTemplate(redisTemplate);
        redisKit.setStringRedisTemplate(stringRedisTemplate);
        return redisKit;
    }

}
