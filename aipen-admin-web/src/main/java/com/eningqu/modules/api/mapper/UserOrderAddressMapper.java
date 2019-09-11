package com.eningqu.modules.api.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.UserOrderAddress;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  包名mapper
 * @author     Yanghuangping
 * @since      2018/9/10 11:11
 * @version    1.0
 *
 **/

public interface UserOrderAddressMapper extends CrudDao<UserOrderAddress> {

    UserOrderAddress selectByWhereId(@Param("orderNumber") String orderNumber, @Param("adid") Long adid);

}
