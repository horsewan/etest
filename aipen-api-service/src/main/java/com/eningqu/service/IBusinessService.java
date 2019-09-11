package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.domain.api.BusinessFinanceResult;
import com.eningqu.domain.api.BusinessInfo;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/11/20 16:43
 * @version    1.0
 *
 **/

public interface IBusinessService extends IBaseService<BusinessInfo> {


    /**
     * 通过信用码，个代
     */
    BusinessInfo selectByTicketAndSingleNo(String ticket,String singleNo);

    BusinessInfo selectByPhone(String phone);

    BusinessFinanceResult selectBusinessByPhone(String phone);

    RJson selectUserBalanceList(int page, int limit);

}
