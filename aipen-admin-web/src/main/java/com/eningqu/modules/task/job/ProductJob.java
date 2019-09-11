package com.eningqu.modules.task.job;

import com.eningqu.modules.api.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO 每天晚上11点定时提醒所有未领取全部红包的用户，推送消息方式（个代聊天页面对话框方式推送
 * @date 2019/9/3 16:28
 **/
@Service("productJob")
public class ProductJob implements Job {

    @Autowired
    private IUserService userService;

    @Override
    public void execute() {
        userService.sendMsgToUnGetProductUser();
    }
}
