package com.eningqu.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.domain.api.BalanceResult;
import com.eningqu.domain.api.BusinessFinanceResult;
import com.eningqu.domain.api.BusinessInfo;
import com.eningqu.mapper.BusinessMapper;
import com.eningqu.service.IBusinessService;
import com.eningqu.vo.LoginInfoHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/11/20 16:43
 **/

@Service
public class BusinessServiceImpl extends BaseServiceImpl<BusinessMapper, BusinessInfo> implements IBusinessService {


    @Override
    public BusinessInfo selectByTicketAndSingleNo(String ticket, String singleNo) {
        return baseMapper.selectByTicketAndSingleNo(ticket, singleNo);
    }

    @Override
    public BusinessInfo selectByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }

    @Override
    public BusinessFinanceResult selectBusinessByPhone(String phone) {
        return baseMapper.selectBusinessByPhone(phone);
    }

    @Override
    public RJson selectUserBalanceList(int page, int limit) {
        Page<BalanceResult> page1 = new Page<BalanceResult>(page, limit);
        String mobile = LoginInfoHelper.getUserMobile();
        if (StringUtils.isEmpty(mobile)) {
            return RJson.error("用户数据不存在!请尝试重新登录。");
        }
        return RJson.ok().setData(baseMapper.selectUserBalanceList(page1, mobile));
    }
}
