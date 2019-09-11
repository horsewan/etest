package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.BusinessInfo;
import org.apache.ibatis.annotations.Param;


/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @since      2018/11/20 16:42
 * @version    1.0
 *
 **/

public interface BusinessMapper extends CrudDao<BusinessInfo> {

    BusinessInfo selectByTicketAndSingleNo(@Param("ticket")String ticket,@Param("singleNo")String singleNo);

    BusinessInfo selectByPhone(@Param("phone")String phone);


}
