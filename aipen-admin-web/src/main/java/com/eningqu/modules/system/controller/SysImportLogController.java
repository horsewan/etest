package com.eningqu.modules.system.controller;

import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.exception.AipenException;
import com.eningqu.domain.system.SysImportLog;
import com.eningqu.modules.system.service.ISysImportLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/11/22 11:17
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/sys/import")
public class SysImportLogController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysImportLogService importLogService;

    /**
     * TODO 导入日志列表页面
     * @return
     */
    @GetMapping
    @RequiresPermissions("sys:import:list")
    public String list(){
        return "sys/log/import_list";
    }


    @PostMapping(value = "/dataTable")
    @ResponseBody
    @RequiresPermissions("sys:import:list")
    @Log("导入日志列表查询")
    public DataTable<SysImportLog> dataTable(@RequestBody DataTable dataTable){
        dataTable.sortDesc("op_time");
        importLogService.pageSearch(dataTable);
        return dataTable;
    }

    /**
     * TODO 删除日志
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:import:delete")
    @Log("导入日志删除")
    public RJson delete(@RequestParam("ids[]") List<Long> ids){
        if(!importLogService.deleteBatchIds(ids)){
            throw new AipenException("日志删除失败");
        }
        return RJson.ok("日志删除成功");
    }

}
