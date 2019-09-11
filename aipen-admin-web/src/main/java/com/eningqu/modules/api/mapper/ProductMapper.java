package com.eningqu.modules.api.mapper;

import com.eningqu.domain.api.Product;
import com.eningqu.common.base.mapper.CrudDao;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/6 16:15
 * @version    1.0
 *
 **/

public interface ProductMapper extends CrudDao<Product> {

    /**
     * TODO 更新产品状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * TODO 产品详情
     * @param id
     * @return
     */
    String selectDetail(@Param("id") Long id);

    /**
     * TODO 保存产品详情
     * @param id
     * @param html
     * @return
     */
    int saveDetail(@Param("id") Long id, @Param("html") String html);
}
