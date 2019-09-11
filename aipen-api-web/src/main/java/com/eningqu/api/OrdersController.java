package com.eningqu.api;

import cn.hutool.core.util.StrUtil;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.service.*;
import com.eningqu.vo.LoginInfoHelper;
import com.eningqu.common.kit.IdWorkerKit;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.domain.api.*;
import com.eningqu.vo.OrdersListVO;
import com.eningqu.vo.OrdersListVO2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @desc  TODO 订单接口
 * @author     Yanghuangping
 * @since      2018/7/6 17:29
 * @version    1.0
 *
 **/
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IOrderPayService orderPayService;
    @Autowired
    private IUserCreditService userCreditService;

    /**
     * TODO 查询订单列表
     * @return
     */
//    @GetMapping("/list")
//    public RJson list(){
//        Long uId = LoginInfoHelper.getUserID();
//        List<OrdersListVO> lists = orderService.selectOrdersList(uId);
//        return RJson.ok().setData(lists);
//    }

    @PostMapping("/list")
    public RJson orderListByUserId(){
        Long uId = LoginInfoHelper.getUserID();
//        List<OrdersListVO2> lists = ;//TODO,只查询预支付、已支付订单，其它线下付、已发货不查询出来。
        return RJson.ok().setData(orderService.selectOrders2List(uId));
    }


    /**
     * TODO 申请退款
     * @param orderNumber
     * @return
     */
    @GetMapping("/refund/apply/{orderNumber}")
    public RJson refund(@PathVariable String orderNumber){
        Long uId = LoginInfoHelper.getUserID();
        Orders orders = orderService.queryOrderByUid(orderNumber, uId);
        if (orders == null) {
            logger.error("退款申请，未查询到该订单号:{}的订单记录", orderNumber);
            throw new AipenException("退款失败");
        }

        // 若订单状态不是【待支付】状态，不能支付
        if (!StrUtil.equalsIgnoreCase(OrderStatusEnum.PAID.getValue(), orders.getOrderStatus())) {
            logger.error("退款申请，该订单号：{} 记录不是[已支付]的状态，不能支付", orderNumber);
            throw new AipenException("支付异常");
        }

        return RJson.ok();
    }


    //线上创建订单
    @PostMapping("/create")
    public RJson orderByPay(@RequestParam Long proId){
        Long uId = LoginInfoHelper.getUserID();
        if(proId<=0)
            return RJson.error("产品不存在");
        Product product = productService.selectById(proId);
        if(product==null)
            return RJson.error("产品不存在");

        Orders orders = new Orders();
        orders.setUid(uId);
        //自动生成订单号，流水号，
        String uuid = System.currentTimeMillis()+"";
        orders.setOrderNumber(IdWorkerKit.uniqueId());
        orders.setSerialNumber(new StringBuilder(uuid).reverse().toString()+">>"+proId);
        orders.setOrderStatus(OrderStatusEnum.UNPAID.getValue());//待支付
        orders.setOrderTime(new Date(System.currentTimeMillis()));
        orders.setAmountPayable(product.getOriginalPrice());
        orders.setBusinessId(1L);
        orderService.insert(orders);

        //添加订单详情
        OrdersDetail ordersDetail = new OrdersDetail();
        ordersDetail.setUid(uId);
        ordersDetail.setProductId(proId);
        ordersDetail.setOrderNumber(orders.getOrderNumber());
        ordersDetail.setOrderPrice(product.getOriginalPrice());
        ordersDetail.setOrderQuantity(product.getStockQuantity());
        orderDetailService.insert(ordersDetail);

        OrdersResult ordersResult = new OrdersResult();
        ordersResult.setoId(orders.getId());
        ordersResult.setbOrderNumber(orders.getOrderNumber());
        ordersResult.setbOrderStatus(orders.getOrderStatus());
        ordersResult.setbOrderTime(orders.getOrderTime());
        ordersResult.setbOriginalPrice(product.getOriginalPrice());
        ordersResult.setbSolePrice(product.getSalePrice());

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(uId);
        if(mUserCredit!=null){
            ordersResult.setbCreditTotal(mUserCredit.getVipCredit()==null?new BigDecimal(0) :mUserCredit.getVipCredit());
        }else{
            ordersResult.setbCreditTotal(new BigDecimal(0));
        }

        return RJson.ok("订单创建成功").setData(ordersResult);
    }

    @PostMapping("/getOrderInfo")
    public RJson getOrderDetails(@RequestParam String orderNumber){
        Long uId = LoginInfoHelper.getUserID();
        if(uId<0)
            return RJson.error("用户id编号参数错误");
        if(orderNumber==null||"".equals(orderNumber))
            return RJson.error("订单编号参数错误");
        //根据订单获取所有数据
        Orders orders = orderService.queryOrderByNumber(orderNumber);
        if(orders==null)
            return RJson.error("该订单不存在");

        OrdersDetailResult ordersDetailResult = new OrdersDetailResult();

        //获取物流信息
        ordersDetailResult.setExpCode(orders.getExpCode());
        ordersDetailResult.setExpCompany(orders.getExpCompany());
        ordersDetailResult.setExpNo(orders.getExpNo());

        //获取配送信息
        ordersDetailResult.setSignAddress(orders.getSignAddress());
        ordersDetailResult.setSignName(orders.getSignName());
        ordersDetailResult.setSignPhone(orders.getSignPhone());

        //订单详情
        ordersDetailResult.setId(orders.getId());
        ordersDetailResult.setOrderNumber(orderNumber);
        ordersDetailResult.setOrderTime(orders.getOrderTime());
        ordersDetailResult.setOrderStatus(orders.getOrderStatus());
        ordersDetailResult.setAmountPayable(orders.getAmountPayable());
        ordersDetailResult.setAmountRealpay(orders.getAmountRealpay());
        ordersDetailResult.setPayWay(orders.getPayWay());
        ordersDetailResult.setPayTime(orders.getPayTime());
        ordersDetailResult.setRemarks(orders.getRemarks());

        //TODO 微信支付必要参数返回
        if(orders.getOrderStatus()!=null&&OrderStatusEnum.UNPAID.getValue().equalsIgnoreCase(orders.getOrderStatus())
                &&(orders.getPayWay().equalsIgnoreCase(PayWayStatusEnum.WEI_XIN_PAY.getValue()))
                ||orders.getPayWay().equalsIgnoreCase(PayWayStatusEnum.UNION_PAY.getValue())
                ||orders.getPayWay().equalsIgnoreCase(PayWayStatusEnum.ALI_PAY.getValue())){
            OrderPay orderPay = orderPayService.selectByWhere(orderNumber);
            if(orderPay!=null){
                ordersDetailResult.setAppid(orderPay.getAppid());
                ordersDetailResult.setPackagePay(orderPay.getPackagePay());
                ordersDetailResult.setPartnerid(orderPay.getPartnerid());
                ordersDetailResult.setNoncestr(orderPay.getNoncestr());
                ordersDetailResult.setSign(orderPay.getSign());
                ordersDetailResult.setPrepayid(orderPay.getPrepayid());
                ordersDetailResult.setTimestamp(orderPay.getTimestamp());
            }
        }/*else if(orders.getPayWay().equalsIgnoreCase(PayWayStatusEnum.UNION_PAY.getValue())){//TODO 云闪付则需要通过订单号进行查询当前是否已经超时（支付时效）
            OrderPay tempOrderPay = orderPayService.selectByWhere(orderNumber);
            if(tempOrderPay!=null){
                ordersDetailResult.setAppid(orderPay.getAppid());
                ordersDetailResult.setPackagePay(orderPay.getPackagePay());
                ordersDetailResult.setPartnerid(orderPay.getPartnerid());
                ordersDetailResult.setNoncestr(orderPay.getNoncestr());
                ordersDetailResult.setSign(orderPay.getSign());
                ordersDetailResult.setPrepayid(orderPay.getPrepayid());
                ordersDetailResult.setTimestamp(orderPay.getTimestamp());
            }
        }*/

        return RJson.ok().setData(ordersDetailResult);
    }


    /**
     * 红包兑换订单详情
     * @param orderNumber
     * @return
     */
    @PostMapping("/getOrderByUserCredit")
    public RJson getOrderByUserCredit(@RequestParam String orderNumber){
        Long uId = LoginInfoHelper.getUserID();
        if(uId<0)
            return RJson.error("用户id编号参数错误");
        if(orderNumber==null||"".equals(orderNumber))
            return RJson.error("订单编号参数错误");
        //根据订单获取所有数据
        Orders orders = orderService.queryOrderByNumber(orderNumber);
        if(orders==null)
            return RJson.error("该订单不存在");

        HongBaoOrdersDetailResult ordersDetailResult = new HongBaoOrdersDetailResult();

        //获取物流信息
        ordersDetailResult.setExpCode(orders.getExpCode());
        ordersDetailResult.setExpCompany(orders.getExpCompany());
        ordersDetailResult.setExpNo(orders.getExpNo());

        //获取配送信息
        ordersDetailResult.setSignAddress(orders.getSignAddress());
        ordersDetailResult.setSignName(orders.getSignName());
        ordersDetailResult.setSignPhone(orders.getSignPhone());

        //订单详情
        ordersDetailResult.setId(orders.getId());
        ordersDetailResult.setOrderNumber(orderNumber);
        ordersDetailResult.setOrderTime(orders.getOrderTime());
        ordersDetailResult.setOrderStatus(orders.getOrderStatus());
        ordersDetailResult.setAmountPayable(orders.getAmountPayable());
        ordersDetailResult.setAmountRealpay(orders.getAmountRealpay());
        ordersDetailResult.setPayWay(orders.getPayWay());
        ordersDetailResult.setPayTime(orders.getPayTime());
        ordersDetailResult.setRemarks(orders.getRemarks());

        return RJson.ok().setData(ordersDetailResult);
    }

}
