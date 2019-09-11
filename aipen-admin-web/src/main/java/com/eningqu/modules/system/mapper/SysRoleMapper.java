package com.eningqu.modules.system.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.system.SysRole;
import com.eningqu.modules.system.vo.RoleMenusVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @desc TODO  系统角色DAO接口
 * @author     Yanghuangping
 * @date       2018/4/18 17:24
 * @version    1.0
 *
 **/
public interface SysRoleMapper extends CrudDao<SysRole> {

    /**
     * TODO 根据用户ID查询角色菜单权限
     * @param id
     * @return
     */
    List<RoleMenusVO> selectRoleMenus(@Param("id") Long id, @Param("delStatus") String delStatus);

    /**
     * TODO 根据角色ID删除权限菜单
     * @param id
     */
    void delRoleMenus(@Param("id") Long id);

    /**
     * TODO 批量插入权限菜单
     * @param roleId
     * @param menuIds
     */
    void batchInsertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * TODO 批量删除角色关联的菜单
     * @param id
     */
    void batchDeleteRoleMenus(@Param("id") Long id);
}
