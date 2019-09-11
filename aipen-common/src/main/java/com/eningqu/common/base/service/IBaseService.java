package com.eningqu.common.base.service;

import com.baomidou.mybatisplus.service.IService;
import com.eningqu.common.base.vo.DataTable;

/**
 * @desc TODO service基类
 * @author Yanghuangping
 * @version 1.0
 * @date 2018/4/18 9:50
 **/
public interface IBaseService <T> extends IService<T> {

    /**
     * 分页查询
     * @param dataTable
     * @return
     */
    DataTable<T> pageSearch(DataTable<T> dataTable);

}
