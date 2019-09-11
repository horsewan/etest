package com.eningqu.modules.api.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.SysAPIInfo;

/**
 *
 * @desc TODO  API模块总控制service接口
 * @author     Yanghuangping
 * @date       2018/5/2 19:38
 * @version    1.0
 */
public interface ISysAPIService extends IBaseService<SysAPIInfo> {


    SysAPIInfo selectSysAPIBySta();

    SysAPIInfo selectUserRegBySta();

}
