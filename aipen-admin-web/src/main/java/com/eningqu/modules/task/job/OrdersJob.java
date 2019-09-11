package com.eningqu.modules.task.job;

import com.eningqu.modules.api.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvbu
 * @version 1.0
 * @desc TODO 查询15分钟内未支付订单，超过改时间订单则为已取消状态。
 * @date 2019/9/4 10:35
 **/
@Service("ordersJob")
public class OrdersJob implements Job {
    @Autowired
    private IOrderService orderService;

    @Override
    public void execute() {
        orderService.returnMoneyOnNoPay();
    }
}
