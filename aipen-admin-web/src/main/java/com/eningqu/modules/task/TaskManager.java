package com.eningqu.modules.task;

import com.eningqu.common.kit.SpringContextKit;
import com.eningqu.modules.task.job.Job;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @desc TODO  定时任务
 * @author     Yanghuangping
 * @since      2018/8/15 16:44
 * @version    1.0
 *
 **/
@Component
public class TaskManager{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Qualifier("poolTaskScheduler")
    @Autowired
    private ThreadPoolTaskScheduler poolTaskScheduler;

    // 任务线程
    private Map<Long, ScheduledFuture<?>> futureMaps = Maps.newHashMap();

    /**
     * TODO 启动定时任务
     * @param id
     * @param corn
     * @param job
     */
    public boolean startCron(Long id , String corn, Job job){
        // TODO 先停止 再开启线程执行
        stopCron(id);
        ScheduledFuture<?> future;
        try {
            future = poolTaskScheduler.schedule(() -> job.execute(), new CronTrigger(corn));
        } catch (Exception e) {
            logger.error("任务编号：{}的定时任务开启异常，{}", id, e);
            return false;
        }

        // TODO 把执行任务的线程保存起来，根据此ScheduledFuture停止任务
        if(future != null){
            futureMaps.put(id, future);
            logger.info("任务编号：{}的定时任务已启动", id);
            return true;
        }else {
            return false;
        }


    }

    /**
     * TODO 停止定时任务
     * @param id
     */
    public void stopCron(Long id){
        ScheduledFuture<?> future = futureMaps.get(id);
        if(future != null && !future.isCancelled()) {
            future.cancel(true);
            logger.info("任务编号：{}的定时任务已停止", id);
        }
    }

    public static void main(String[] args) {
        Job job = SpringContextKit.getBean("TestJob", Job.class);

    }
}
