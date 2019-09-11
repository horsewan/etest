package com.eningqu.modules.system.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysRole;
import com.eningqu.modules.system.vo.RoleMenusVO;

import java.util.List;

/**
 * @desc TODO 系统角色service接口
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 17:21
 **/
public interface ISysRoleService extends IBaseService<SysRole> {

    /**
     * TODO 根据用户ID查询所拥有的角色
     * @param userId
     * @return
     */
    List<SysRole> selectByUserId(Long userId);

    /**
     * TODO 根据用户ID查询角色菜单权限
     * @param id
     * @return
     */
    List<RoleMenusVO> selectRoleMenus(Long id);

    /**
     * TODO 删除角色以及关联的菜单
     * @param id
     */
    void delRole(Long id) throws ServiceException;

    /**
     * TODO 添加角色及权限菜单
     * @param sysRole
     * @param ids
     */
    void insertRole(SysRole sysRole, List<Long> ids) throws ServiceException;

    /**
     * TODO 修改角色及权限菜单
     * @param sysRole
     * @param ids
     */
    void updateRole(SysRole sysRole, List<Long> ids) throws ServiceException;
}
