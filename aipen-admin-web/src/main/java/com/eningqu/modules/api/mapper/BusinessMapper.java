package com.eningqu.modules.api.mapper;

import com.eningqu.domain.api.BusinessInfo;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;


/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @since      2018/11/20 16:42
 * @version    1.0
 *
 **/

public interface BusinessMapper extends CrudDao<BusinessInfo> {

    /**
     * TODO 根据条件查询
     * @return
     */
    BusinessInfo queryByWhere(@Param("phone") String phone);

    BusinessInfo queryByTicket(@Param("ticket") String ticket);

    //统一社会编码，个代不可修改
    boolean updateBusinessById(@Param("id")Long id, @Param("bName")String bName, @Param("bPhone")String bPhone, @Param("bPerson")String bPerson,
                       @Param("bAddress")String bAddress, @Param("bSolePrice")BigDecimal bSolePrice,@Param("bPrice")BigDecimal bPrice);

    //sign/cdn_qr
    boolean updateSignById(@Param("id")Long id,@Param("bSign")String bSign,@Param("bCdnQr")String bCdnQr);

    boolean updateBusinessUnionById(@Param("id")Long id, @Param("macid")String macid, @Param("mackey")String mackey,@Param("mchSta") String mchSta);
}
