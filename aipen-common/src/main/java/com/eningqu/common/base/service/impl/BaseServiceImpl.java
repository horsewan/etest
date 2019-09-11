package com.eningqu.common.base.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.base.vo.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 * @desc TODO  service基类实现
 * @author     Yanghuangping
 * @date       2018/4/18 9:50
 * @version    1.0
 *
 **/
public class BaseServiceImpl <M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T>{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * TODO 分页查询
     *
     * @param dataTable
     * @return
     */
    @Override
    public DataTable<T> pageSearch(DataTable<T> dataTable) {

        Page<T> page = new Page<T>(dataTable.getPage(), dataTable.getLimit());

        Condition cnd = new Condition();
        if(dataTable.getFields() != null && dataTable.getFields().length != 0){
            cnd.setSqlSelect(dataTable.getFields());
        }

        // 组装查询条件
        loadSearchParam(dataTable.getSearchParams(), cnd);

        // 排序
        if(dataTable.getSort() != null){
            for (Map.Entry<String, Boolean> entry : dataTable.getSort().entrySet()) {
                cnd.orderBy(entry.getKey(), entry.getValue());
            }
        }

        // 分页查询
        selectPage(page, cnd);

        dataTable.setCount(page.getTotal());
        dataTable.setData(page.getRecords());

        dataTable.setFields(null);
        dataTable.setSearchParams(null);

        return dataTable;
    }

    /**
     * TODO 查询条件组装
     * @param searchParams
     * @param cnd
     */
    private void loadSearchParam(Map<String, Object> searchParams, Condition cnd) {
        if (null != searchParams) {

            Object v, v1;
            String field;

            for (String k : searchParams.keySet()) {
                v = searchParams.get(k);
                if (idLoadCnd(SearchParam.SEARCH_EQ, k, v)) {
                    cnd.eq(k.split(SearchParam.SEARCH_EQ)[1], v);
                } else if (idLoadCnd(SearchParam.SEARCH_LLIKE, k, v)) {
                    cnd.like(k.split(SearchParam.SEARCH_LLIKE)[1], String.valueOf(v), SqlLike.LEFT);
                } else if (idLoadCnd(SearchParam.SEARCH_RLIKE, k, v)) {
                    cnd.like(k.split(SearchParam.SEARCH_RLIKE)[1], String.valueOf(v), SqlLike.RIGHT);
                } else if (idLoadCnd(SearchParam.SEARCH_LIKE, k, v)) {
                    cnd.like(k.split(SearchParam.SEARCH_LIKE)[1], String.valueOf(v));
                } else if (idLoadCndNull(SearchParam.SEARCH_ISNULL,k)){//TODO add is null
                    cnd.isNull(k.split(SearchParam.SEARCH_ISNULL)[1]);
                } else if (idLoadCndNull(SearchParam.SEARCH_ISNOTNULL,k)){//TODO add is not null
                    cnd.isNotNull(k.split(SearchParam.SEARCH_ISNOTNULL)[1]);
                }
            }

            for (String k : searchParams.keySet()) {
                v = searchParams.get(k);
                if(startsWith(SearchParam.SEARCH_BETWEEN, k) && endsWith(SearchParam.BETWEEN_START_WITH, k)){
                    field = k.replace(SearchParam.SEARCH_BETWEEN, "").replace(SearchParam.BETWEEN_START_WITH, "");
                    v1 = searchParams.get(SearchParam.SEARCH_BETWEEN + field + SearchParam.BETWEEN_END_WITH);
                    if(null != v && v.toString().length() > 0){
                        if(null != v1 && v1.toString().length() > 0){
                            cnd.between(field, v, v1);
                        }else{
                            cnd.ge(field, v);
                        }
                    }else{
                        if(null != v1 && v1.toString().length() > 0){
                            cnd.le(field, v1);
                        }
                    }
                }
            }
        }
    }

    /**
     * TODO 是否加载 查询条件
     * @param cnd
     * @param k
     * @param v
     * @return
     */
    private boolean idLoadCnd(String cnd, String k, Object v) {
        return k.startsWith(cnd) && null != v && v.toString().length() > 0;
    }

    private boolean idLoadCndNull(String cnd, String k) {
        return k.startsWith(cnd);
    }

    private boolean startsWith(String cnd, String k) {
        return k.startsWith(cnd);
    }

    private boolean endsWith(String cnd, String k){
        return k.endsWith(cnd);
    }
}
