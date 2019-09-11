package com.eningqu.mapper;

import com.eningqu.domain.system.SysCity;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;


/**
 * TODO 字典表 Mapper 接口
 */
public interface SysCityMapper extends CrudDao<SysCity> {
    SysCity selectByCityName(@Param("cityName") String cityName);
}
