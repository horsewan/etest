package com.eningqu.modules.api.controller;

import cn.hutool.core.util.StrUtil;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.enums.SysUserTypeEnum;
import com.eningqu.common.kit.DateTimeKit;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.domain.api.FinanceListResult;
import com.eningqu.domain.api.FinanceResult;
import com.eningqu.modules.api.service.IOrderService;
import com.eningqu.modules.system.vo.ShiroUser;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 描    述：  TODO  结算数据
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/28 9:48
 */
@Controller
@RequestMapping("/mall/finance")
public class FinanceController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;

    @GetMapping("/home")
    public String financePage() {

        return "api/finance/finance-page";
    }


    /**
     * 普通用户结算列表
     *
     * @return
     */
    @GetMapping("/normalList")
    @RequiresPermissions("dy:finance:normalList")
    public String normalList() {
        return "api/finance/finance_normal_list";
    }

    /**
     * 商家结算列表
     *
     * @return
     */
    @GetMapping("/shopList")
    @RequiresPermissions("dy:finance:shopList")
    public String shopList() {
        return "api/finance/finance_shop_list";
    }

    /**
     * 市级代理结算列表
     *
     * @return
     */
    @GetMapping("/cityList")
    @RequiresPermissions("dy:finance:cityList")
    public String list() {
        return "api/finance/finance_city_list";
    }

    /**
     * 团代结算列表
     *
     * @return
     */
    @GetMapping("/teamList")
    @RequiresPermissions("dy:finance:teamList")
    public String teamList() {
        return "api/finance/finance_team_list";
    }

    @GetMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions(value = {"dy:finance:shopList", "dy:finance:cityList", "dy:finance:normalList", "dy:finance:teamList"}, logical = Logical.OR)
    @Log("商户信息列表查询")
    public DataTable<FinanceListResult> dataTable(@RequestParam int page,
                                                  @RequestParam int limit,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false) String userType,
                                                  @RequestParam(required = false) String singleNo) {
        //TODO 获取当前登录用户标识，代理则是代理编号
        ShiroUser shiroUser = null;
        if (ShiroKit.isLogin()) {
            shiroUser = ShiroKit.loginInfo();
        } else {
            return null;
        }
        String loginName = shiroUser.getLoginName();

        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put("singleNo", singleNo);//编号
        searchParams.put("phone", phone);//手机号
        searchParams.put("userType", userType);//用户类型
        if (!StrUtil.equalsIgnoreCase(SysUserTypeEnum.SUPER.getValue(), shiroUser.getUserType())) {
            searchParams.put("agentNo", loginName.substring(0, 2));//登陆账号
        }

        searchParams.put("startDate", DateTimeKit.getDayOnThisMonth());//开始日期
        searchParams.put("endDate", DateTimeKit.format(DateTimeKit.getDate(),DateTimeKit.DATE_TIME_PATTERN));//结束日期
        DataTable<FinanceListResult> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        orderService.selectFinanceList(dataTable);

        return dataTable;
    }


}
