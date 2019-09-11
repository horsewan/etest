package com.eningqu.api;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.domain.api.Orders;
import com.eningqu.domain.api.UserAddress;
import com.eningqu.domain.api.UserOrderAddress;
import com.eningqu.service.IOrderService;
import com.eningqu.service.IUserAddressService;
import com.eningqu.service.IUserOrderAddressService;
import com.eningqu.vo.LoginInfoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描   述：用户订单地址
 * 作   者：zhouchongwen
 * 时   间：2019/03/08
 * */
@RestController
@RequestMapping("/api/orderAddress")
public class UserOrderAddressController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserOrderAddressService userOrderAddressService;
    @Autowired
    private IOrderService ordersService;
    @Autowired
    private IUserAddressService userAddressService;

    @RequestMapping("/add")
    public RJson add(@RequestParam String orderNumber, @RequestParam Long adid){
        Long userID = LoginInfoHelper.getUserID();
        if(orderNumber==null&&"".equals(orderNumber))
            return RJson.error("当前订单编号参数错误");
        if(adid<0)
            return RJson.error("当前地址id参数错误");

        UserOrderAddress tempUserOrderAddress = userOrderAddressService.selectByWhereId(orderNumber,adid);
        if(tempUserOrderAddress!=null){
            return RJson.ok( "设置配送地址成功" );
        }

        //先查询地址详情
        UserAddress userAddress = userAddressService.selectById(adid);
        if(userAddress!=null && userAddress.getuId().equals(userID)){
            Orders orders = ordersService.queryOrderByNumber(orderNumber);
            if(orders!=null && orders.getUid().equals(userID)){
                orders.setSignAddress(userAddress.getaAddress());
                orders.setSignName(userAddress.getaNick());
                orders.setSignPhone(userAddress.getaPhone());
                ordersService.updateById(orders);
            } else {
                return RJson.error("订单信息有误");
            }
        } else {
            return RJson.error("地址信息有误");
        }

        UserOrderAddress userOrderAddress = new UserOrderAddress();
        userOrderAddress.setAdid(adid);
        userOrderAddress.setOrderNumber(orderNumber);
        boolean bool = userOrderAddressService.insert(userOrderAddress);
        if(bool){
            return RJson.ok( "设置配送地址成功" );
        }else{
            return RJson.error( "设置配送地址失败" );
        }
    }
}
