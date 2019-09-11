package com.eningqu.modules.system.controller;

import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysMenu;
import com.eningqu.modules.system.service.ISysMenuService;
import com.eningqu.modules.system.vo.MenuSelectVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @desc TODO  系统菜单controller
 * @author     Yanghuangping
 * @since      2018/5/16 15:56
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {

    private Logger logger = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private RedisKit redisKit;
    @Autowired
    private ISysMenuService sysMenuService;

    @GetMapping("")
    @RequiresPermissions("sys:menu:list")
    public String list(){
        return "sys/menu/menu_list";
    }

    @GetMapping("/add")
    @RequiresPermissions("sys:menu:add")
    public String add(Model model){
        List<MenuSelectVO> list = sysMenuService.queryMenuSelects();
        model.addAttribute("selectJson", list);
        return "sys/menu/menu_add";
    }


    @GetMapping("/edit")
    @RequiresPermissions("sys:menu:edit")
    public String edit(){
        return "sys/menu/menu_edit";
    }

    /**
     * TODO 加载权限菜单列表
     * @return
     */
    @GetMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions("sys:menu:list")
    @Log("查询权限菜单列表")
    public RJson dataTable(){
        List<SysMenu> menus = sysMenuService.selectMenus();
        return RJson.ok().setData(menus);
    }

    /**
     * TODO 新增菜单信息
     * @param sysMenu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("sys:menu:add")
    @Log("新增权限菜单")
    public RJson add(SysMenu sysMenu){
        try {
            sysMenu.setDelStatus(StatusEnum.YES.getValue());
            if(!sysMenuService.insert(sysMenu)){
                throw new AipenException("权限菜单添加异常");
            }
//            clearCache();
            return RJson.ok("权限菜单添加成功");
        } catch (ServiceException e) {
            logger.error("", e);
            throw new AipenException("权限菜单添加异常");
        }
    }

    /**
     * TODO修改菜单信息
     * @param sysMenu
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("sys:menu:edit")
    @Log("修改权限菜单")
    public RJson edit(SysMenu sysMenu){
        try {
            if(!sysMenuService.updateById(sysMenu)){
                throw new AipenException("权限菜单修改异常");
            }
//            clearCache();
            return RJson.ok("权限菜单修改成功");
        } catch (ServiceException e) {
            logger.error("", e);
            throw new AipenException("权限菜单修改失败");
        }
    }

    /**
     * TODO 删除菜单
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:menu:delete")
    @Log("删除权限菜单")
    public RJson delete(@RequestParam("ids[]") List<Long> ids){
        try {
            sysMenuService.delMenus(ids);
//            clearCache();
            return RJson.ok("菜单删除成功");
        } catch (ServiceException e) {
            logger.error("", e);
            throw new AipenException("菜单删除失败");
        }
    }

//    /**
//     * TODO 清除缓存
//     */
//    private void clearCache(){
//        redisKit.delKeys(Global.REDIS_PERMS_PREFIX + "*");
//        redisKit.delKeys(Global.REDIS_MENUS_PREFIX + "*");
//    }

}
