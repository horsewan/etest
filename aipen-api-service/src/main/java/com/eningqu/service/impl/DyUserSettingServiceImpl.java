package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyUserSetting;
import com.eningqu.mapper.DyUserSettingMapper;
import com.eningqu.service.DyUserSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyUserSettingServiceImpl extends BaseServiceImpl<DyUserSettingMapper, DyUserSetting> implements DyUserSettingService {

    private Logger logger  = LoggerFactory.getLogger(getClass());

    @Override
    public DyUserSetting selectByPrimaryKey(Long id) throws ServiceException{
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List <DyUserSetting> selectByCode(Long user_id,String  code) throws ServiceException{
        return baseMapper.selectByCode(user_id,code);
    }

    @Override
    public List <DyUserSetting> selectByCodeValue(String icode,String ivalue) throws ServiceException{
        return baseMapper.selectByCodeValue(icode,ivalue);
    }


    public List<DyUserSetting> selectAll() throws ServiceException{
        return baseMapper.selectAll();
    }

    @Override
    public  void deleteByPrimaryKey(Long id) throws ServiceException{
        baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertDyConstant (DyUserSetting dyUserSetting) throws ServiceException{
        baseMapper.insertDyUserSetting(dyUserSetting);
    }

    @Override
    public void updateByPrimaryKey (DyUserSetting dyUserSetting) throws ServiceException{
        baseMapper.updateByPrimaryKey(dyUserSetting);
    }


}
