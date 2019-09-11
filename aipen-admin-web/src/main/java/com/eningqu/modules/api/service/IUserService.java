package com.eningqu.modules.api.service;

import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.modules.system.vo.CityGroupVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  用户信息service接口
 * @since 2018/5/19 16:08
 **/

public interface IUserService extends IBaseService<UserInfo> {

    boolean deleteByUid(Long uid) throws ServiceException;

    boolean updateDelStatus(Long uid, String sta);

    UserInfo getUserInfoByPhone(String mobile);

    Integer getRegisterCount(String agentNo);

    void updateUserAgentNo();

    void updateUserContact();

    void sendMsgToUnGetProductUser();

    /**
     * 功能描述:
     *
     * @Param 获取当日平台用户零钱总额
     * @Author: lvbu
     * @Date: 2019/9/5 17:26
     */
    BigDecimal selectAllUserMoneyTotal(String agentNo);


    List<CityGroupVo> selectUserCityGroup();

}
