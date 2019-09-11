package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.Product;
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
     * TODO 更新产品库存量
     * @param id
     * @param nums
     * @return
     */
    int updateStockQuantity(@Param("id") Long id, @Param("nums") int nums);

}
