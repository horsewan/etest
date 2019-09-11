package com.eningqu.modules.api.controller;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.SysUserTypeEnum;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.modules.api.service.IOrderService;
import com.eningqu.modules.api.service.IUserService;
import com.eningqu.modules.system.service.ISysCityService;
import com.eningqu.modules.system.vo.ShiroUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描    述：  TODO   控制台
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/28 9:48
 */
@Controller
@RequestMapping("/sys/console")
public class ConsoleController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisKit redisKit;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ISysCityService cityService;



    @GetMapping("")
    @RequiresPermissions("sys:console:home")
    public String list(Map<String, Object> map) {

        //总成交金额
        BigDecimal totalMoney = iOrderService.selectCityTotal();
        map.put("totalMoney", totalMoney.intValue() == 0 ? 0 : totalMoney);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        //总收入
        BigDecimal totalSum = iOrderService.selectCityTotal();
        map.put("totalSum", totalSum.intValue() == 0 ? 0 : decimalFormat.format(totalSum.multiply(new BigDecimal(0.005))));
        //注册用户总数
        ShiroUser shiroUser = ShiroKit.loginInfo();
        String agentNo = "";
        if (!Objects.equals(SysUserTypeEnum.SUPER.getValue(), shiroUser.getUserType())) {
            agentNo = shiroUser.getLoginName().substring(0, 2);
        }
        Integer userTotal = userService.getRegisterCount(agentNo);
        map.put("userTotal", userTotal);
        //平台零钱总额
        map.put("moneyTotal", userService.selectAllUserMoneyTotal(agentNo));
        //活跃用户
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        map.put("onLineUserTotal", "".equals(agentNo) ? redisKit.getBit(Global.API_LOGIN_INFO + today) : redisKit.getBit(Global.API_LOGIN_INFO + agentNo + ":" + today));
        map.put("cityList",userService.selectUserCityGroup());
        return "api/console/home_page";
    }

    @GetMapping("getCityList")
    @ResponseBody
    @RequiresPermissions("sys:console:home")
    public RJson getCountOnline() {
        return RJson.ok().setData(userService.selectUserCityGroup());
    }
}
