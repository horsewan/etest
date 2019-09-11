package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.DyUserFriendSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DyUserFriendSettingMapper extends CrudDao<DyUserFriendSetting> {


    DyUserFriendSetting selectByPrimaryKey(Long id);

    List <DyUserFriendSetting> selectByCode(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id, @Param("code") String code);

    List<DyUserFriendSetting> selectAll();

    void deleteByPrimaryKey(Long id);

    void insertDyUserFriendSetting (DyUserFriendSetting dyUserFriendSetting);

    void updateByPrimaryKey (DyUserFriendSetting dyUserFriendSetting);


}
