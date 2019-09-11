package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.system.SysCity;
import com.eningqu.mapper.SysCityMapper;
import com.eningqu.service.ISysCityService;
import org.springframework.stereotype.Service;


/**
 *
 * @desc TODO  城市service实现类
 * @author     Yanghuangping
 * @date       2018/4/18 19:44
 * @version    1.0
 *
 **/
@Service
public class SysCityServiceImpl extends BaseServiceImpl<SysCityMapper, SysCity> implements ISysCityService {


    @Override
    public SysCity selectByCityName(String cityName) {
        return baseMapper.selectByCityName(cityName);
    }
}
