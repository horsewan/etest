package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.vo.UserCreditRecordTemp;

import java.util.List;

/**
 *
 * @desc TODO  用户红包（积分）mapper
 * @author     Yanghuangping
 * @date       2019/5/2 19:38
 * @version    1.0
 */
public interface UserCreditRecordMapper extends CrudDao<UserCreditRecord> {

    UserCreditRecord selectUserCreditRecordById(Long uid, Long hongbaoId);

    List<UserCreditRecordTemp> selectUserCreditRecordStaById(String oneClass,Long uid);



}
