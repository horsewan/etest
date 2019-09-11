package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.domain.api.Product;

/**
 *
 * @desc TODO  产品service接口
 * @author     Yanghuangping
 * @since      2018/7/5 15:22
 * @version    1.0
 *
 **/

public interface IProductService extends IBaseService<Product>{

    /**
     * 更新产品库存量
     * @param id
     * @param nums
     */
    boolean updateStockQuantity(Long id, int nums);
}
