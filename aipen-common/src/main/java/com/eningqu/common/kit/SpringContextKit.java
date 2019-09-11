package com.eningqu.common.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @desc TODO  获取spring context
 * @author     Yanghuangping
 * @date       2018/4/18 16:39
 * @version    1.0
 *
 **/
@Component
public class SpringContextKit implements ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(SpringContextKit.class);

    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * TODO 设置spring上下文
     * @param applicationContext spring上下文
     * @throws BeansException
     * */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("---------ApplicationContext 上下文--------------");
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * TODO 获取容器
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * TODO 根据Clazz返回指定的 Bean
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        return APPLICATION_CONTEXT.getBean(type);
    }

    /**
     * TODO 根据name,以及Clazz返回指定的 Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * TODO 通过name获取 Bean
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }
}
