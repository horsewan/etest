package com.eningqu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * 描    述：  TODO  异步执行任务线程池配置
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/28 15:54
 *
 */
@Configuration
public class PoolTaskConfig {

    @Bean("asyncPool")
    public Executor poolTaskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数 线程池创建时候初始化的线程
        poolTaskExecutor.setCorePoolSize(20);
        //最大线程数，只有在缓冲队列满了之后才会申请超核心的线程数
        poolTaskExecutor.setMaxPoolSize(200);
        //缓冲队列
        poolTaskExecutor.setQueueCapacity(500);
        //允许线程的空闲时间（秒） 当超过核心线程之外的线程在空闲时间到达之后会销毁
        poolTaskExecutor.setKeepAliveSeconds(60);
        //线程池的前缀
        poolTaskExecutor.setThreadNamePrefix("async-pool-");
        //线程池对拒绝任务的处理策略
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //当应用程序异常关闭 等候异步任务执行完才关闭  等待时间是60秒
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskExecutor.setAwaitTerminationSeconds(60);
        //执行初始化
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

}
