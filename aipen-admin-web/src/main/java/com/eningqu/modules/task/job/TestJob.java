package com.eningqu.modules.task.job;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/16 10:02
 * @version    1.0
 *
 **/

@Service("testJob")
public class TestJob implements Job {

    @Override
    public void execute() {
        System.out.println("----- 我是每隔五秒执行一次的定时任务 -----");
    }

}
