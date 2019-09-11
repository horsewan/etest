package com.eningqu.modules.system.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.system.SysCity;

/**
 * @desc TODO  城市servie接口
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 19:43
 **/
public interface ISysCityService extends IBaseService<SysCity> {

    SysCity selectByCityName(String cityName);

    SysCity selectByShengName(String shengName);

}
