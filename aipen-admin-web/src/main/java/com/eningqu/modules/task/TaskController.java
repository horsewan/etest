package com.eningqu.modules.task;

import cn.hutool.core.util.StrUtil;
import com.eningqu.common.kit.SpringContextKit;
import com.eningqu.modules.task.job.Job;
import com.eningqu.modules.task.service.ITaskService;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.exception.AipenException;
import com.eningqu.domain.api.TaskJob;
import org.apache.logging.log4j.core.util.CronExpression;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/16 10:03
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/task")
public class TaskController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ITaskService taskService;


    @GetMapping("")
    @RequiresPermissions("nq:task:list")
    public String list(){
        return "task/task_list";
    }

    @GetMapping("/page")
    @RequiresPermissions(value = {"nq:task:edit", "nq:task:add"}, logical = Logical.OR)
    public String page(){
        return "task/task_page";
    }

    /**
     * TODO 定时任务列表
     * @param dataTable
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions("nq:task:list")
    @Log("查询定时任务列表")
    public DataTable<TaskJob> dataTable(@RequestBody DataTable dataTable){
        taskService.pageSearch(dataTable);
        return dataTable;
    }

    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("nq:task:add")
    public RJson add(TaskJob taskJob){
        if(!cronValid(taskJob.getCronExpression())){
            logger.error("cron表达式：{}错误", taskJob.getCronExpression());
            throw new AipenException("cron表达式错误");
        }
        if(!taskService.insert(taskJob)){
            logger.error("定时任务新增失败");
            throw new AipenException("新增失败");
        }
        return RJson.ok();
    }

    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("nq:task:edit")
    public RJson edit(TaskJob taskJob){
        if(!cronValid(taskJob.getCronExpression())){
            logger.error("cron表达式：{}错误", taskJob.getCronExpression());
            throw new AipenException("cron表达式错误");
        }
        if(!taskService.updateById(taskJob)){
            logger.error("定时任务修改失败");
            throw new AipenException("修改失败");
        }
        start(taskJob.getId());
        return RJson.ok();
    }

    /**
     * TODO 开启定时任务
     * @param id
     * @return
     */
    @GetMapping("/status/{id}")
    @ResponseBody
    @RequiresPermissions("nq:task:run")
    @Log("开启/停止定时任务")
    public RJson start(@PathVariable Long id) {
        TaskJob taskJob = taskService.selectById(id);
        if(taskJob == null){
            logger.error("未查询到定时任务：", id);
            throw new AipenException("操作失败");
        }

        if(StrUtil.equalsIgnoreCase(taskJob.getStatus(), TaskStatusEnum.ON.toString())){
            taskManager.stopCron(id);
            taskJob.setStatus(TaskStatusEnum.OFF.toString());
        }else{
            Job job = SpringContextKit.getBean(taskJob.getClassName(), Job.class);
            if(!taskManager.startCron(id, taskJob.getCronExpression(), job)){
                logger.error("定时任务:{}开启失败", id);
                throw new AipenException("开启失败");
            }
            taskJob.setStatus(TaskStatusEnum.ON.toString());
        }
        taskService.updateById(taskJob);
        return RJson.ok(StrUtil.equalsIgnoreCase(taskJob.getStatus(), TaskStatusEnum.ON.toString()) ? "成功开启" : "成功停止");
    }

    private static boolean cronValid(String cronExpression) {
        try {
            CronExpression exp = new CronExpression(cronExpression);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(cronValid("* * * * * ?"));
    }
}
