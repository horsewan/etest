package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyConstant;
import com.eningqu.mapper.DyConstantMapper;
import com.eningqu.service.DyConstantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DyConstantServiceImpl extends BaseServiceImpl<DyConstantMapper, DyConstant> implements DyConstantService {

    private Logger logger  = LoggerFactory.getLogger(getClass());

    @Override
    public DyConstant selectByPrimaryKey(Long id) throws ServiceException{
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List <DyConstant> selectByCode(String  code) throws ServiceException{
        return baseMapper.selectByCode(code);
    }


    public List<DyConstant> selectAll() throws ServiceException{
        return baseMapper.selectAll();
    }

    @Override
    public  void deleteByPrimaryKey(Long id) throws ServiceException{
        baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertDyConstant (DyConstant dyConstant) throws ServiceException{
        baseMapper.insertDyConstant(dyConstant);
    }

    @Override
    public void updateByPrimaryKey (DyConstant dyConstant) throws ServiceException{
        baseMapper.updateByPrimaryKey(dyConstant);
    }


}
