package com.eningqu.modules.system.service;

import java.util.List;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysServerConfig;

public interface ISysServerConfigService extends IBaseService<SysServerConfig> {
	
	boolean check(String domain,Integer version,Integer type) throws ServiceException;
	
	int add(SysServerConfig sysServerConfig) throws ServiceException;
	
	int update(SysServerConfig sysServerConfig) throws ServiceException;
	
	int delById(Long id) throws ServiceException;
	
	SysServerConfig selectById(Long id) throws ServiceException;
	
	List<SysServerConfig> selectOptimalRecords() throws ServiceException;
	
	List<SysServerConfig> selectList() throws ServiceException;
	
	boolean checkRepeated(String domain, Integer priority) throws ServiceException;

	boolean checkVersionAvaliable(String domain,Integer type) throws ServiceException;
}
