package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.UserAddress;

import java.util.List;

/**
 *
 * @desc TODO  用户地址接口service
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

public interface IUserAddressService extends IBaseService<UserAddress> {


    UserAddress selectUserAddressByAUID(Long uid,Long aid);

    UserAddress selectUserAddressByUid(Long uid);

    UserAddress selectUserAddressByUidAndTime(Long uid);

    UserAddress selectIdByUid(Long uid);

    List<UserAddress> selectUserAddressByids(Long uid,Long adidOld,Long adidNew);

}
