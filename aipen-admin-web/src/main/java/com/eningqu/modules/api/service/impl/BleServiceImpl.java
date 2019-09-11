package com.eningqu.modules.api.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.eningqu.modules.api.mapper.UserMapper;
import com.eningqu.modules.system.service.ISysImportLogService;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.modules.api.mapper.BleMapper;
import com.eningqu.modules.api.service.IBleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/11/20 16:43
 **/

@Service
public class BleServiceImpl extends BaseServiceImpl<BleMapper, BleDevice> implements IBleService {


    @Autowired
    private ISysImportLogService sysImportLogService;

    @Autowired
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean updateStatus(Long id) throws ServiceException {
        try {
            BleDevice bleDevice = baseMapper.selectById(id);
            bleDevice.setEnableStatus(StrUtil.equalsIgnoreCase(StatusEnum.YES.getValue(), bleDevice.getEnableStatus()) ? StatusEnum.NO.getValue() : StatusEnum.YES.getValue());
            baseMapper.updateById(bleDevice);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    @Override
    public BleDevice getBleDeviceByPhone(String phone) {
        BleDevice bleDevice = new BleDevice();
        bleDevice.setMac(phone);
        return baseMapper.selectOne(bleDevice);
    }

    @Override
    public BleDevice getBleDeviceByAgentNo(String agentNo) {
        BleDevice bleDevice = new BleDevice();
        bleDevice.setAusn(agentNo);
        return baseMapper.selectOne(bleDevice);
    }


    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void updateBleDeviceByPhone(Long id, BleDevice bleDevice) {
        baseMapper.updateBleDeviceByPhone(id, bleDevice.getMac());
        //修改手机号对应的普通用户的私人特助
        if (!StringUtils.isEmpty(bleDevice.getMac())) {
            userMapper.updateAgentNoByPhone(bleDevice.getAusn().substring(0, 4) + "00", bleDevice.getMac(), "2");
        }
    }

    @Override
    public void updateBleSn(Long bleId, String bleSn) {
        BleDevice bleDevice = new BleDevice();
        bleDevice.setBleSn(bleSn);
        bleDevice.setId(bleId);
        baseMapper.updateById(bleDevice);
    }

    @Override
    public BleDevice getBleDeviceOne() {
        BleDevice bleDevice = this.selectOne(new Wrapper<BleDevice>() {
            @Override
            public String getSqlSegment() {
                return " where enable_status='Y' and ble_sn is null";
            }
        });
        return bleDevice;
    }
}
