package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.AppInfo;
import com.eningqu.mapper.PkgMapper;
import com.eningqu.service.IPkgService;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  包名接口service实现类
 * @author     Yanghuangping
 * @since      2018/9/10 11:12
 * @version    1.0
 *
 **/

@Service
public class PkgServiceImpl extends BaseServiceImpl<PkgMapper, AppInfo> implements IPkgService{


    /**
     * TODO 根据条件获取包名
     *
     * @param pkgName
     * @return
     */
    @Override
    public AppInfo selectByWhere(String pkgName) {
        return baseMapper.selectByWhere(pkgName);
    }
}
