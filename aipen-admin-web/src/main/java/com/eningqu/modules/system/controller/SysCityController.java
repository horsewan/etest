package com.eningqu.modules.system.controller;

import com.eningqu.common.annotation.Log;
import com.eningqu.common.exception.AipenException;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.system.service.ISysCityService;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.domain.system.SysCity;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  城市controller
 * @author     Yanghuangping
 * @since      2018/5/23 19:22
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/sys/city")
public class SysCityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysCityService sysCityService;
    @Autowired
    private ISysUserService sysUserService;

    @GetMapping("")
    @RequiresPermissions("sys:city:list")
    public String list(){
        return "sys/city/city_list";
    }

    @GetMapping("/page")
    @RequiresPermissions(value = {"sys:city:add","sys:city:edit"}, logical = Logical.OR)
    public String page(){
        return "sys/city/city_page";
    }

    @GetMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions("sys:city:list")
    @Log("查询城市列表")
    public DataTable<SysCity> dataTable(@RequestParam int page,
                                        @RequestParam int limit,
                                        @RequestParam(required = false) String shengName,
                                        @RequestParam(required = false) String cityName,
                                        @RequestParam(required = false) String platformCode){
        Map<String, Object> searchParams = Maps.newHashMap();
        if(shengName!=null&&!"".equals(shengName)){
            searchParams.put(SearchParam.SEARCH_LIKE + "sheng_name", shengName);
        }
        if(cityName!=null&&!"".equals(cityName)){
            searchParams.put(SearchParam.SEARCH_LIKE + "city_name", cityName);
        }
        if(platformCode!=null&&!"".equals(platformCode)){
            searchParams.put(SearchParam.SEARCH_LIKE + "agent_no", platformCode);
        }

        DataTable<SysCity> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        dataTable.setFields(new String[]{"id","sheng_no","sheng_name","city_no","city_name","agent_no"});
        dataTable.sortAsc("city_no");
        sysCityService.pageSearch(dataTable);

        return dataTable;
    }

    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("sys:city:add")
    @Log("添加城市")
    public RJson add(SysCity sysCity){

        if(sysCity==null){
            throw new AipenException("参数异常");
        }
        sysCity.setAgentNo(sysCity.getAgentNo().trim());
        sysCityService.insert(sysCity);
        return RJson.ok("城市添加成功");
    }

    /**
     * TODO 编辑城市
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("sys:city:edit")
    @Log("修改城市")
    public RJson edit(SysCity sysCity){
        if(sysCity==null){
            return RJson.error("参数错误");
        }
        //检查是否存在该市级代理编号，A10000前两位，如果有=则进行截取后查询
        String agentNo = sysCity.getAgentNo().toUpperCase();

        if(agentNo!=null&&!"".equals(agentNo)){
            sysCity.setAgentNo(agentNo.toUpperCase());
            if(agentNo.contains("=")){
                String[] agentNoArr = agentNo.split("=");
                List<String> agentNoArrCheck = new ArrayList<>();
                for (int i=0;i<agentNoArr.length;i++){
                    if(agentNoArr[i]!=null&&!"".equals(agentNoArr[i])){
                        SysUser sysUser = sysUserService.selectByLoginNameSplit(agentNoArr[i]+"0000");
                        if(sysUser==null){
                            agentNoArrCheck.add(agentNoArr[i]);
                        }
                    }
                }
                if(agentNoArrCheck.size()>0){
                    StringBuffer sbAgentNo = new StringBuffer();
                    for (String str:agentNoArrCheck) {
                        sbAgentNo.append(str+"/");
                    }
                    return RJson.error("["+sbAgentNo.toString()+"]平台编号不存在");
                }
            }else{
                //当个查询
//                String agentNoSplit = agentNo.substring(0,2);
                SysUser sysUser = sysUserService.selectByLoginNameSplit(agentNo.trim()+"0000");
                if(sysUser==null){
                    return RJson.error("平台编号不存在");
                }
            }
        }

        sysCityService.updateById(sysCity);
        return RJson.ok("修改成功");
    }

    @PostMapping("/edit/agentNo/{id}")
    @ResponseBody
    @RequiresPermissions("sys:city:edit")
    @Log("修改城市")
    public RJson editAgentNo(@PathVariable("id") Long id){
        if(id==null){
            return RJson.error("参数错误");
        }

        SysCity sysCity = sysCityService.selectById(id);
        if(sysCity==null){
            return RJson.error("参数错误");
        }
        sysCity.setAgentNo(" ");
        sysCityService.updateById(sysCity);
        return RJson.ok("撤销成功");
    }

    /**
     * TODO 删除城市
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys:city:delete")
    @Log("删除城市")
    public RJson delete(@PathVariable("id") Long id){
        if(!sysCityService.deleteById(id)){
            throw new AipenException("删除平台编号失败");
        }
        return RJson.ok("删除平台编号成功");
    }

}
