package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.UserOrderAddress;

/**
 *
 * @desc TODO  包名接口service
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

public interface IUserOrderAddressService extends IBaseService<UserOrderAddress> {

    UserOrderAddress selectByWhereId(String orderNumber,Long adid);

}
