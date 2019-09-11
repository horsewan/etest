package com.eningqu.service;

import com.eningqu.domain.api.BleDevice;
import com.eningqu.common.base.service.IBaseService;
import com.eningqu.vo.BleDeviceBindVO;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/20 17:45
 * @version    1.0
 *
 **/

public interface IBleService extends IBaseService<BleDevice>{

    /**
     * TODO 根据Mac查询蓝牙设备
     * @param mac
     * @param pkgId
     * @return
     */
    BleDevice selectOneByMac(String mac, Long pkgId);

    /**
     * TODO 根据ausn查询蓝牙设备
     * @param ausn
     * @param id
     * @return
     */
    BleDevice selectOneByAusn(String ausn, Long id);

    /**
     * TODO 首次更新秘文
     * @param id
     * @param ausn
     * @return
     * */
    boolean updateBleDeviceAusn(Long id,String ausn);

    /**
     * TODO 联合查询
     * @param pkgId        应用包名ID
     * @param bleMac       蓝牙设备MAC
     * @return
     */
    BleDeviceBindVO QueryOneByWhere(Long pkgId, String bleMac);


    BleDevice getBleDeviceOne();

    BleDevice getBleDeviceOneByAusn(String ausn);

    int updateBleSn(Long bleId, String bleSn);

    int getBleDeviceCountByAusn(String ausn);
}
