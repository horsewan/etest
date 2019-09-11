package com.eningqu.mapper;

import com.eningqu.domain.api.OrdersDetail;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/6 16:15
 * @version    1.0
 *
 **/

public interface OrdersDetailMapper extends CrudDao<OrdersDetail> {
    OrdersDetail selectOrderDetailByNumber(@Param("orderNumber")String orderNumber);
}
