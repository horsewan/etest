package com.eningqu.modules.api.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.SysAPIInfo;

public interface SysAPIMapper extends CrudDao<SysAPIInfo> {


    SysAPIInfo selectSysAPIBySta();

    SysAPIInfo selectUserRegBySta();

}
