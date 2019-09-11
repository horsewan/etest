package com.eningqu.modules.system.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.enums.SysUserTypeEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.kit.PasswordKit;
import com.eningqu.domain.api.AppInfo;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.api.service.IBleService;
import com.eningqu.modules.api.service.IPkgService;
import com.eningqu.modules.api.service.IUserService;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.modules.system.vo.MenuVo;
import com.eningqu.modules.system.vo.ShiroUser;
import com.eningqu.modules.system.vo.UserRolesVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.codec.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  系统用户controller
 * @since 2018/5/23 19:22
 **/
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPkgService pkgService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IBleService bleService;

    @Autowired
    private IUserService userService;



    @RequiresPermissions("sys:user:list")
    @GetMapping("")
    public String list() {
        return "sys/user/user_list";
    }

    @GetMapping("/page")
    @RequiresPermissions(value = {"sys:user:add", "sys:user:edit"}, logical = Logical.OR)
    public String page(@RequestParam(required = false) Long id, Model model) {
        List<UserRolesVO> roleJson = sysUserService.selectRolesForUser(id);
        model.addAttribute("roleJson", roleJson);
        return "sys/user/user_page";
    }

    /**
     * TODO 查询用户数据列表
     *
     * @param page
     * @param limit
     * @param loginName
     * @return
     */
    @GetMapping(value = "/dataTable")
    @ResponseBody
    @Log("查询用户数据列表")
    @RequiresPermissions("sys:user:list")
    public DataTable<SysUser> dataTable(@RequestParam int page,
                                        @RequestParam int limit,
                                        @RequestParam(required = false) String loginName,
                                        @RequestParam(required = false) String phone) {

        Map<String, Object> searchParams = Maps.newHashMap();
        if (loginName != null && !"".equals(loginName)) {
            searchParams.put(SearchParam.SEARCH_EQ + "login_name", loginName);
        }
        if (phone != null && !"".equals(phone)) {
            searchParams.put(SearchParam.SEARCH_EQ + "phone", phone);
        }
        searchParams.put(SearchParam.SEARCH_EQ + "user_type", SysUserTypeEnum.ORDINARY.getValue());

        DataTable<SysUser> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        dataTable.setFields(new String[]{"id", "login_name", "name", "sex", "phone", "is_disable", "del_status"});

        sysUserService.pageSearch(dataTable);

        return dataTable;
    }

    /**
     * TODO 添加系统用户
     *
     * @param sysUser
     * @param ids
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("sys:user:add")
    @Log("添加客服总监用户")
    public RJson add(SysUser sysUser, @RequestParam("ids[]") List<Long> ids) {
        if (!Validator.isMobile(sysUser.getPhone())) {
            logger.error("mac地址：{}格式错误", sysUser.getPhone());
            throw new AipenException("客服总监手机号格式错误");
        }
        //TODO 判
        //TOODO 判断当前添加手机号是否已存在
        SysUser tempSysUser = sysUserService.selectByPhone(sysUser.getPhone());
        if (tempSysUser != null) {
            throw new AipenException("客服总监用户已存在不可重复添加");
//            return RJson.ok("市级代理用户已存在不可重复添加");
        }

        AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(sysUser.getPhone());
        if (tempAppInfo2 != null) {
            throw new AipenException("该用户已是客服经理不可重复授权");
        }

        BleDevice bleDevice = bleService.getBleDeviceByPhone(sysUser.getPhone());
        if (bleDevice != null) {
            throw new AipenException("该用户已是客服不可重复授权");
        }

        try {
            sysUserService.insertUser(sysUser, ids);
        } catch (ServiceException e) {
            logger.error("", e);
            throw new AipenException("客服总监用户添加异常");
        }
        return RJson.ok("市客服总监用户添加成功");
    }

    /**
     * TODO 修改系统用户信息
     *
     * @param sysUser
     * @param ids
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @Log("修改客服总监用户")
    public RJson edit(SysUser sysUser) {
        //TODO 手机号为空则为解除绑定
        String sysUserPhone = sysUser.getPhone();
        if (sysUserPhone != null && !"".equals(sysUserPhone)) {
            //TODO 手机号合法性，在验证是否为平台用户
            if (!Validator.isMobile(sysUser.getPhone())) {
                logger.error("mac地址：{}格式错误", sysUser.getPhone());
                return RJson.error("客服总监手机号格式错误");
            }
            //TODO 判断是否为普通用户
            UserInfo userInfo = userService.getUserInfoByPhone(sysUserPhone);
            if (userInfo == null) {
                return RJson.error("该用户还不是平台用户，不可授权");
            }
            //TODO 判断当前添加手机号是否已存在
            SysUser tempSysUser = sysUserService.selectByPhone(sysUserPhone);
            if (tempSysUser != null) {
                return RJson.error("客服总监用户已存在不可重复添加");
            }

            AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(sysUserPhone);
            if (tempAppInfo2 != null) {
                return RJson.error("该用户已是客服经理不可重复授权");
            }

            BleDevice bleDevice = bleService.getBleDeviceByPhone(sysUserPhone);
            if (bleDevice != null) {
                return RJson.error("该用户已是客服不可重复授权");
            }
            try {
                if(StringUtils.isEmpty(sysUser.getIsDisable())){
                    sysUser.setIsDisable("N");
                }
                sysUserService.updateUserPhone(sysUser);
            } catch (ServiceException e) {
                logger.error("", e);
                return RJson.error("客服总监用户修改异常");
            }
        }else{
            sysUserService.updateUser(sysUser.getId());
        }
        return RJson.ok("客服总监用户修改成功");
    }

    /**
     * TODO 删除系统用户
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys:user:delete")
    @Log("删除系统用户信息")
    public RJson delete(@PathVariable("id") Long id) {
        sysUserService.deleteUser(id);
        return RJson.ok("系统用户删除成功");
    }

    /**
     * TODO 根据系统用户ID查询权限菜单
     *
     * @return
     */
    @GetMapping("/loadMenus")
    @ResponseBody
    public RJson loadMenus() {
        List<MenuVo> meuns = sysUserService.selectMenusForUser();
        return RJson.ok().setData(meuns);
    }

    /**
     * TODO 个人资料信息
     *
     * @param model
     * @return
     */
    @GetMapping("/user-info")
    public String userInfo(Model model) {
        SysUser sysUser = sysUserService.selectById(ShiroKit.id());
        model.addAttribute("user", sysUser);
        return "sys/user/user_info";
    }

    /**
     * TODO 更新个人资料信息
     *
     * @return
     */
    @PostMapping("/user-info")
    @ResponseBody
    @Log("修改个人资料")
    public RJson userInfo(SysUser sysUser) {
        if (!sysUserService.updateById(sysUser)) {
            return RJson.error("用户信息保存失败");
        }
        return RJson.ok("用户信息保存成功");
    }

    /**
     * TODO 修改密码
     *
     * @return
     */
    @GetMapping("/pwd")
    public String pwd() {
        return "sys/user/user_pwd";
    }

    /**
     * TODO 修改个人资料信息
     *
     * @return
     */
    @PostMapping("/pwd")
    @ResponseBody
    @Log("修改密码")
    public RJson pwd(@RequestParam("oldPwd") String oldPwd,
                     @RequestParam("newPwd") String newPwd,
                     @RequestParam("checkPassword") String checkPassword) {
        SysUser sysUser = sysUserService.selectById(ShiroKit.id());
        byte[] salt = Hex.decode(sysUser.getLoginPwd().substring(0, 16));
        if(!Objects.equals(newPwd,checkPassword)){
            return RJson.error("两次密码输入不一致!");
        }
        if (!StrUtil.equalsIgnoreCase(PasswordKit.entrypt(oldPwd, salt), sysUser.getLoginPwd())) {
            return RJson.error("原密码不正确");
        }
        sysUser.setLoginPwd(PasswordKit.entrypt(newPwd));
        if (!sysUserService.updateById(sysUser)) {
            return RJson.error("密码修改失败");
        }
        return RJson.ok("用户密码修改成功");
    }

    /**
     * TODO 重置密码
     *
     * @param userId
     * @return
     */
    @GetMapping("/reset/{userId}")
    @ResponseBody
    @RequiresPermissions("sys:user:reset")
    public RJson resetPwd(@PathVariable Long userId) {
        ShiroUser shiroUser = ShiroKit.loginInfo();
        if (!StrUtil.equalsIgnoreCase(SysUserTypeEnum.SUPER.getValue(), shiroUser.getUserType())) {
            return RJson.error("必须是超级管理员才能给用户重置密码");
        }
        SysUser sysUser = sysUserService.selectById(userId);
        sysUser.setLoginPwd(PasswordKit.entrypt(Global.DEFAULT_PWD));
        sysUser.updateById();
        return RJson.ok("密码重置成功");

    }
}
