package com.eningqu.modules.task.job;

import com.eningqu.modules.api.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO 定时处理普通用户上传的通讯录数据
 * @date 2019/9/3 11:51
 **/
@Service("userContactJob")
public class UserContactJob implements Job {

    @Autowired
    private IUserService userService;

    @Override
    public void execute() {
        userService.updateUserContact();
    }
}
