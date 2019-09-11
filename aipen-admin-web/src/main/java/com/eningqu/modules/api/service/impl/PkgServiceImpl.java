package com.eningqu.modules.api.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.AppInfo;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.modules.api.mapper.PkgMapper;
import com.eningqu.modules.api.mapper.UserMapper;
import com.eningqu.modules.api.service.IPkgService;
import com.eningqu.modules.api.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  包名接口service实现类
 * @since 2018/9/10 11:12
 **/

@Service
public class PkgServiceImpl extends BaseServiceImpl<PkgMapper, AppInfo> implements IPkgService {


    @Autowired
    private UserMapper userMapper;

    /**
     * TODO 保存app应用信息
     * 去掉了language操作
     *
     * @param appInfo
     * @param languageIds
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    public boolean saveAppInfo(AppInfo appInfo, List<Long> languageIds) throws ServiceException {
        try {
            if (appInfo.getId() == null) {
                baseMapper.insert(appInfo);
            } else {
                baseMapper.updateById(appInfo);
            }
        } catch (Exception e) {
            logger.error("App应用保存异常，{}", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return true;
    }

    @Override
    public AppInfo getAppInfoByPkg(String appPkg) {
        return baseMapper.getAppInfoByPkg(appPkg);
    }

    @Override
    public AppInfo getAppInfoBySig(String signature) {
        return baseMapper.getAppInfoBySig(signature);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void updateAppInfoPhone(Long id, String phone, AppInfo appInfo) {
        baseMapper.updateAppInfoPhone(id, phone, appInfo.getAppName());
        //修改手机号对应的普通用户的私人特助
        if(!StringUtils.isEmpty(phone)){
            userMapper.updateAgentNoByPhone(appInfo.getAppPkg().substring(0,2)+"0000",phone,"1");
        }
    }
}
