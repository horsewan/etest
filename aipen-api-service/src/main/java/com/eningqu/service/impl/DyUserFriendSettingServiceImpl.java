package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyUserFriendSetting;
import com.eningqu.mapper.DyUserFriendSettingMapper;
import com.eningqu.service.DyUserFriendSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyUserFriendSettingServiceImpl extends BaseServiceImpl<DyUserFriendSettingMapper, DyUserFriendSetting> implements DyUserFriendSettingService {

    private Logger logger  = LoggerFactory.getLogger(getClass());

    @Override
    public DyUserFriendSetting selectByPrimaryKey(Long id) throws ServiceException{
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List <DyUserFriendSetting> selectByCode(Long user_id,Long user_friend_id,String  code) throws ServiceException{
        return baseMapper.selectByCode(user_id,user_friend_id,code);
    }


    public List<DyUserFriendSetting> selectAll() throws ServiceException{
        return baseMapper.selectAll();
    }

    @Override
    public  void deleteByPrimaryKey(Long id) throws ServiceException{
        baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertDyConstant (DyUserFriendSetting dyUserFriendSetting) throws ServiceException{
        baseMapper.insertDyUserFriendSetting(dyUserFriendSetting);
    }

    @Override
    public void updateByPrimaryKey (DyUserFriendSetting dyUserFriendSetting) throws ServiceException{
        baseMapper.updateByPrimaryKey(dyUserFriendSetting);
    }


}
