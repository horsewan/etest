package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyUserMobilelist;
import com.eningqu.mapper.DyUserMobilelistMapper;
import com.eningqu.service.DyUserMobilelistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyUserMobilelistServiceImpl extends BaseServiceImpl<DyUserMobilelistMapper, DyUserMobilelist> implements DyUserMobilelistService {

    private Logger logger  = LoggerFactory.getLogger(getClass());

    @Override
    public DyUserMobilelist selectByPrimaryKey(Long id) throws ServiceException{
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List <DyUserMobilelist> selectByMobile(String  mobile) throws ServiceException{
        return baseMapper.selectByMobile(mobile);
    }

    @Override
    public List <DyUserMobilelist> selectByUserId(Long  user_id) throws ServiceException{
        return baseMapper.selectByUserId(user_id);
    }


    @Override
    public  void deleteByPrimaryKey(Long id) throws ServiceException{
        baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertDyUserMobilelist (DyUserMobilelist dyUserMobilelist) throws ServiceException{
        baseMapper.insertDyUserMobilelist(dyUserMobilelist);
    }

    @Override
    public void updateByPrimaryKey (DyUserMobilelist dyUserMobilelist) throws ServiceException{
        baseMapper.updateByPrimaryKey(dyUserMobilelist);
    }


}
