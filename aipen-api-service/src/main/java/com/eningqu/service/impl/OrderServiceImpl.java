package com.eningqu.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.service.IOrderDetailService;
import com.eningqu.service.IOrderService;
import com.eningqu.service.IProductService;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.domain.api.Orders;
import com.eningqu.mapper.OrdersMapper;
import com.eningqu.vo.OrdersListVO;
import com.eningqu.vo.OrdersListVO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/6 16:20
 * @version    1.0
 *
 **/
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrdersMapper, Orders> implements IOrderService {

    @Autowired
    private IProductService productService;
    @Autowired
    private IOrderDetailService orderDetailService;

    /**
     * 查询订单列表
     *
     * @param uid
     * @return
     */
    @Override
    public List<OrdersListVO> selectOrdersList(Long uid) {
        return baseMapper.selectOrdersList(uid);
    }

    @Override
    public List<OrdersListVO2> selectOrders2List(Long uid) {
        return baseMapper.selectOrders2List(uid);
    }

    /**
     * TODO 根据订单号查询
     *
     * @param orderNumber
     * @param uid
     * @return
     */
    @Override
    public Orders queryOrderByUid(String orderNumber, Long uid) {
        return baseMapper.selectOrderByOrderNumberForUid(orderNumber, uid);
    }

    @Override
    public Orders queryOrderByNumber(String orderNumber) {
        return baseMapper.queryOrderByNumber(orderNumber);
    }

    /**
     * TODO 支付宝或微信 支付异步通知 更新订单状态
     *
     * @param orderNumber       订单号
     * @param serialNumber      支付流水号
     * @param buyerPayAmount    支付金额
     * @param payTime           支付时间
     * @param payWay            支付方式
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.REPEATABLE_READ,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean updatePayNotifyOrder(String orderNumber, String serialNumber, String buyerPayAmount, String payTime, String payWay) {

        Orders order = baseMapper.selectOrderByOrderNumber(orderNumber);
        if(order == null){
            logger.error("支付异步通知, 订单号：{}不存在", orderNumber);
            return false;
        }

        logger.error("支付异步通知, 1  订单号：{} orderStatus：{}", orderNumber,order.getOrderStatus());
        //待支付、线下付都直接执行
        if(StrUtil.equalsIgnoreCase(OrderStatusEnum.UNPAID.getValue(),order.getOrderStatus())
            ||StrUtil.equalsIgnoreCase(OrderStatusEnum.COMPLETED.getValue(),order.getOrderStatus())){
            logger.error("支付异步通知, 2  订单号：{} orderStatus：{}", orderNumber,order.getOrderStatus());

            BigDecimal payAmount = new BigDecimal(buyerPayAmount);
    //        if(order.getAmountPayable().compareTo(payAmount) != 0){
    //            logger.error("支付异步通知, 订单号：{}, 应付订单金额：{}，实付订单金额：{}，订单金额不正确", orderNumber, order.getAmountPayable().toString(), buyerPayAmount);
    //            return false;
    //        }

            // TODO 更新字段

            // 从【待支付】状态变更为【已支付】状态
            order.setOrderStatus(OrderStatusEnum.PAID.getValue());
            // 实付金额
    //        order.setAmountPayable(payAmount);
            order.setAmountRealpay(payAmount);
            // 支付时间
    //        order.setPayTime(DateUtil.parse(payTime, DatePattern.NORM_DATETIME_PATTERN));
            order.setPayTime(DateUtil.date(DateUtil.current(false)));
            // 支付方式
            order.setPayWay(payWay);
            // 支付流水号
            order.setSerialNumber(serialNumber);
            logger.error("setOrderStatus：{} setAmountPayable：{} setPayTime：{} setPayWay：{} setSerialNumber：{}", order.getOrderStatus(),payAmount,order.getPayTime(),order.getPayWay(),order.getSerialNumber());
            try {
                int count = baseMapper.updatePayNotifyOrder(order, OrderStatusEnum.UNPAID.getValue());
                if(count != 1){
                    logger.error("支付异步通知, 更新订单号：{} 的订单失败，{}", orderNumber);
                    return false;
                }
            } catch (RuntimeException e) {
                logger.error("支付异步通知, 更新订单号：{} 的订单异常，{}", orderNumber, e);
                return false;
            }
        }else{
            logger.error("支付异步通知, 订单号：{}, 订单状态不是【待支付】", orderNumber);
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.REPEATABLE_READ,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean updateUnionPayNotifyOrder(String orderNumber) {

        Orders order = baseMapper.selectOrderByOrderNumber(orderNumber);
        if(order == null){
            logger.error("支付异步通知, 订单号：{}不存在", orderNumber);
            return false;
        }
        logger.error("支付异步通知, 1  订单号：{} orderStatus：{}", orderNumber,order.getOrderStatus());
        //待支付、线下付都直接执行
        if(StrUtil.equalsIgnoreCase(OrderStatusEnum.UNPAID.getValue(),order.getOrderStatus())
                ||StrUtil.equalsIgnoreCase(OrderStatusEnum.COMPLETED.getValue(),order.getOrderStatus())){
            logger.error("支付异步通知, 2  订单号：{} orderStatus：{}", orderNumber,order.getOrderStatus());
            // 从【待支付】状态变更为【已支付】状态
            order.setOrderStatus(OrderStatusEnum.PAID.getValue());

//            order.setPayTime(DateUtil.date(DateUtil.current(false)));
            // 支付方式
//            order.setPayWay(PayWayStatusEnum.UNION_PAY.getValue());

            try {
                int count = baseMapper.updateUnionPayNotifyOrder(order);
                if(count != 1){
                    logger.error("支付异步通知, 更新订单号：{} 的订单失败，{}", orderNumber);
                    return false;
                }
            } catch (RuntimeException e) {
                logger.error("支付异步通知, 更新订单号：{} 的订单异常，{}", orderNumber, e);
                return false;
            }
        }else{
            logger.error("支付异步通知, 订单号：{}, 订单状态不是【待支付】", orderNumber);
            return false;
        }
        return true;
    }
}
