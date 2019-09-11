package com.eningqu.modules.api.service;

import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.AppInfo;

import java.util.List;

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
     * TODO 保存app应用信息
     * @param appInfo
     * @param languageIds
     * @return
     * @throws ServiceException
     */
    boolean saveAppInfo(AppInfo appInfo, List<Long> languageIds) throws ServiceException;

    AppInfo getAppInfoByPkg(String appPkg);

    AppInfo getAppInfoBySig(String signature);

    void updateAppInfoPhone(Long id,String phone,AppInfo appInfo);

}
