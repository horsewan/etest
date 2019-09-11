package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.DyUserSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DyUserSettingMapper extends CrudDao<DyUserSetting> {


    DyUserSetting selectByPrimaryKey(Long id);

    List <DyUserSetting> selectByCode(@Param("user_id") Long user_id, @Param("code") String code);

    List <DyUserSetting> selectByCodeValue(@Param("icode") String icode, @Param("ivalue") String ivalue);

    List<DyUserSetting> selectAll();

    void deleteByPrimaryKey(Long id);

    void insertDyUserSetting (DyUserSetting dyUserSetting);

    void updateByPrimaryKey (DyUserSetting dyUserSetting);


}
