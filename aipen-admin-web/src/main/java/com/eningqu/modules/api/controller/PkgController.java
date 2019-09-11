package com.eningqu.modules.api.controller;

import cn.hutool.core.lang.Validator;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.modules.system.vo.ShiroUser;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.domain.api.AppInfo;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.api.service.IBleService;
import com.eningqu.modules.api.service.IPkgService;
import com.eningqu.modules.api.service.IUserService;
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
import java.util.Objects;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  应用信息Controller
 * @since 2018/11/20 16:13
 **/
@Controller
@RequestMapping("/nq/pkg")
public class PkgController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    //    private final static String SPEECH_APPPKG_KEY = "check:appPkg:%1$s";
//    private final static String SPEECH_APPSIGN_KEY = "check:appSign:%1$s";
    @Autowired
    private IPkgService pkgService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IBleService bleService;

    @Autowired
    private IUserService userService;


    @GetMapping("/page")
    @RequiresPermissions(value = {"nq:ble:list", "nq:device:list"}, logical = Logical.OR)
    public String pkgPage(@RequestParam(required = false, defaultValue = "0") Long appId, Model model) {
//        List<LanguageVO> languageList = pkgService.querySelectedLanguage(appId);
//        model.addAttribute("languageList", languageList);
        return "api/pkg/pkg_page";
    }

    //TODO appPkg需要截取前面两个字符作为like查询的调价，比如：A10000-->A1%作为查询条件。
    @GetMapping("/dataTable/{type}")
    @ResponseBody
    @RequiresPermissions(value = {"nq:ble:list", "nq:device:list"}, logical = Logical.OR)
    @Log("客服经理信息列表查询")
    public DataTable<AppInfo> pkgDataTable(@PathVariable String type,
                                           @RequestParam int page,
                                           @RequestParam int limit,
                                           @RequestParam(required = false) String appPkg,
                                           @RequestParam(required = false) String phone) {

        //TODO 获取当前登录用户标识，代理则是代理编号
        ShiroUser shiroUser = null;
        if (ShiroKit.isLogin()) {
            shiroUser = ShiroKit.loginInfo();
        } else {
            return null;
        }
        String loginName = shiroUser.getLoginName();

        Map<String, Object> searchParams = Maps.newHashMap();
        if ("O".equals(shiroUser.getUserType())) {
            searchParams.put(SearchParam.SEARCH_RLIKE + "app_pkg", loginName.substring(0, 2));
        }

        searchParams.put(SearchParam.SEARCH_EQ + "app_pkg", appPkg);
        searchParams.put(SearchParam.SEARCH_EQ + "signature", phone);
//        searchParams.put(SearchParam.SEARCH_EQ + "app_type", type);

        DataTable<AppInfo> dataTable = new DataTable<>();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        pkgService.pageSearch(dataTable);
//        List<AppInfo> list = dataTable.getData();
//        for(AppInfo appInfo:list){
//            redisKit.set( String.format( SPEECH_APPPKG_KEY, appInfo.getAppPkg()),appInfo.getSignature() );
//            redisKit.set( String.format( SPEECH_APPSIGN_KEY, appInfo.getSignature()),appInfo.getAppPkg() );
//        }
        return dataTable;
    }

    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions(value = {"nq:ble:add", "nq:device:add"}, logical = Logical.OR)
    @Log("新增客服经理信息")
    public RJson add(AppInfo appInfo) {

        //TODO 判断是否已经添加该团代，根据团代编号进行查询
        AppInfo tempAppInfo = pkgService.getAppInfoByPkg(appInfo.getAppPkg());
        if (tempAppInfo != null) {
            throw new AipenException("该客服经理信息已存在");
        }

        //TODO 判断phone是否已经为其它代理（市级代理、团代、个代）
        SysUser tempSysUser = sysUserService.selectByPhone(appInfo.getSignature());
        if (tempSysUser != null) {
            return RJson.error("该用户已是客服总监不可重复授权");
        }

        AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(appInfo.getSignature());
        if (tempAppInfo2 != null) {
            return RJson.error("该用户已是客服经理不可重复授权");
        }

        BleDevice bleDevice = bleService.getBleDeviceByPhone(appInfo.getSignature());
        if (bleDevice != null) {
            return RJson.error("该用户已是客服不可重复授权");
        }

//        appInfo.setSignature(DigestUtil.md5Hex(appInfo.getAppPkg()));
//        appInfo.setSignature(appInfo.getAppPkg());
        appInfo.setEnableStatus(StatusEnum.YES.getValue());
        appInfo.setAppName(appInfo.getAppPkg());
        pkgService.saveAppInfo(appInfo, null);
        return RJson.ok("客服经理信息新增成功");
    }

    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions(value = {"nq:ble:edit", "nq:device:edit"}, logical = Logical.OR)
    @Log("修改客服经理信息")
    public RJson edit(AppInfo appInfo) {
//        appInfo.setSignature(DigestUtil.md5Hex(appInfo.getAppPkg()));

        //TODO 判断是否已经添加该团代，根据团代编号进行查询
        /*AppInfo tempAppInfo = pkgService.getAppInfoByPkg(appInfo.getAppPkg());
        if(tempAppInfo!=null){
            throw new AipenException("该客服经理信息已存在");
        }*/

        String tempPhone = appInfo.getSignature();
        if (tempPhone != null && !"".equals(tempPhone)) {
            if (!Validator.isMobile(tempPhone)) {
                logger.error("mac地址：{}格式错误", tempPhone);
                return RJson.error("客服经理手机号格式错误");
            }
            //TODO 判断是否为普通用户
            UserInfo userInfo = userService.getUserInfoByPhone(tempPhone);
            if (userInfo == null) {
                return RJson.error("该用户还不是平台用户，不可授权");
            }
            //TODO 判断phone是否已经为其它代理（市级代理、团代、个代）
            SysUser tempSysUser = sysUserService.selectByPhone(tempPhone);
            if (tempSysUser != null ) {
                return RJson.error("该用户已是客服总监不可重复授权");
            }

            AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(tempPhone);
            if (tempAppInfo2 != null && !tempAppInfo2.getSignature().equals(tempPhone)) {
                return RJson.error("该用户已是客服经理不可重复授权");
            }

            BleDevice bleDevice = bleService.getBleDeviceByPhone(tempPhone);

            if (bleDevice != null ) {
                return RJson.error("该用户已是客服不可重复授权");
            }
        }
//        pkgService.saveAppInfo(appInfo, null);
        pkgService.updateAppInfoPhone(appInfo.getId(), tempPhone, appInfo);
        return RJson.ok("客服经理信息修改成功");
    }

}
