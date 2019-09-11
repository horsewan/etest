package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.UserCredit;
import com.eningqu.mapper.UserCreditMapper;
import com.eningqu.service.IUserCreditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @desc TODO  用户磁盘service接口实现类
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
@Service
public class UserCreditServiceImpl extends BaseServiceImpl<UserCreditMapper, UserCredit> implements IUserCreditService {


    /**
     * 根据uid查询笔数据信息
     * @param uid
     * @return
     */
    @Override
    public UserCredit selectUserCreditByUid(Long uid) {
        UserCredit userDisk = baseMapper.selectUserCreditByUid(uid);
        if(userDisk == null){
            userDisk = new UserCredit();
            userDisk.setUid(uid);
            baseMapper.insert(userDisk);
        }
        return userDisk;
    }

    /**
     * 更新红包（积分）
     * @param userDisk
     * @return
     * @throws ServiceException
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = Exception.class)
    @Override
    public boolean updateOne(UserCredit userDisk) throws ServiceException {
        int count = baseMapper.updateById(userDisk);
        return count == 1 ? true : false;
    }

    /**
     * TODO 根据UID 创建一个默认的磁盘空间
     *
     * @param uid
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean insertOne(Long uid) throws ServiceException {
        UserCredit disk = new UserCredit();
        disk.setUid(uid);
        try {
            baseMapper.insert(disk);
        } catch (ServiceException e) {
            logger.error("分配磁盘空间失败，{}", e);
            return false;
        }
        return true;
    }
}
