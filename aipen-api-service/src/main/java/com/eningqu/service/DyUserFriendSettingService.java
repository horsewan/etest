package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.DyUserFriendSetting;

import java.util.List;


public interface DyUserFriendSettingService extends IBaseService<DyUserFriendSetting> {

    DyUserFriendSetting selectByPrimaryKey(Long id) throws ServiceException;

    List <DyUserFriendSetting> selectByCode(Long user_id,Long user_friend_id,String code) throws ServiceException;

    List<DyUserFriendSetting> selectAll() throws ServiceException;

    void deleteByPrimaryKey(Long id) throws ServiceException;

    void insertDyConstant(DyUserFriendSetting dyUserFriendSetting) throws ServiceException;

    void updateByPrimaryKey(DyUserFriendSetting dyUserFriendSetting) throws ServiceException;

}
