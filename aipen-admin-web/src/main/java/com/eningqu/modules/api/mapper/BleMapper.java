package com.eningqu.modules.api.mapper;

import com.eningqu.domain.api.BleDevice;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;


/**
 *
 * @desc TODO
 * @author     Yanghuangping
 * @since      2018/11/20 16:42
 * @version    1.0
 *
 **/

public interface BleMapper extends CrudDao<BleDevice> {

    /**
     * TODO 根据条件查询
     * @param pkgId
     * @param mac
     * @return
     */
    BleDevice queryByWhere(@Param("pkgId") Long pkgId, @Param("mac") String mac);

    void updateBleDeviceByPhone(Long id,String phone);

}
