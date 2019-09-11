package com.eningqu.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * @desc TODO  如果我们需要在Spring容器完成Bean的实例化，配置和其他的初始化后添加一些自己的逻辑处理，我们就可以定义一个或者多个BeanPostProcessor接口的实现
 * @author Yanghuangping
 * @date 2018/4/17 16:08
 * @version 1.0
 **/
@Configuration
public class InitializeBeanConfig implements BeanPostProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //logger.debug("---对象" + beanName + "开始实例化---");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //logger.debug("---对象" + beanName + "实例化完成---");
        return bean;
    }
}
