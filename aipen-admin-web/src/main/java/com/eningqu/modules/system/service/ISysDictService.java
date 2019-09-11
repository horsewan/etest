package com.eningqu.modules.system.service;

import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.system.SysDict;
import com.eningqu.common.base.service.IBaseService;
import com.eningqu.modules.system.vo.DictVO;

import java.util.List;

/**
 * @desc TODO 字典servie接口
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 19:43
 **/
public interface ISysDictService extends IBaseService<SysDict> {

    /**
     * TODO 查询字典列表
     * @return
     */
    List<SysDict> selectDicts();

    /**
     * TODO 删除字典数据  若是删除字典组 则连同删除字典信息
     * @param id
     * @return
     */
    boolean delDict(Long id) throws ServiceException;


    /**
     * TODO 根据字典组编码查询
     * @param groupCode
     * @return
     */
    List<DictVO> queryDictByGroupCode(String groupCode);

    /**
     * TODO 添加字典组
     * @param sysDict
     */
    void insertDictGroup(SysDict sysDict) throws ServiceException;

    /**
     * TODO 更新字典组
     * @param sysDict
     */
    void updateDictGroup(SysDict sysDict) throws ServiceException;

    /**
     * TODO 添加字典
     * @param sysDict
     * @throws ServiceException
     */
    void insertDict(SysDict sysDict) throws ServiceException;
}
