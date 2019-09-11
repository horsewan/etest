package com.eningqu.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysDict;
import com.eningqu.modules.system.mapper.SysDictMapper;
import com.eningqu.modules.system.service.ISysDictService;
import com.eningqu.modules.system.vo.DictVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @desc TODO  字典表service实现类
 * @author     Yanghuangping
 * @date       2018/4/18 19:44
 * @version    1.0
 *
 **/
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    /**
     * TODO 查询字典列表
     *
     * @return
     */
    @Override
    public List<SysDict> selectDicts() {
        EntityWrapper<SysDict> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id", "pid", "group_code", "group_desc", "label_name", "value_code", "del_status");
        List<SysDict> sources = baseMapper.selectList(wrapper);
        List<SysDict> target = Lists.newArrayList();
        for (SysDict sysDict : sources) {
            if(sysDict.getPid().longValue() == Global.TOP_PID_ID.longValue()){
                target.add(sysDict);
                loadChildenSysMenu(sysDict.getId(), sources, target);
            }
        }
        return target;
    }

    /**
     * TODO 删除字典数据  若是删除字典组 则连同删除字典信息
     *
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean delDict(Long id) throws ServiceException{
        try {
            EntityWrapper<SysDict> wrapper = new EntityWrapper<>();
            wrapper.eq("id", id);
            wrapper.or().eq("pid", id);
            int count = baseMapper.delete(wrapper);
            if(count == 0){
                throw new ServiceException("删除数据字典失败");
            }
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    /**
     * TODO 根据字典组编码查询
     *
     * @param groupCode
     * @return
     */
    @Override
    public List<DictVO> queryDictByGroupCode(String groupCode) {
        return baseMapper.queryDicts(groupCode, StatusEnum.YES.getValue());
    }

    /**
     * TODO 添加字典组
     *
     * @param sysDict
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    public void insertDictGroup(SysDict sysDict) throws ServiceException{
        int count = baseMapper.checkGroup(sysDict.getGroupCode(), sysDict.getPid());
        if(count > 0){
            throw new ServiceException("数据字典组码已存在");
        }
        baseMapper.insert(sysDict);
    }

    /**
     * TODO 更新字典组
     *
     * @param sysDict
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    public void updateDictGroup(SysDict sysDict) throws ServiceException{
        baseMapper.updateById(sysDict);
    }

    /**
     * TODO 添加字典
     *
     * @param sysDict
     * @throws ServiceException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    public void insertDict(SysDict sysDict) throws ServiceException {
        int count = baseMapper.checkDict(sysDict.getGroupCode(), sysDict.getValueCode());
        if(count > 0){
            throw new ServiceException("数据字典码已存在");
        }
        baseMapper.insert(sysDict);
    }

    private void loadChildenSysMenu(Long pid, List<SysDict> sources, List<SysDict> target) {
        for (SysDict child : sources) {
            if(child.getPid().equals(pid)){
                target.add(child);
            }
        }
    }
}
