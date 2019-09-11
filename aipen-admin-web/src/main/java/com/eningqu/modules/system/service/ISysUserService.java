package com.eningqu.modules.system.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.system.vo.MenuVo;
import com.eningqu.modules.system.vo.UserRolesVO;

import java.util.List;
import java.util.Set;

/**
 * @desc TODO 系统用户service接口
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 15:40
 **/

public interface ISysUserService extends IBaseService<SysUser> {

    /**
     * TODO 登录用户名查询用户信息
     * @param loginName
     * @return
     */
    SysUser selectByLoginName(String loginName);

    /**
     * TODO 登录phone查询用户信息
     * @param phone
     * @return
     */
    SysUser selectByPhone(String phone);

    /**
     * TODO 根据用户ID查询权限
     * @return
     */
    Set<String> selectPermsForUser();


    /**
     * TODO 根据用户ID查询菜单
     * @return
     */
    List<MenuVo> selectMenusForUser();

    /**
     * TODO 根据用户ID查询 用户角色
     * @param id
     * @return
     */
    List<UserRolesVO> selectRolesForUser(Long id);


    /**
     * TODO 删除用户信息 （把删除状态改为 N )
     * @param id
     */
    void deleteUser(Long id) throws ServiceException;

    /**
     * TODO 添加系统用户
     * @param sysUser
     * @param ids
     */
    void insertUser(SysUser sysUser, List<Long> ids) throws ServiceException;

    /**
     * TODO 修改系统用户
     * @param sysUser
     * @param ids
     */
    void updateUser(Long ids) throws ServiceException;

    void updateUserPhone(SysUser sysUser);

    SysUser selectByLoginNameSplit(String loginName);
}
