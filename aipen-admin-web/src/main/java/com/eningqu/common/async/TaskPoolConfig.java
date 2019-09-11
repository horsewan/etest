package com.eningqu.common.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @desc TODO  线程池配置   也可以 implements AsyncConfigurer  自定义线程池
 * @author     Yanghuangping
 * @date       2018/4/23 8:43
 * @version    1.0
 *
 **/
@EnableAsync
@Configuration
public class TaskPoolConfig {

    /**
     * TODO 系统操作日志记录线程池
     * @return
     */
    @Bean("logThreadPool")
    public ThreadPoolTaskExecutor logThreadPoolExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数 线程池创建时候初始化的线程
        poolTaskExecutor.setCorePoolSize(10);
        //最大线程数，只有在缓冲队列满了之后才会申请超核心的线程数
        poolTaskExecutor.setMaxPoolSize(20);
        //缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
        //允许线程的空闲时间（秒） 当超过核心线程之外的线程在空闲时间到达之后会销毁
        poolTaskExecutor.setKeepAliveSeconds(60);
        //线程池的前缀
        poolTaskExecutor.setThreadNamePrefix("logThreadPool-");
        //线程池对拒绝任务的处理策略
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //当应用程序异常关闭 等候异步任务执行完才关闭  等待时间是60秒
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskExecutor.setAwaitTerminationSeconds(60);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    /**
     * TODO 定时任务线程池
     * @return
     */
    @Bean("poolTaskScheduler")
    public ThreadPoolTaskScheduler poolTaskScheduler (){
        ThreadPoolTaskScheduler poolTaskScheduler = new ThreadPoolTaskScheduler();
        poolTaskScheduler.setPoolSize(5);
        poolTaskScheduler.setThreadNamePrefix("task-pool-");
        poolTaskScheduler.setAwaitTerminationSeconds(60);
        poolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskScheduler.initialize();
        return poolTaskScheduler;
    }

    /**
     * TODO EXCEL导入线程池
     * @return
     */
    @Bean("importPoolTaskExecutor")
    public ThreadPoolTaskExecutor importPoolTaskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(10);
        poolTaskExecutor.setQueueCapacity(200);
        poolTaskExecutor.setKeepAliveSeconds(30);
        poolTaskExecutor.setThreadNamePrefix("importPool-");
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        poolTaskExecutor.setAwaitTerminationSeconds(60);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }
}
