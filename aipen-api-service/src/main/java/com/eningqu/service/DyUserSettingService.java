package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyUserSetting;

import java.util.List;


public interface DyUserSettingService extends IBaseService<DyUserSetting> {

    DyUserSetting selectByPrimaryKey(Long id) throws ServiceException;

    List <DyUserSetting> selectByCode(Long user_id,String code) throws ServiceException;

    List <DyUserSetting> selectByCodeValue(String icode,String ivalue) throws ServiceException;


    List<DyUserSetting> selectAll() throws ServiceException;

    void deleteByPrimaryKey(Long id) throws ServiceException;

    void insertDyConstant(DyUserSetting dyUserSetting) throws ServiceException;

    void updateByPrimaryKey(DyUserSetting dyUserSetting) throws ServiceException;

}
