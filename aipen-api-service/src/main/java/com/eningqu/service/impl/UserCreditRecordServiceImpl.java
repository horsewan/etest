package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.mapper.UserCreditRecordMapper;
import com.eningqu.service.IUserCreditRecordService;
import com.eningqu.vo.UserCreditRecordTemp;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * @desc TODO  用户磁盘service接口实现类
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
@Service
public class UserCreditRecordServiceImpl extends BaseServiceImpl<UserCreditRecordMapper, UserCreditRecord> implements IUserCreditRecordService {

    @Override
    public UserCreditRecord selectUserCreditRecordById(Long uid, Long hongbaoId) {
        return baseMapper.selectUserCreditRecordById(uid,hongbaoId);
    }

    @Override
    public List<UserCreditRecordTemp> selectUserCreditRecordStaById(String oneClass,Long uid) {
        return baseMapper.selectUserCreditRecordStaById(oneClass,uid);
    }


}
