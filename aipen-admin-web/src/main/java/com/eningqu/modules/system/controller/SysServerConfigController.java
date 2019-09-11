package com.eningqu.modules.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.groups.Default;

import com.eningqu.common.kit.RedisKit;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.core.bean.BeanUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.base.vo.ValidJson;
import com.eningqu.common.kit.ValidationKit;
import com.eningqu.domain.system.SysServerConfig;
import com.eningqu.modules.system.params.SysServerConfigParams;
import com.eningqu.modules.system.service.ISysServerConfigService;


@Controller
@RequestMapping("/sys/server")
public class SysServerConfigController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private HashMap<String,Object> redisHashMap = new HashMap<String, Object>();
	
	@Autowired
	private ISysServerConfigService sysServerConfigService;
	@Autowired
    RedisKit redisKit;
	
	@PostMapping("/getRedisKeySet")
	@ResponseBody
	public RJson getRedisKeySet(){
		Set<String> keySet = redisKit.getKeys("*");
		keySet.stream().forEach(key -> {
			try{
				redisHashMap.put(key, redisKit.get(key));
			}catch(Exception e){
				logger.error("key="+key+";message="+e.getMessage());
			}
		});
		logger.info(JSONObject.toJSONString(redisHashMap));
		return RJson.ok().setData(JSONObject.toJSONString(redisHashMap));
	}
	
	@PostMapping("/addAppServer")
	@RequiresPermissions(value = {"nq:ble:server", "nq:device:server"}, logical = Logical.OR)
    @ResponseBody
    public RJson addAppServer(@RequestBody JSONObject object){
		Long appId = object.getLong("appId");
		String pkgName = object.getString("pkgName");
		JSONArray arr = object.getJSONArray("serverIds");
		List<Integer> serverIds = new ArrayList<Integer>();
		for(int i=0;i<arr.size();i++){
			serverIds.add(arr.getInteger(i));
		}
    	//查看是否是最优版本 最优版本不允许删除
//    	Integer result = nqSpeechAppServerService.add(appId, serverIds,pkgName);
//    	if(result > 0){
//    		return RJson.ok("保存成功！");
//    	}
    	return RJson.error("保存失败！");
    }
	
	/**
	 * 应用语音服务器列表配置
	 * @param appId
	 * @param model
	 * @return
	 */
    @GetMapping("/app-server-page/{appId}")
    @RequiresPermissions(value = {"nq:ble:server", "nq:device:server"}, logical = Logical.OR)
    public String appMapServer(@PathVariable Long appId, Model model){
        //查询主机记录
//    	List<NqSpeechAppServer> list = nqSpeechAppServerService.selectByAppId(appId);
    	model.addAttribute("list",null);
    	model.addAttribute("appId", appId);
        return "sys/dict/app_server";
    }
	
	@GetMapping("")
	@RequiresPermissions("sys:server:list")
    public String list(){
        return "sys/dict/server_list";
    }
    @GetMapping("/dataTable")
    @ResponseBody
    @Log("查询服务器列表")
    @RequiresPermissions("sys:server:list")
    public RJson dataTable(){
        List<SysServerConfig> sysServerConfigList = sysServerConfigService.selectList();
        logger.info(JSON.toJSONString(sysServerConfigList));
        if(CollectionUtils.isEmpty(sysServerConfigList)){
        	return RJson.ok().setMsg("无记录");
        }
        return RJson.ok().setData(sysServerConfigList);
    }
    
    @GetMapping("/add")
    @RequiresPermissions("sys:server:add")
    public String server(){
        return "sys/dict/server_add";
    }
    
    @GetMapping("/delServer/{id}")
    @RequiresPermissions("sys:server:del")
    @ResponseBody
    public RJson delServer(@PathVariable("id") Long id){
    	//查看是否是最优版本 最优版本不允许删除
    	SysServerConfig sysServerConfig = sysServerConfigService.selectById(id);
    	if(Objects.isNull(sysServerConfig)){
    		return RJson.error("操作记录不存在!");
    	}
    	
    	if(sysServerConfig.getAvaliable().intValue() == 1){
    		return RJson.error("主机记录最优版本不可以删除！");
    	}
    	
    	//查看是否当前记录被应用引用
//    	Integer count = nqSpeechAppServerService.selectByServerId(id.intValue());
    	
//    	if(count > 0) {
//    		return RJson.error("当前主机记录已被应用绑定，不允许删除!");
//    	}
    	
    	Integer result = sysServerConfigService.delById(id);
    	
    	//删除后 是否需要重新
    	
    	if(result > 0){
    		return RJson.ok("删除成功！");
    	}
    	return RJson.error("删除失败！");
    }
    
    @PostMapping("/addServer")
    @RequiresPermissions("sys:server:add")
    @ResponseBody
    @Log("添加服务器")
    public RJson addServer(SysServerConfigParams params){
        ValidJson validJson = ValidationKit.validate(params, Default.class);
        if(validJson.hasErrors()){
//            throw new AipenException("参数异常");
        	return RJson.error(validJson.getMessage());
        }
        SysServerConfig sysServerConfig = new SysServerConfig();
        BeanUtil.copyProperties(params, sysServerConfig);
        //判断主机记录和版本号是否冲突
        boolean status =  sysServerConfigService.check(sysServerConfig.getDomain(), sysServerConfig.getVersion(),sysServerConfig.getType());
        if(status){
        	return RJson.error("主机记录和版本号重复！");
        }
        //根据主机记录值判断权重值判断是否唯一
        boolean flag = sysServerConfigService.checkRepeated(sysServerConfig.getDomain(), sysServerConfig.getPriority());
        if(flag){
        	return RJson.error("权重值必须唯一!"); 
        }
        //主机记录必须有最优版本 更新有最优版本互斥限制
        if(params.getAvaliable() == 2){
        	boolean avaliable = sysServerConfigService.checkVersionAvaliable(sysServerConfig.getDomain(),sysServerConfig.getType());
        	if(!avaliable){
        		return RJson.error("主机记录必须有最优版本！"); 
        	}
        }
        
        Integer result = sysServerConfigService.add(sysServerConfig);
        if(result > 0){
        	 return RJson.ok("添加成功");
        }
        return RJson.error("添加失败");
    }

}
