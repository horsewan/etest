package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.UserInfo;

/**
 *
 * @desc TODO  用户信息service接口
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
public interface IUserInfoService extends IBaseService<UserInfo> {

    /**
     * TODO 手机注册
     * @param mobile
     * @param password
     * @param pkgName
     * @return
     * @throws ServiceException
     */
    void register(String mobile, String password, String pkgName,String addressX,String addressY) throws ServiceException;

    /**
     * TODO 根据手机号码查询用户信息
     * @param mobile
     * @return
     */
    UserInfo selectByMobile(String mobile);

    UserInfo selectByuserQrVal(String userQrVal);


    /**
     * TODO 检查手机号是否被绑定
     * @param mobile
     * @return
     */
    boolean checkMobile(String mobile);
}
