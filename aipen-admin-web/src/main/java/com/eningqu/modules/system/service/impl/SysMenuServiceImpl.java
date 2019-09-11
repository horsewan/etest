package com.eningqu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.enums.SysMenuTypeEnum;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysMenu;
import com.eningqu.modules.system.mapper.SysMenuMapper;
import com.eningqu.modules.system.service.ISysMenuService;
import com.eningqu.modules.system.vo.MenuSelectVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @desc TODO  系统菜单service实现类
 * @author     Yanghuangping
 * @date       2018/4/18 19:44
 * @version    1.0
 *
 **/
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

//    @Autowired
//    private RedisKit redisKit;

    /**
     * TODO 查询所有菜单列表
     *
     * @return
     */
    @Override
    public List<SysMenu> selectMenus() {
        EntityWrapper<SysMenu> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id", "pid", "title", "menu_type", "href", "perm", "sort");
        wrapper.eq("del_status", StatusEnum.YES.getValue());
        wrapper.orderBy("sort", true);
        List<SysMenu> sources = baseMapper.selectList(wrapper);
        List<SysMenu> target = Lists.newArrayList();
        for (SysMenu sysMenu : sources) {
            if(sysMenu.getPid().longValue() == Global.TOP_PID_ID.longValue()){
                target.add(sysMenu);
                loadChildNode(sysMenu.getId(), sources, target);
            }
        }
        return target;
    }

    private void loadChildNode(Long pid, List<SysMenu> sources, List<SysMenu> target) {
        for (SysMenu child : sources) {
            if(child.getPid().equals(pid)){
                if(StrUtil.equalsIgnoreCase(child.getMenuType(), SysMenuTypeEnum.PAGE.getValue())){
                    target.add(child);
                    loadChildNode(child.getId(), sources, target);
                } else if(StrUtil.equalsIgnoreCase(child.getMenuType(), SysMenuTypeEnum.BUTTON.getValue())){
                    target.add(child);
                }
            }
        }
    }

    /**
     * TODO 获取权限父级菜单下拉列表值
     *
     * @return
     */
    @Override
    public List<MenuSelectVO> queryMenuSelects() {
        return baseMapper.queryMenuSelects(Lists.newArrayList(SysMenuTypeEnum.DIRECTORY.getValue(), SysMenuTypeEnum.PAGE.getValue()),
                StatusEnum.YES.getValue());
    }

    /***
     * TODO 删除菜单
     * @param ids
     * @throws ServiceException
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void delMenus(List<Long> ids) throws ServiceException {
        try {
            baseMapper.updateMenusStatus(ids, StatusEnum.NO.getValue());
            // 清除缓存
//            redisKit.delKeys(Global.REDIS_PERMS_PREFIX + "*");
//            redisKit.delKeys(Global.REDIS_MENUS_PREFIX + "*");
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }
}
