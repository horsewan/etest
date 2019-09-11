package com.eningqu.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.base.vo.ValidJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.kit.ValidationKit;
import com.eningqu.domain.system.SysDict;
import com.eningqu.modules.system.params.DictParams;
import com.eningqu.modules.system.service.ISysDictService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 *
 * @desc TODO  数据字典controller
 * @author     Yanghuangping
 * @since      2018/5/23 19:22
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/sys/dict")
public class SysDictController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysDictService sysDictService;

    @GetMapping("")
    @RequiresPermissions("sys:dict:list")
    public String list(){
        return "sys/dict/dict_list";
    }

    @GetMapping("/page")
    @RequiresPermissions(value = {"sys:dict:add","sys:dict:edit"}, logical = Logical.OR)
    public String page(){
        return "sys/dict/dict_page";
    }

    @GetMapping("/group")
    @RequiresPermissions(value = {"sys:dict:add","sys:dict:edit"}, logical = Logical.OR)
    public String group(){
        return "sys/dict/dict_group";
    }

    /**
     * TODO 加载数据字典列表
     * @return
     */
    @GetMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions("sys:dict:list")
    @Log("查询数据字典列表")
    public RJson dataTable(){
        List<SysDict> dicts = sysDictService.selectDicts();
        return RJson.ok().setData(dicts);
    }

    /**
     * TODO 添加数据字典
     * @param dictParams
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("sys:dict:add")
    @Log("添加数据字典")
    public RJson add(DictParams dictParams){

        ValidJson validJson = ValidationKit.validate(dictParams, ValidationKit.EditValid.class, ValidationKit.GroupValid.class);
        if(validJson.hasErrors()){
            throw new AipenException("参数异常");
        }
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictParams, sysDict);
        sysDictService.insertDict(sysDict);

        return RJson.ok("数据字典添加成功");
    }

    /**
     * TODO 编辑数据字典
     * @param dictParams
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("sys:dict:edit")
    @Log("修改数据字典")
    public RJson edit(DictParams dictParams){
        ValidJson validJson = ValidationKit.validate(dictParams, Default.class, ValidationKit.EditValid.class, ValidationKit.GroupValid.class);
        if(validJson.hasErrors()){
            throw new AipenException("参数异常");
        }
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictParams, sysDict);
        sysDict.setGroupCode(null);
        sysDict.setGroupDesc(null);
        sysDict.setPid(null);
        sysDictService.updateById(sysDict);
        return RJson.ok("修改成功");
    }


    /**
     * TODO 添加数据字典组
     * @param dictParams
     * @return
     */
    @PostMapping("/addGroup")
    @ResponseBody
    @RequiresPermissions("sys:dict:add")
    @Log("添加数据字典组")
    public RJson addGroup(DictParams dictParams){
        ValidJson validJson = ValidationKit.validate(dictParams, ValidationKit.GroupValid.class);
        if(validJson.hasErrors()){
            throw new AipenException("参数异常");
        }
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictParams, sysDict);
        sysDict.setPid(Global.TOP_PID_ID);
        sysDictService.insertDictGroup(sysDict);
        return RJson.ok("添加成功");
    }

    /**
     * TODO 编辑数据字典组
     * @param dictParams
     * @return
     */
    @PostMapping("/editGroup")
    @ResponseBody
    @RequiresPermissions("sys:dict:edit")
    @Log("修改数据字典组")
    public RJson editGroup(DictParams dictParams){
        ValidJson validJson = ValidationKit.validate(dictParams, Default.class, ValidationKit.GroupValid.class);
        if(validJson.hasErrors()){
            throw new AipenException("参数异常");
        }
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictParams, sysDict);
        sysDict.setGroupCode(null);
        sysDict.setPid(Global.TOP_PID_ID);
        sysDictService.updateDictGroup(sysDict);
        return RJson.ok("修改成功");
    }


    /**
     * TODO 删除数据字典
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys:dict:delete")
    @Log("删除数据字典")
    public RJson delete(@PathVariable("id") Long id){
        if(!sysDictService.delDict(id)){
            throw new AipenException("删除字典失败");
        }
        return RJson.ok("删除字典成功");
    }
}
