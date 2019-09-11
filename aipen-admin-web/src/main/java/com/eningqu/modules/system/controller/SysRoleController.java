package com.eningqu.modules.system.controller;

import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysRole;
import com.eningqu.modules.system.service.ISysRoleService;
import com.eningqu.modules.system.vo.RoleMenusVO;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  系统角色controller
 * @author     Yanghuangping
 * @since      2018/5/23 19:22
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysRoleService sysRoleService;

    @GetMapping("")
    @RequiresPermissions("sys:role:list")
    public String list(){
        return "sys/role/role_list";
    }

    @GetMapping("/page")
    @RequiresPermissions(value = {"sys:role:add","sys:role:edit"}, logical = Logical.OR)
    public String page(@RequestParam Long id, Model model){
        // 加载权限
        List<RoleMenusVO> lists = sysRoleService.selectRoleMenus(id);
        model.addAttribute("treeJson", lists);
        return "sys/role/role_page";
    }


    /**
     * TODO 查询角色数据列表
     * @param page
     * @param limit
     * @param roleName
     * @param roleCode
     * @return
     */
    @GetMapping(value = "/dataTable")
    @ResponseBody
    @RequiresPermissions("sys:role:list")
    @Log("查询角色数据列表")
    public DataTable<SysRole> dataTable(@RequestParam int page,
                                       @RequestParam int limit,
                                       @RequestParam(required = false) String roleName,
                                       @RequestParam(required = false) String roleCode){

        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_RLIKE + "role_name", roleName);
        searchParams.put(SearchParam.SEARCH_EQ + "role_code", roleCode);

        DataTable<SysRole> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        sysRoleService.pageSearch(dataTable);

        return dataTable;
    }


    /**
     * TODO 添加角色
     * @param sysRole
     * @param ids
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("sys:role:add")
    @Log("添加角色")
    public RJson add(SysRole sysRole,
                     @RequestParam("ids[]") List<Long> ids){
        try {
            sysRoleService.insertRole(sysRole, ids);
        } catch (ServiceException e) {
            logger.error("", e);
            throw new AipenException("角色添加失败");
        }
        return RJson.ok("角色添加成功");
    }

    /**
     * TODO 修改角色
     * @param sysRole
     * @param ids
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("sys:role:add")
    @Log("修改角色")
    public RJson edit(SysRole sysRole,
                      @RequestParam("ids[]") List<Long> ids){
        try {
            sysRoleService.updateRole(sysRole, ids);
        } catch (ServiceException e) {
            logger.error("", e);
            throw new AipenException("角色修改失败");
        }
        return RJson.ok("角色修改成功");
    }

    /**
     * TODO 删除角色
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys:role:delete")
    @Log("删除角色")
    public RJson delete(@PathVariable("id") Long id){
        try {
            sysRoleService.delRole(id);
        } catch (ServiceException e) {
            logger.error("", e);
            throw new ServiceException("删除角色失败");
        }
        return RJson.ok("角色删除成功");
    }
}
