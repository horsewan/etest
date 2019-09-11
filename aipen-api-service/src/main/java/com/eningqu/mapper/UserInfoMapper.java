package com.eningqu.mapper;

import com.eningqu.domain.api.UserInfo;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  用户信息mapper
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
public interface UserInfoMapper extends CrudDao<UserInfo> {


    /**
     * TODO 根据手机号码查询用户记录
     * @param mobile
     * @return
     */
    UserInfo selectByMobile(@Param("mobile") String mobile);

    UserInfo selectByuserQrVal(@Param("userQrVal") String userQrVal);

    /**
     * TODO 检查手机号是否被绑定
     * @param mobile
     * @return
     */
    int checkMobile(@Param("mobile") String mobile);
}
