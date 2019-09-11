package com.eningqu.modules.api.service;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.BusinessInfo;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.math.BigDecimal;

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
     * TODO 更新可用状态
     * @param id
     * @return
     * @throws ServiceException
     */
    boolean updateStatus(Long id) throws ServiceException;

    BusinessInfo queryByWhere(String phone);

    BusinessInfo queryByTicket(String ticket);

    boolean updateBusinessById(Long id, String bName, String bPhone, String bPerson, String bAddress, BigDecimal bSolePrice, BigDecimal bPrice);

    boolean updateBusinessUnionById(Long id, String macid, String mackey,String mchSta);

    boolean updateSignById(Long id, String bSign, String bCdnQr);

    RJson insertBusinessInfo(BusinessInfo businessInfo) throws IndexOutOfBoundsException,IOException, WriterException;
}
