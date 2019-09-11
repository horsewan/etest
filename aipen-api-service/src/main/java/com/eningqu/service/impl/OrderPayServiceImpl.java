package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.OrderPay;
import com.eningqu.mapper.OrderPayMapper;
import com.eningqu.service.IOrderPayService;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  包名接口service实现类
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

@Service
public class OrderPayServiceImpl extends BaseServiceImpl<OrderPayMapper, OrderPay> implements IOrderPayService {


    @Override
    public OrderPay selectByWhere(String orderNumber) {
        return baseMapper.selectByWhere(orderNumber);
    }
}
