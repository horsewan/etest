package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.UserCredit;

/**
 *
 * @desc TODO  用户红包（积分）mapper
 * @author     Yanghuangping
 * @date       2019/5/2 19:38
 * @version    1.0
 */
public interface UserCreditMapper extends CrudDao<UserCredit> {

    /**
     * TODO 根据用户uid查询总积分
     * @param uid
     * @return
     */
    UserCredit selectUserCreditByUid(Long uid);

}
