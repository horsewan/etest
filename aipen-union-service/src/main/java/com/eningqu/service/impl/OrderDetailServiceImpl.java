package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.OrdersDetail;
import com.eningqu.mapper.OrdersDetailMapper;
import com.eningqu.service.IOrderDetailService;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/6 16:21
 * @version    1.0
 *
 **/
@Service
public class OrderDetailServiceImpl extends BaseServiceImpl<OrdersDetailMapper, OrdersDetail> implements IOrderDetailService {
}
