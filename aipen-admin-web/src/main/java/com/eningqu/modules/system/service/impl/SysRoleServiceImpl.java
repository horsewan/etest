package com.eningqu.modules.system.service.impl;

import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysRole;
import com.eningqu.modules.system.mapper.SysRoleMapper;
import com.eningqu.modules.system.service.ISysRoleService;
import com.eningqu.modules.system.vo.RoleMenusVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @desc TODO  系统角色service接口实现
 * @author     Yanghuangping
 * @date       2018/4/18 17:38
 * @version    1.0
 *
 **/
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private RedisKit redisKit;

    /**
     * 通过用户ID查询所拥有的角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> selectByUserId(Long userId) {
        return null;
    }

    /**
     * 根据用户ID查询角色菜单权限
     *
     * @param id
     * @return
     */
    @Override
    public List<RoleMenusVO> selectRoleMenus(Long id) {
        List<RoleMenusVO> sources = baseMapper.selectRoleMenus(id, StatusEnum.YES.getValue());
        List<RoleMenusVO> target = Lists.newArrayList();
        for (RoleMenusVO roleMenus : sources) {
            if(roleMenus.getPid().longValue() == 0){
                loadRoleMenus(sources, roleMenus);
                target.add(roleMenus);
            }
        }
        return target;
    }

    /**
     * TODO 删除角色以及关联的菜单
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void delRole(Long id) {
        try {
            baseMapper.deleteById(id);
            baseMapper.batchDeleteRoleMenus(id);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * TODO 添加角色及权限菜单
     *
     * @param sysRole
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void insertRole(SysRole sysRole, List<Long> ids) throws ServiceException {
        try {
            sysRole.setDelStatus(StatusEnum.YES.getValue());
            baseMapper.insert(sysRole);
            baseMapper.batchInsertRoleMenus(sysRole.getId(), ids);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }


    /**
     * TODO 修改角色及权限菜单
     *
     * @param sysRole
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void updateRole(SysRole sysRole, List<Long> ids) throws ServiceException {
        try {
            baseMapper.updateById(sysRole);
            // 先删除后添加
            baseMapper.delRoleMenus(sysRole.getId());
            baseMapper.batchInsertRoleMenus(sysRole.getId(), ids);

            // 清除缓存
            redisKit.delKeys(Global.REDIS_PERMS_PREFIX + "*");
            redisKit.delKeys(Global.REDIS_MENUS_PREFIX + "*");

        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    private void loadRoleMenus(List<RoleMenusVO> sources, RoleMenusVO roleMenusVO){
        List<RoleMenusVO> childs = Lists.newArrayList();
        for (RoleMenusVO roleMenus : sources) {
            if(roleMenus.getPid().equals(roleMenusVO.getId())){
                loadRoleMenus(sources, roleMenus);
                childs.add(roleMenus);
                roleMenusVO.setList(childs);
            }

        }
    }
}
