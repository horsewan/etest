package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.Orders;
import org.apache.ibatis.annotations.Param;


/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/6 16:15
 * @version    1.0
 *
 **/

public interface OrdersMapper extends CrudDao<Orders> {

    /**
     * TODO 根据订单号查询 对应用户的订单
     * @param orderNumber
     * @param uid
     * @return
     */
    Orders selectOrderByOrderNumberForUid(@Param("orderNumber") String orderNumber, @Param("uid") Long uid);

    Orders queryOrderByNumber(@Param("orderNumber") String orderNumber);

    /**
     * TODO 根据订单号查询
     * @param orderNumber
     * @return
     */
    Orders selectOrderByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * TODO 更新成功支付的订单信息 订单初始状态为【待支付】
     * @param order
     * @param originalOrderStatus 初始订单状态
     * @return
     */
    int updatePayNotifyOrder(@Param("order") Orders order, @Param("originalOrderStatus") String originalOrderStatus);
}
