package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.OrderPay;

/**
 *
 * @desc TODO  包名接口service
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

public interface IOrderPayService extends IBaseService<OrderPay> {


    OrderPay selectByWhere(String orderNumber);

}
