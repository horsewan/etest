package com.eningqu.modules.system.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.system.SysMenu;
import com.eningqu.modules.system.vo.MenuSelectVO;
import com.eningqu.modules.system.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc TODO 菜单DAO层
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 19:45
 **/
public interface SysMenuMapper extends CrudDao<SysMenu> {

    /**
     * TODO 查询用户所拥有的菜单列表 (只有目录和页面数据)
     * @param id
     * @param delStatus
     * @param menuType
     * @return
     */
    List<MenuVo> selectMenuForUser(@Param("id") Long id, @Param("delStatus") String delStatus, @Param("menuType") List<String> menuType);

    /**
     * TODO 查询父级菜单选择的下拉列表值
     * @param menuTypes
     * @return
     */
    List<MenuSelectVO> queryMenuSelects(@Param("menuTypes") List<String> menuTypes, @Param("delStatus") String delStatus);

    /**
     * TODO 更新菜单状态为删除
     * @param ids
     * @param delStatus
     */
    void updateMenusStatus(@Param("ids")List<Long> ids, @Param("delStatus")String delStatus);
}
