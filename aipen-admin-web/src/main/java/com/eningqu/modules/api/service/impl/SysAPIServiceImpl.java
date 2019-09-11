package com.eningqu.modules.api.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.SysAPIInfo;
import com.eningqu.modules.api.mapper.SysAPIMapper;
import com.eningqu.modules.api.service.ISysAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @desc TODO  用户信息service接口实现类
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/5/2 19:38
 */
@Service
public class SysAPIServiceImpl extends BaseServiceImpl<SysAPIMapper, SysAPIInfo> implements ISysAPIService {

    private Logger logger  = LoggerFactory.getLogger(getClass());


    @Override
    public SysAPIInfo selectSysAPIBySta() {

        return baseMapper.selectSysAPIBySta();
    }

    @Override
    public SysAPIInfo selectUserRegBySta() {
        return baseMapper.selectUserRegBySta();
    }

}
