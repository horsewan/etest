package com.eningqu.modules.system.controller;

import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.exception.AipenException;
import com.eningqu.domain.system.SysLog;
import com.eningqu.modules.system.service.ISysLogService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  操作日志controller类
 * @author     Yanghuangping
 * @since      2018/5/21 11:56
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysLogService sysLogService;

    /**
     * TODO 操作日志列表页面
     * @return
     */
    @GetMapping("")
    @RequiresPermissions("sys:log:list")
    public String list(){
        return "sys/log/log_list";
    }

    @GetMapping(value = "/dataTable")
    @ResponseBody
    @RequiresPermissions("sys:log:list")
    public DataTable<SysLog> dataTable(@RequestParam int page,
                                       @RequestParam int limit,
                                       @RequestParam(required = false) String userName){

        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_LLIKE + "login_name", userName);

        DataTable<SysLog> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        sysLogService.pageSearch(dataTable);

        return dataTable;
    }


    /**
     * TODO 删除日志
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:log:delete")
    public RJson delete(@RequestParam("ids[]") List<Long> ids){
        if(!sysLogService.deleteBatchIds(ids)){
            throw new AipenException("日志删除失败");
        }
        return RJson.ok("日志删除成功");
    }
}
