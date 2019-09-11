package com.eningqu.modules.api.mapper;

import com.eningqu.domain.api.AppInfo;
import com.eningqu.common.base.mapper.CrudDao;

/**
 *
 * @desc TODO  应用mapper
 * @author     Yanghuangping
 * @since      2018/9/10 11:11
 * @version    1.0
 *
 **/

public interface PkgMapper extends CrudDao<AppInfo> {

    AppInfo getAppInfoByPkg(String appPkg);

    AppInfo getAppInfoBySig(String signature);

    void updateAppInfoPhone(Long id, String phone,String name);

}
