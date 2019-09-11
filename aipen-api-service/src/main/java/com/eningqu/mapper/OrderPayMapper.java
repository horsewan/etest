package com.eningqu.mapper;

import com.eningqu.domain.api.OrderPay;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  包名mapper
 * @author     Yanghuangping
 * @since      2018/9/10 11:11
 * @version    1.0
 *
 **/

public interface OrderPayMapper extends CrudDao<OrderPay> {

    OrderPay selectByWhere(@Param("orderNumber")String orderNumber);
}
