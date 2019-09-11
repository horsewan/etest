package com.eningqu.modules.api.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.modules.system.vo.CityGroupVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/19 16:12
 * @version    1.0
 *
 **/

public interface UserMapper extends CrudDao<UserInfo> {

    Integer deleteOauthUser(@Param("uid") Long uid);

    Integer updateDelStatus(@Param("uid") Long uid,@Param("sta") String sta);

    UserInfo getUserInfoByPhone(String mobile);

    void updateAgentNoByPhone(@Param("agentNo") String agentNo,@Param("mobile") String mobile,@Param("level")String level);

    void updateSysUser(@Param("id") Long id);

    List<UserInfo> selectByAgentNoIsDefault(Pagination pagination);

    List<UserInfo> selectUnGetProductUser();

    /**
    * 功能描述:
    * @Param 获取当日平台用户零钱总额
    * @Author: lvbu
    * @Date: 2019/9/5 17:26
    */
    BigDecimal  selectAllUserMoneyTotal(@Param("agentNo")String agentNo);

    List<CityGroupVo> selectCityNums();
}
