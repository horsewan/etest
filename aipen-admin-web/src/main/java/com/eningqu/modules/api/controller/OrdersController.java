package com.eningqu.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.modules.system.service.ISysDictService;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.domain.api.Orders;
import com.eningqu.modules.api.service.IOrderService;
import com.eningqu.modules.api.service.IUserOrderAddressService;
import com.eningqu.modules.system.vo.OrderExcelVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/7/24 9:43
 **/

@Controller
@RequestMapping("/mall/orders")
public class OrdersController {

    @Autowired
    private IOrderService ordersService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private IUserOrderAddressService userOrderAddressService;

    @GetMapping("")
    @RequiresPermissions("mall:orders:list")
    public String list(Model model) {
        model.addAttribute("orderStatus", sysDictService.queryDictByGroupCode("order_status"));
        return "api/orders/orders_list";
    }

    @GetMapping("/detail/{orderNumber}")
    @RequiresPermissions("mall:orders:detail")
    @Log("查看订单详情")
    public String detail(@PathVariable String orderNumber, Model model) {
        model.addAttribute("list", ordersService.selectOrderDetaiList(orderNumber));
        return "api/orders/orders_detail";
    }

    @GetMapping("/exp/{orderNumber}")
    @RequiresPermissions("mall:orders:exp")
    @Log("查看订单物流信息")
    public String exp(@PathVariable String orderNumber, Model model) {

//        UserOrderAddress userOrderAddress = userOrderAddressService.selectByWhereId(orderNumber,adid);
//        if(userOrderAddress!=null){
//
//        }
        return "api/orders/orders_exp";
    }

    /**
     * TODO 订单数据列表
     *
     * @param dataTable
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions("mall:orders:list")
    @Log("查询订单数据列表")
    public DataTable<Orders> dataTable(@RequestBody DataTable dataTable) {
        dataTable.setFields(new String[]{"id", "uid", "order_number", "order_status", "serial_number", "exp_company", "exp_code", "exp_no", "sign_name", "sign_phone", "sign_address", "order_time", "pay_way", "pay_time", "amount_payable", "amount_realpay"});
        ordersService.pageSearch(dataTable);
        return dataTable;
    }

    @PostMapping("/excelList")
    @ResponseBody
    public Object getList(@RequestBody Map<String, Object> searchParams) {
//        Map<String, Object> paramsMap = (Map<String, Object>) searchParams;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<OrderExcelVo> ordersList = ordersService.selectExcelList(searchParams);
            map.put("code", 0);
            map.put("msg", "导出成功");
            map.put("count",ordersList==null?"": ordersList.size());
            map.put("data", ordersList);
        } catch (Exception e) {
            map.put("code", 1);
            map.put("msg", "导出失败，请稍后重试！");
            e.printStackTrace();
        }
        return JSON.toJSON(map);
    }


    @PostMapping("/saveExp")
    @ResponseBody
    @Log("更新订单物流信息")
    @RequiresPermissions("mall:orders:exp")
    public RJson saveExp(Orders orders) {
//        ordersService.updateExp(orderNumber, expCode, expNo);
        orders.setOrderStatus(OrderStatusEnum.SHIPPED.getValue());
        ordersService.updateById(orders);
        return RJson.ok();
    }
}
