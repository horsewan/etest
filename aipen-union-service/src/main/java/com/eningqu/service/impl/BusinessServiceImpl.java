package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.BusinessInfo;
import com.eningqu.mapper.BusinessMapper;
import com.eningqu.service.IBusinessService;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/11/20 16:43
 * @version    1.0
 *
 **/

@Service
public class BusinessServiceImpl extends BaseServiceImpl<BusinessMapper, BusinessInfo> implements IBusinessService {


    @Override
    public BusinessInfo selectByTicketAndSingleNo(String ticket, String singleNo) {
        return baseMapper.selectByTicketAndSingleNo(ticket,singleNo);
    }

    @Override
    public BusinessInfo selectByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }
}
