package com.eningqu.mapper;

import com.eningqu.domain.api.AppInfo;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  包名mapper
 * @author     Yanghuangping
 * @since      2018/9/10 11:11
 * @version    1.0
 *
 **/

public interface PkgMapper extends CrudDao<AppInfo> {

    /**
     * TODO 根据包名和应用签名查询
     * @param pkgName
     * @return
     */
    AppInfo selectByWhere(@Param("pkgName") String pkgName);

}
