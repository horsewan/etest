package com.eningqu.modules.api.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.BleDevice;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/11/20 16:43
 **/

public interface IBleService extends IBaseService<BleDevice> {

    /**
     * TODO 更新可用状态
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    boolean updateStatus(Long id) throws ServiceException;

    BleDevice getBleDeviceByPhone(String phone);

    BleDevice getBleDeviceByAgentNo(String agentNo);

    void updateBleDeviceByPhone(Long id, BleDevice bleDevice);

    void updateBleSn(Long bleId, String bleSn);

    BleDevice getBleDeviceOne();

}
