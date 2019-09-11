package com.eningqu.modules.api.controller;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.toolkit.IOUtils;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.exception.AipenException;
import com.eningqu.domain.api.AppInfo;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.api.service.IBleService;
import com.eningqu.modules.api.service.IPkgService;
import com.eningqu.modules.api.service.IUserService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @desc TODO  蓝牙设备Controller
 * @author     Yanghuangping
 * @since      2018/11/20 16:13
 * @version    1.0
 *
 **/

@Controller
@RequestMapping("/nq/ble")
public class BleController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPkgService pkgService;

    @Autowired
    private IBleService bleService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IUserService userService;

    @GetMapping("")
    @RequiresPermissions("nq:ble:list")
    public String list(){
        return "api/ble/ble_list";
    }


    @GetMapping("/page")
    public String page(){
        return "api/ble/ble_page";
    }

    @GetMapping("/dataTable/{appName}")
    @ResponseBody
    @RequiresPermissions("nq:ble:list")
    @Log("客服信息列表查询")
    public DataTable<BleDevice> dataTable(@PathVariable String appName,
                                          @RequestParam int page,
                                          @RequestParam int limit,
                                          @RequestParam(required = false) String mac,
                                          @RequestParam(required = false) String agentNo){
        //TODO 获取当前登录用户标识，客服则是客服编号
        /*ShiroUser shiroUser = null;
        if(ShiroKit.isLogin()){
            shiroUser = ShiroKit.loginInfo();
        }else{
            return null;
        }
        String loginName = shiroUser.getLoginName();

        Map<String, Object> searchParams = Maps.newHashMap();
        if("O".equals(shiroUser.getUserType())){
            searchParams.put(SearchParam.SEARCH_RLIKE + "app_pkg", loginName.substring(0,2));
        }*/
        //TODO BleDevice需要进行横向扩展（添加字段）
        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_RLIKE + "ausn", appName.substring(0,4));
        searchParams.put(SearchParam.SEARCH_EQ + "mac", mac);//手机号
        searchParams.put(SearchParam.SEARCH_EQ + "ausn", agentNo);//编号

        DataTable<BleDevice> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        bleService.pageSearch(dataTable);

        return dataTable;
    }


    @RequestMapping("/add")
    @ResponseBody
    @RequiresPermissions("nq:ble:add")
    @Log("新增客服信息")
    public RJson add(BleDevice bleDevice){

//        if(!Validator.isMactchRegex("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9])) d{8}$", bleDevice.getMac())){
        if(!Validator.isMobile(bleDevice.getMac())){
            logger.error("mac地址：{}格式错误", bleDevice.getMac());
            return RJson.error("客服手机号格式错误");
        }

        //TODO 判断phone是否已经为其它客服（客服总监、客服经理、客服）
        BleDevice tempBleDevice = bleService.getBleDeviceByPhone(bleDevice.getMac());
        if(tempBleDevice!=null){
            return RJson.error("该用户已是客服不可重复授权");
        }

        SysUser tempSysUser = sysUserService.selectByPhone(bleDevice.getMac());
        if(tempSysUser!=null){
            return RJson.error("该用户已是客服总监不可重复授权");
        }

        AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(bleDevice.getMac());
        if(tempAppInfo2!=null){
            return RJson.error("该用户已是客服经理不可重复授权");
        }

        if(!bleService.insert(bleDevice)){
            logger.error("客服信息新增失败");
            return RJson.error("客服信息新增失败");
        }
        return RJson.ok("客服信息新增成功");
    }

    @RequestMapping("/edit")
    @ResponseBody
    @RequiresPermissions("nq:ble:edit")
    @Log("修改蓝牙设备信息")
    public RJson edit(BleDevice bleDevice){

        String tempPhone = bleDevice.getMac();
        if(tempPhone!=null&&!"".equals(tempPhone)){
            if(!Validator.isMobile(bleDevice.getMac())){
                logger.error("mac地址：{}格式错误", tempPhone);
                return RJson.error("客服手机号格式错误");
            }
            //TODO 判断是否为普通用户
            UserInfo userInfo = userService.getUserInfoByPhone(tempPhone);
            if(userInfo==null){
                return RJson.error("该用户还不是平台用户，不可授权做客服");
            }

            //TODO 判断phone是否已经为其它代理（客服总监、客服经理、客服）
            BleDevice tempBleDevice = bleService.getBleDeviceByPhone(tempPhone);
            if(tempBleDevice!=null){
                return RJson.error("该用户已是客服不可重复授权");
            }

            SysUser tempSysUser = sysUserService.selectByPhone(tempPhone);
            if(tempSysUser!=null){
                return RJson.error("该用户已是客服总监不可重复授权");
            }

            AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(tempPhone);
            if(tempAppInfo2!=null){
                return RJson.error("该用户已是客服经理不可重复授权");
            }
        }

//        if(!bleDevice.updateById()){
//            logger.error("蓝牙设备信息修改失败");
//            throw new AipenException("蓝牙设备信息修改失败");
//        }
        bleService.updateBleDeviceByPhone(bleDevice.getId(),bleDevice);
        return RJson.ok("蓝牙设备信息修改成功");
    }

    @GetMapping("/del/{id}")
    @ResponseBody
    @RequiresPermissions("nq:ble:delete")
    @Log("删除蓝牙设备信息")
    public RJson delete(@PathVariable Long id){
        if(!bleService.deleteById(id)){
            logger.error("蓝牙设备删除失败");
            throw new AipenException("蓝牙设备删除失败");
        }
        return RJson.ok("蓝牙设备删除成功");
    }

    /**
     * TODO 更改设备使用状态
     * @param id
     * @return
     */
    @GetMapping("/status/{id}")
    @ResponseBody
    @RequiresPermissions("nq:ble:status")
    @Log("更新蓝牙设备状态")
    public RJson status(@PathVariable("id") Long id){

        if(!bleService.updateStatus(id)){
            logger.error("蓝牙设备状态更新失败");
            throw new AipenException("蓝牙设备状态更新失败");
        }
        return RJson.ok("蓝牙设备状态更新成功");
    }

    /**
     * TODO 上传文件界面
     * @param pkgId
     * @param model
     * @return
     */
    @GetMapping("/import/{pkgId}")
    public String importPage(@PathVariable Long pkgId, Model model){
        model.addAttribute("pkgId", pkgId);
        return "api/ble/ble_import";
    }

    /**
     * TODO 下载文件
     * */
    @RequestMapping("/downLoadFile")
    public void downLoadFile(HttpServletResponse response){
        response.setHeader( "Content-Disposition","attachment; filename=yyyyMMddadd87ceb6ca7458193e346d614372474.jpg" );

        try {
            FileInputStream fis = new FileInputStream( "C:\\Users\\MyPC\\AppData\\Local\\Temp\\tomcat-docbase.7406897509051050296.8080\\yyyyMMddadd87ceb6ca7458193e346d614372474.png" );
            OutputStream out = response.getOutputStream();
            int length = 0;
            byte [] bytes = new byte[4096];
            while((length=fis.read( bytes ))!=-1){
                out.write( bytes,0,length);
            }
            out.flush();
            IOUtils.closeQuietly( fis,out );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
