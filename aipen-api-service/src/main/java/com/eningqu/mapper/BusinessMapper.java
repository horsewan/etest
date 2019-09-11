package com.eningqu.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.eningqu.domain.api.BalanceResult;
import com.eningqu.domain.api.BusinessFinanceResult;
import com.eningqu.domain.api.BusinessInfo;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;


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

    BusinessFinanceResult selectBusinessByPhone(@Param("phone")String phone);


    List<BalanceResult> selectUserBalanceList(Pagination pagination, @Param("phone") String phone);


}
