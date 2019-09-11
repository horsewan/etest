package com.eningqu.modules.system.mapper;

import com.eningqu.domain.system.SysDict;
import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.modules.system.vo.DictVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO 字典表 Mapper 接口
 */
public interface SysDictMapper extends CrudDao<SysDict> {

    /**
     * TODO 根据字典组编码查询
     * @param groupCode
     * @param delStatus
     * @return
     */
    List<DictVO> queryDicts(@Param("groupCode") String groupCode, @Param("delStatus") String delStatus);

    /**
     * TODO 检测字典组码是否存在
     * @param groupCode
     * @param pid
     * @return
     */
    int checkGroup(@Param("groupCode") String groupCode, @Param("pid") Long pid);

    /**
     * TODO 检查字典码是否存在
     * @param groupCode
     * @param valueCode
     * @return
     */
    int checkDict(@Param("groupCode")String groupCode, @Param("valueCode") String valueCode);
}
