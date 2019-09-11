package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.UserAddress;
import com.eningqu.mapper.UserAddressMapper;
import com.eningqu.service.IUserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @desc TODO  包名接口service实现类
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {


    @Override
    public UserAddress selectUserAddressByAUID(Long uid, Long aid) {
        return baseMapper.selectUserAddressByAUID(uid,aid);
    }

    @Override
    public UserAddress selectUserAddressByUid(Long uid) {
        return baseMapper.selectUserAddressByUid(uid);
    }

    @Override
    public UserAddress selectUserAddressByUidAndTime(Long uid) {
        return baseMapper.selectUserAddressByUidAndTime(uid);
    }

    @Override
    public UserAddress selectIdByUid(Long uid) {
        return baseMapper.selectIdByUid(uid);
    }

    @Override
    public List<UserAddress> selectUserAddressByids(Long uid, Long adidOld,Long adidNew) {
        return baseMapper.selectUserAddressByids(uid,adidOld,adidNew);
    }
}
