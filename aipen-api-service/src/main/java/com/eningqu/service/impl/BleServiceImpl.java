package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.mapper.BleMapper;
import com.eningqu.service.IBleService;
import com.eningqu.vo.BleDeviceBindVO;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/20 17:45
 * @version    1.0
 *
 **/

@Service
public class BleServiceImpl extends BaseServiceImpl<BleMapper, BleDevice> implements IBleService{

    /**
     * TODO 根据条件查询蓝牙设备
     *
     * @param mac
     * @param pkgId
     * @return
     */
    @Override
    public BleDevice selectOneByMac(String mac, Long pkgId) {
        return baseMapper.selectOneBle(mac, pkgId);
    }

    /**
     * TODO 根据ausn查询蓝牙设备
     *
     * @param ausn
     * @param pkgId
     * @return
     */
    @Override
    public BleDevice selectOneByAusn(String ausn, Long pkgId) {
        return baseMapper.selectOneByAusn(ausn, pkgId);
    }

    /**
     * TODO 更新首次登录的设备信息
     * 返回0表示更新失败
     * */
    @Override
    public boolean updateBleDeviceAusn(Long id, String ausn) {
        int count = baseMapper.updateBleDeviceAusn(id, ausn);
        return count == 0 ? false : true;
    }

    /**
     * TODO 联合查询
     *
     * @param pkgId  应用包名ID
     * @param bleMac 蓝牙设备MAC
     * @return
     */
    @Override
    public BleDeviceBindVO QueryOneByWhere(Long pkgId, String bleMac) {
        return baseMapper.QueryOneByWhere(pkgId, bleMac);
    }

    @Override
    public BleDevice getBleDeviceOne() {
        return baseMapper.getBleDeviceOne();
    }

    @Override
    public BleDevice getBleDeviceOneByAusn(String ausn) {
        return baseMapper.getBleDeviceOneByAusn(ausn);
    }

    @Override
    public int updateBleSn(Long bleId, String bleSn) {
        return baseMapper.updateBleSn(bleId,bleSn);
    }

    @Override
    public int getBleDeviceCountByAusn(String ausn) {
        return baseMapper.getBleDeviceCountByAusn(ausn);
    }
}
