package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.Product;
import com.eningqu.mapper.ProductMapper;
import com.eningqu.service.IProductService;
import org.springframework.stereotype.Service;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/6 16:23
 * @version    1.0
 *
 **/
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements IProductService {

    /**
     * TODO 更新产品库存量
     *
     * @param id
     * @param nums
     */
    @Override
    public boolean updateStockQuantity(Long id, int nums) {
        int count = baseMapper.updateStockQuantity(id, nums);
        return count == 1 ? true : false;
    }
}
