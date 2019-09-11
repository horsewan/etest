package com.eningqu.modules.system.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.system.vo.MenuVo;
import com.eningqu.modules.system.vo.UserRolesVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  系统用户DAO接口
 * @date 2018/4/18 17:24
 **/
public interface SysUserMapper extends CrudDao<SysUser> {

    /**
     * TODO 根据用户名查询用户信息
     *
     * @param loginName 用户名
     * @param delStatus 用户名
     * @return
     */
    SysUser selectByLoginName(@Param("loginName") String loginName, @Param("delStatus") String delStatus);

    /**
     * TODO 根据phone查询用户
     *
     * @param phone
     * @param delStatus
     * @return
     */
    SysUser selectByPhone(@Param("phone") String phone, @Param("delStatus") String delStatus);

    /**
     * TODO 根据用户查询菜单权限
     *
     * @param id
     * @param delStatus
     * @return
     */
    List<String> selectPermById(@Param("id") Long id, @Param("delStatus") String delStatus);

    /**
     * TODO 根据用户ID 查询用户角色
     *
     * @param id
     * @param delStatus
     * @return
     */
    List<UserRolesVO> selectUserRoles(@Param("id") Long id, @Param("delStatus") String delStatus);

    /**
     * TODO 根据用户ID解除绑定角色
     *
     * @param id
     */
    void delUserRoles(@Param("id") Long id);

    /**
     * TODO 批量插入用户关联的角色
     *
     * @param id
     * @param roleIds
     */
    void batchInsertUserRoles(@Param("id") Long id, @Param("roleIds") List<Long> roleIds);

    /**
     * TODO 查询用户所拥有的菜单列表 (只有目录和页面数据)
     *
     * @param id
     * @param delStatus
     * @param menuType
     * @return
     */
    List<MenuVo> selectMenusForUser(@Param("id") Long id, @Param("delStatus") String delStatus, @Param("menuType") List<String> menuType);

    /**
     * TODO 更新删除状态
     *
     * @param id
     * @param delStatus
     */
    void updateDelStatus(@Param("id") Long id, @Param("delStatus") String delStatus);

    /**
     * TODO 查询超级管理员菜单
     *
     * @param status
     * @param menuType
     * @return
     */
    List<MenuVo> selectMenusForSuperAdmin(@Param("status") String status, @Param("menuType") List<String> menuType);

    /**
     * TODO 查询超级管理员权限
     *
     * @param status
     * @return
     */
    List<String> selectSuperPerm(@Param("status") String status);

    void updateUserPhone(Long id, @Param("sysUser") SysUser sysUser);

    SysUser selectByLoginNameSplit(@Param("loginName") String loginName);


}
