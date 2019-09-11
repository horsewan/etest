package com.eningqu.modules.system.service.impl;


import java.util.List;

import com.eningqu.common.kit.RedisKit;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysServerConfig;
import com.eningqu.modules.system.mapper.SysServerConfigMapper;
import com.eningqu.modules.system.service.ISysServerConfigService;

@Service
public class SysServerConfigServiceImpl extends BaseServiceImpl<SysServerConfigMapper, SysServerConfig> implements
		ISysServerConfigService {
	
	private final static String SERVER_KEY = "system:speech:server";
	
	@Autowired
    private RedisKit redisKit;

	@Override
	@Transactional
	public int add(SysServerConfig sysServerConfig) throws ServiceException {
		
		if(sysServerConfig.getAvaliable().intValue() == 1){
			baseMapper.updateAvaliableByDomain(sysServerConfig.getDomain(), 2,sysServerConfig.getType());
		}
		
		Integer result = baseMapper.insert(sysServerConfig);
		//权重值判断 如果当前主机记录存在 则新增主机记录的权重值统一修改为最新添加的权重记录
		if(result > 0){
			baseMapper.updatePriorityByDomain(sysServerConfig.getDomain(), sysServerConfig.getPriority());
		}
		//枚举
		List<SysServerConfig> sysServer = baseMapper.selectOptimalRecords();
		if(!CollectionUtils.isEmpty(sysServer)){
			
//			List<SysServerCOnfigVO> list = new ArrayList<SysServerCOnfigVO>();
//			sysServer.forEach(sys -> {
//				SysServerCOnfigVO sysServerCOnfigVO = new SysServerCOnfigVO();
//				BeanUtils.copyProperties(sys, sysServerCOnfigVO);
//				list.add(sysServerCOnfigVO);
//			});
//			boolean flag =  redisKit.set(SERVER_KEY, JSON.toJSONString(list));
//			if(!flag){
//			    throw new ServiceException("新增主机记录失败");
//			}
		}
		return result;
	}

	@Override
	public int update(SysServerConfig sysServerConfig) throws ServiceException {
		Integer result = baseMapper.updateById(sysServerConfig);
		return result;
	}

	@Override
	public int delById(Long id) throws ServiceException {
		Integer result = baseMapper.deleteById(id);
		return result;
	}

	@Override
	public List<SysServerConfig> selectOptimalRecords() throws ServiceException {
		List<SysServerConfig> sysServerConfig =  baseMapper.selectOptimalRecords();
		if(CollectionUtils.isEmpty(sysServerConfig)){
			throw new ServiceException("获取最优服务器信息失败");
		}
		return sysServerConfig;
	}

	@Override
	public List<SysServerConfig> selectList() throws ServiceException {
		EntityWrapper<SysServerConfig> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id", "domain","type","version", "priority","avaliable");
        wrapper.orderBy("priority", true);
        List<SysServerConfig> sysList = baseMapper.selectList(wrapper);
        return sysList;
	}

	@Override
	public boolean check(String domain, Integer version,Integer type) throws ServiceException {
		Integer status = baseMapper.selectByUniqueKey(domain, version,type);
		if(status > 0){
			return true;
		}
		return false;
	}
	@Override
	public boolean checkRepeated(String domain,Integer priority) throws ServiceException {
		Integer status = baseMapper.selectRepeatedRecord(domain, priority);
		if(status > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkVersionAvaliable(String domain,Integer type) throws ServiceException {
		Integer avaliable = 1;
		Integer result = baseMapper.selectVersionAvaliable(domain, avaliable,type);
		if(result > 0){
			return true;
		}
		return false;
	}

	@Override
	public SysServerConfig selectById(Long id) throws ServiceException {
		SysServerConfig sysServerConfig = baseMapper.selectById(id);
		return sysServerConfig;
	}
	
}
