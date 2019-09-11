package com.eningqu.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.system.SysServerConfig;

public interface SysServerConfigMapper extends CrudDao<SysServerConfig> {
	
	List<SysServerConfig> selectOptimalRecords();
	/**
	 * 检查主机记录+语音类型+版本号是否重复
	 * @param domain
	 * @param version
	 * @param type
	 * @return
	 */
	Integer selectByUniqueKey(@Param("domain") String domain,@Param("version") Integer version,@Param("type") Integer type);
	/**
	 * 根据主机记录+语音类型更新权重值
	 * @param domain
	 * @param priority
	 * @param type
	 * @return
	 */
	Integer updatePriorityByDomain(@Param("domain") String domain,@Param("priority") Integer priority);
	/**
	 * 根据主机记录+语音类型更新最优版本值
	 * @param domain
	 * @param avaliable
	 * @param type
	 * @return
	 */
	Integer updateAvaliableByDomain(@Param("domain") String domain,@Param("avaliable") Integer avaliable,@Param("type") Integer type);
	/**
	 * 根据主机记录+语音类型查看权重是否唯一
	 * @param domain
	 * @param priority
	 * @param type
	 * @return
	 */
	Integer selectRepeatedRecord(@Param("domain") String domain,@Param("priority") Integer priority);
	/**
	 * 根据主机记录+语音类型查看是否存在最优版本
	 * @param domain
	 * @param avaliable
	 * @param type
	 * @return
	 */
	Integer selectVersionAvaliable(@Param("domain") String domain,@Param("avaliable") Integer avaliable,@Param("type") Integer type);
	
}
