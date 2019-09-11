package com.eningqu.modules.system.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.system.SysImportLog;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/11/22 10:26
 * @version    1.0
 *
 **/

public interface ISysImportLogService extends IBaseService<SysImportLog>{

    void importLog(String importMsg, String opName, String type);

}
