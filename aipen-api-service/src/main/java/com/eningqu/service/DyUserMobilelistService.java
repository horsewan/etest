package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyUserMobilelist;

import java.util.List;


public interface DyUserMobilelistService extends IBaseService<DyUserMobilelist> {

    DyUserMobilelist selectByPrimaryKey(Long id) throws ServiceException;

    List <DyUserMobilelist> selectByMobile(String mobile) throws ServiceException;

    List <DyUserMobilelist> selectByUserId(Long user_id) throws ServiceException;

    //List <DyUserMobilelist> selectByCodeValue(String icode, String ivalue) throws ServiceException;


    //List<DyUserMobilelist> selectAll() throws ServiceException;

    void deleteByPrimaryKey(Long id) throws ServiceException;

    void insertDyUserMobilelist(DyUserMobilelist dyUserMobilelist) throws ServiceException;

    void updateByPrimaryKey(DyUserMobilelist dyUserMobilelist) throws ServiceException;

}
