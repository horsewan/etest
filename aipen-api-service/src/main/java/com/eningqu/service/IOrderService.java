package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.Orders;
import com.eningqu.vo.OrdersListVO;
import com.eningqu.vo.OrdersListVO2;

import java.util.List;

/**
 *
 * @desc TODO  订单service接口
 * @author     Yanghuangping
 * @since      2018/7/5 15:26
 * @version    1.0
 *
 **/

public interface IOrderService extends IBaseService<Orders>{

    /**
     * TODO 查询订单列表
     * @param uid
     * @return
     */
    List<OrdersListVO> selectOrdersList(Long uid);

    List<OrdersListVO2> selectOrders2List(Long uid);


    /**
     * TODO 根据订单号查询
     * @param orderNumber
     * @param uid
     * @return
     */
    Orders queryOrderByUid(String orderNumber, Long uid);

    Orders queryOrderByNumber(String orderNumber);

    /**
     * TODO 支付宝或微信 支付异步通知 更新订单状态
     * @param orderNumber       订单号
     * @param serialNumber      支付流水号
     * @param buyerPayAmount    支付金额
     * @param payTime           支付时间
     * @param payWay            支付方式
     * @return
     */
    boolean updatePayNotifyOrder(String orderNumber, String serialNumber, String buyerPayAmount, String payTime, String payWay);

    boolean updateUnionPayNotifyOrder(String orderNumber);


}
