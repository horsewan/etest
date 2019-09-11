package com.eningqu.modules.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.system.SysImportLog;
import com.eningqu.modules.system.mapper.SysImportLogMapper;
import com.eningqu.modules.system.service.ISysImportLogService;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/11/22 10:32
 * @version    1.0
 *
 **/
@Service
public class SysImportLogServiceImpl extends BaseServiceImpl<SysImportLogMapper, SysImportLog> implements ISysImportLogService{

    @Override
    public void importLog(String importMsg, String opName, String type) {
        SysImportLog importLog = new SysImportLog();
        importLog.setImportMsg(importMsg);
        importLog.setOpName(opName);
        importLog.setImportType(type);
        importLog.setOpTime(DateUtil.date());
        baseMapper.insert(importLog);
    }
}
