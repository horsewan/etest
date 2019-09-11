package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.SysAPIInfo;

public interface SysAPIMapper extends CrudDao<SysAPIInfo> {


    SysAPIInfo selectSysAPIBySta();
    SysAPIInfo selectUserRegBySta();
}
