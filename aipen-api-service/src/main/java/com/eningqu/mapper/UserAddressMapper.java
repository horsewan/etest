package com.eningqu.mapper;

import com.eningqu.domain.api.UserAddress;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @desc TODO  包名mapper
 * @author     Yanghuangping
 * @since      2018/9/10 11:11
 * @version    1.0
 *
 **/

public interface UserAddressMapper extends CrudDao<UserAddress> {

    UserAddress selectUserAddressByAUID(@Param("uid")Long uid,@Param("aid")Long aid);

    UserAddress selectUserAddressByUid(@Param("uid")Long uid);
    UserAddress selectUserAddressByUidAndTime(@Param("uid")Long uid);

    UserAddress selectIdByUid(@Param("uid")Long uid);

    List<UserAddress> selectUserAddressByids(@Param("uid")Long uid, @Param("adidOld")Long adidOld,@Param("adidNew")Long adidNew);

}
