package com.eningqu.modules.api.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.UserOrderAddress;
import com.eningqu.modules.api.mapper.UserOrderAddressMapper;
import com.eningqu.modules.api.service.IUserOrderAddressService;
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
public class UserOrderAddressServiceImpl extends BaseServiceImpl<UserOrderAddressMapper, UserOrderAddress> implements IUserOrderAddressService {


    @Override
    public UserOrderAddress selectByWhereId(String orderNumber, Long adid) {
        return baseMapper.selectByWhereId(orderNumber,adid);
    }
}
