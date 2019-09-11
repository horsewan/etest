package com.eningqu.mapper;

import com.eningqu.domain.api.BleDevice;
import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.vo.BleDeviceBindVO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/20 17:44
 * @version    1.0
 *
 **/

public interface BleMapper extends CrudDao<BleDevice> {

    /**
     * TODO 根据包名和mac地址查询蓝牙设备信息
     * @param mac
     * @param pkgId
     * @return
     */
    BleDevice selectOneBle(@Param("mac") String mac, @Param("pkgId") Long pkgId);

    /**
     * TODO
     * @param ausn
     * @param pkgId
     * @return
     */
    BleDevice selectOneByAusn(@Param("ausn") String ausn, @Param("pkgId") Long pkgId);

    /**
     * TODO 根据主键更新秘文
     * @param id
     * @param ausn
     * @return
     */
    int updateBleDeviceAusn(@Param("id") Long id, @Param("ausn") String ausn);


    /**
     * TODO 联合查询
     * @param pkgId
     * @param bleMac
     * @return
     */
    BleDeviceBindVO QueryOneByWhere(@Param("pkgId") Long pkgId, @Param("bleMac") String bleMac);

    /**
     * TODO
     * @param bleId
     * @param bleSn
     */
    int updateBleSn(@Param("bleId") Long bleId, @Param("bleSn") String bleSn);

    int getBleDeviceCountByAusn(@Param("ausn") String ausn);

    BleDevice getBleDeviceOne();

    BleDevice getBleDeviceOneByAusn(@Param("ausn") String ausn);

}
