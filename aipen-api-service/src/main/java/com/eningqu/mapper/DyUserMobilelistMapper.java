package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.DyUserMobilelist;

import java.util.List;


public interface DyUserMobilelistMapper extends CrudDao<DyUserMobilelist> {

    int deleteByPrimaryKey(Long id);

    int insertDyUserMobilelist(DyUserMobilelist dyUserMobilelist);

    int insertSelective(DyUserMobilelist dyUserMobilelist);

    List<DyUserMobilelist> selectByMobile(String mobile);

    List<DyUserMobilelist> selectByUserId(Long user_id);

    DyUserMobilelist selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DyUserMobilelist dyUserMobilelist);

    int updateByPrimaryKey(DyUserMobilelist dyUserMobilelist);

}