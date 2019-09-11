package com.eningqu.modules.task.job;

import com.eningqu.modules.api.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO 定时处理普通用户有经纬度数据，但分配的个代为A00000则需要重新分配个代。（需要判断是否为市级代理的用户）
 * @date 2019/9/3 11:51
 **/
@Service("userJob")
public class UserJob implements Job {

    @Autowired
    private IUserService userService;

    @Override
    public void execute() {
        userService.updateUserAgentNo();
    }
}
