package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.AppInfo;

/**
 *
 * @desc TODO  包名接口service
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

public interface IPkgService extends IBaseService<AppInfo> {

    /**
     * TODO 根据条件获取包名
     * @param pkgName
     * @return
     */
    AppInfo selectByWhere(String pkgName);

}
