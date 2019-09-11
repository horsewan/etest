package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyConstant;

import java.util.List;


public interface DyConstantService extends IBaseService<DyConstant> {

    DyConstant selectByPrimaryKey(Long id) throws ServiceException;

    List <DyConstant> selectByCode(String code) throws ServiceException;

    List<DyConstant> selectAll() throws ServiceException;

    void deleteByPrimaryKey(Long id) throws ServiceException;

    void insertDyConstant (DyConstant dyConstant) throws ServiceException;

    void updateByPrimaryKey (DyConstant dyConstant) throws ServiceException;

}
