package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.vo.UserCreditRecordTemp;

import java.util.List;

/**
 *
 * @desc TODO  用户磁盘信息接口
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
public interface IUserCreditRecordService extends IBaseService<UserCreditRecord> {

    UserCreditRecord selectUserCreditRecordById(Long uid, Long hongbaoId);

    List<UserCreditRecordTemp> selectUserCreditRecordStaById(String oneClass,Long uid);
}
