package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.kit.PasswordKit;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.mapper.UserInfoMapper;
import com.eningqu.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @desc TODO  用户信息service接口实现类
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/5/2 19:38
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    private Logger logger  = LoggerFactory.getLogger(getClass());
    /**
     * TODO 手机号注册
     *
     * @param mobile
     * @param password
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void register(String mobile, String password, String pkgName,String addressX,String addressY) throws ServiceException {
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(mobile);
        userInfo.setAddressX(addressX);
        userInfo.setAddressY(addressY);
        userInfo.setCredential(PasswordKit.entrypt(password));
        userInfo.setRegisterTime(new Date());
        try {
            baseMapper.insert(userInfo);
            // 智能笔 授权登录 需要自动分配磁盘空间
//            if (StrUtil.equalsIgnoreCase(pkgName, Global.AIPEN_PKG_NAME)) {
//                if(!userDiskService.insertOne(userInfo.getId())){
//                    throw new ServiceException("分配磁盘空间失败");
//                }
//            }
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * TODO 根据手机号码查询用户信息
     *
     * @param mobile
     * @return
     */
    @Override
    public UserInfo selectByMobile(String mobile) {
        return baseMapper.selectByMobile(mobile);
    }

    @Override
    public UserInfo selectByuserQrVal(String userQrVal) {
        return baseMapper.selectByuserQrVal(userQrVal);
    }


    /**
     * TODO 检查手机号是否被绑定
     *
     * @param mobile
     * @return
     */
    @Override
    public boolean checkMobile(String mobile) {
        int count = baseMapper.checkMobile(mobile);
        return count != 0 ? true : false;
    }


    /**
     * TODO 分配存储空间
     * @param uid
     * @param diskSize
     */
    /*private boolean allocateDiskSpace(Long uid, long diskSize){
        UserDisk disk = userDiskService.selectByUid(uid);
        if(disk != null){
            return false;
        }
        disk = new UserDisk();
        disk.setUid(uid);
        disk.setTotalSize(diskSize);
        try {
            userDiskService.insert(disk);
        } catch (ServiceException e) {
            logger.error("分配磁盘空间失败，{}", e);
            return false;
        }
        return true;
    }*/
}
