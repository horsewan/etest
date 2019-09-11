package com.eningqu.modules.system.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysMenu;
import com.eningqu.modules.system.vo.MenuSelectVO;

import java.util.List;

/**
 * @desc TODO 系统菜单servie接口
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 19:43
 **/
public interface ISysMenuService extends IBaseService<SysMenu> {

    /**
     * TODO 查询菜单列表
     * @return
     */
    List<SysMenu> selectMenus();

    /**
     * TODO 获取权限父级菜单下拉列表值
     * @return
     */
    List<MenuSelectVO> queryMenuSelects();

    /***
     * TODO 删除菜单
     * @param ids
     * @throws ServiceException
     */
    void delMenus(List<Long> ids) throws ServiceException;
}
