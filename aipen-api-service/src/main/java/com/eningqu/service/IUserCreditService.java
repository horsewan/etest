package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.UserCredit;

/**
 *
 * @desc TODO  用户磁盘信息接口
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
public interface IUserCreditService extends IBaseService<UserCredit> {


    /**
     * TODO 根据uid查询总积分
     * @param uid
     * @return
     */
    UserCredit selectUserCreditByUid(Long uid);

    /**
     * TODO
     * @param userCredit
     * @return
     * @throws ServiceException
     */
    boolean updateOne(UserCredit userCredit) throws ServiceException;

    /**
     * TODO 根据UID 创建一个默认的磁盘空间
     * @param uid
     * @return
     * @throws ServiceException
     */
    boolean insertOne(Long uid) throws ServiceException;
}
