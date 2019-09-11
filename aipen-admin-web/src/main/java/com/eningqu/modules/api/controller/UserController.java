package com.eningqu.modules.api.controller;

import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.modules.system.vo.ShiroUser;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.domain.api.SysAPIInfo;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.modules.api.service.ISysAPIService;
import com.eningqu.modules.api.service.IUserService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 * @desc TODO  用户信息controller
 * @author     Yanghuangping
 * @since      2018/5/18 16:19
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/nq/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;
    @Autowired
    private ISysAPIService sysAPIService;
    /**
     * TODO 用户信息列表页面
     * @return
     */
    @GetMapping("")
    @RequiresPermissions("nq:user:list")
    public String list(){
        return "api/user/user_list";
    }

    /**
     * TODO 用户信息列表页面
     * @return
     */
    @GetMapping("/errorUserList")
    @RequiresPermissions("nq:user:errorList")
    public String errorUserList(){
        return "api/user/user_error_list";
    }

    /**
     * TODO 用户信息新增
     * @return
     */
    @GetMapping("/page")
    public String add(){
        return "api/user/user_page";
    }

    @GetMapping(value = "/dataTable")
    @ResponseBody
    @RequiresPermissions("nq:user:list")
    @Log("查询用户信息列表")
    public DataTable<UserInfo> dataTable(@RequestParam int page,
                                         @RequestParam int limit,
                                         @RequestParam(required = false) String phone,
                                         @RequestParam(required = false) String nickName){
        DataTable<UserInfo> dataTable = new DataTable();
        //TODO 市级代理根据市级代理下的所有
        ShiroUser shiroUser = null;
        if(ShiroKit.isLogin()){
            shiroUser = ShiroKit.loginInfo();
        }else{
            return dataTable;
        }
        //TODO获取点前登录用户类型，市级代理编号前两位，比如：A10000-->A1
        String loginName = shiroUser.getLoginName();
        Map<String, Object> searchParams = Maps.newHashMap();
        if("O".equals(shiroUser.getUserType())){
            //判断是否是该市级代理下的个代
            if(nickName!=null&&!"".equals(nickName)){
                boolean isAgentSub = nickName.substring(0,2).equals(loginName.substring(0,2))?true:false;
                if(!isAgentSub){
                    return dataTable;
                }
            }else{
                searchParams.put(SearchParam.SEARCH_RLIKE + "agent_no",loginName.substring(0,2));
            }
        }
        if(nickName!=null&&!"".equals(nickName)){
            searchParams.put(SearchParam.SEARCH_EQ + "agent_no",nickName);
        }
        searchParams.put(SearchParam.SEARCH_EQ + "mobile",phone);
        //TODO Y
//        searchParams.put(SearchParam.SEARCH_EQ + "del_status","Y");

        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        userService.pageSearch(dataTable);

        return dataTable;
    }

    @GetMapping(value = "/dataTableError")
    @ResponseBody
    @RequiresPermissions("nq:user:errorList")
    @Log("查询用户信息列表")
    public DataTable<UserInfo> dataTableError(@RequestParam int page,
                                         @RequestParam int limit,
                                         @RequestParam(required = false) String phone,
                                         @RequestParam(required = false) String nickName){
        DataTable<UserInfo> dataTable = new DataTable();
        //TODO 市级代理根据市级代理下的所有
        ShiroUser shiroUser = null;
        if(ShiroKit.isLogin()){
            shiroUser = ShiroKit.loginInfo();
        }else{
            return dataTable;
        }
        //TODO获取点前登录用户类型，市级代理编号前两位，比如：A10000-->A1
        String loginName = shiroUser.getLoginName();
        Map<String, Object> searchParams = Maps.newHashMap();
        if(nickName!=null&&!"".equals(nickName)){
            searchParams.put(SearchParam.SEARCH_EQ + "agent_no",nickName);
        }
        searchParams.put(SearchParam.SEARCH_EQ + "mobile",phone);
        //TODO Y
        searchParams.put(SearchParam.SEARCH_ISNULL + "address_x","");
        searchParams.put(SearchParam.SEARCH_ISNULL + "address_y","");

        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        userService.pageSearch(dataTable);

        return dataTable;
    }

    @GetMapping("/del/{uid}")
    @ResponseBody
    public RJson del(@PathVariable Long uid){
        userService.deleteByUid(uid);
        return RJson.ok("删除成功");
    }

    @GetMapping("/updateStaId/{uid}/{delStatus}")
    @ResponseBody
    public RJson updateStaId(@PathVariable Long uid,@PathVariable String delStatus){
        if(uid<0){
            return RJson.ok("用户参数错误");
        }
        if(delStatus==null||"".equals(delStatus)){
            return RJson.ok("用户参数错误");
        }
        if("Y".equals(delStatus)){
            userService.updateDelStatus(uid,"N");
            return RJson.ok("关闭禁用成功");
        }else{
            userService.updateDelStatus(uid,"Y");
            return RJson.ok("开启禁用成功");
        }
    }


    //解禁/开启所有用户（API系统解禁/开启）
    @PostMapping("/sysapi")
    @ResponseBody
    public RJson updateSysAPI(){
        //总开关
        boolean bool = false;
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if(sysAPIInfo!=null){
            if("N".equals(sysAPIInfo.getSysApi())){
                sysAPIInfo.setSysApi("Y");//禁用
                sysAPIInfo.setSysUserReg("Y");
                bool = false;
            }else{
                sysAPIInfo.setSysApi("N");//开启
                sysAPIInfo.setSysUserReg("N");
                bool = true;
            }
            sysAPIService.updateById(sysAPIInfo);
        }
        if(bool){
            return RJson.ok("关闭解禁成功");
        }else{
            return RJson.ok("开启解禁成功");
        }
    }

    @PostMapping("/userRegSta")
    @ResponseBody
    public RJson updateUserRegBySta(){
        //总开关
        boolean bool = false;
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if(sysAPIInfo!=null){
            if("N".equals(sysAPIInfo.getSysUserReg())){
                sysAPIInfo.setSysUserReg("Y");
                bool = false;
            }else{
                sysAPIInfo.setSysUserReg("N");
                bool = true;
            }
            sysAPIService.updateById(sysAPIInfo);
        }
        if(bool){
            return RJson.ok("关闭解禁成功");
        }else{
            return RJson.ok("开启解禁成功");
        }
    }

    @GetMapping("/getSysAPI")
    @ResponseBody
    public RJson getSysAPI(){
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if(sysAPIInfo==null){
            return  RJson.error();
        }
        return  RJson.ok().setData(sysAPIInfo);
    }
}
