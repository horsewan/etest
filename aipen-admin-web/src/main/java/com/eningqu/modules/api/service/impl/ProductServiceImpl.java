package com.eningqu.modules.api.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.Product;
import com.eningqu.modules.api.mapper.ProductMapper;
import com.eningqu.modules.api.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     * TODO 更新状态
     *
     * @param id
     * @param status
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    public boolean updateStatus(Long id, String status) {
        int count = baseMapper.updateStatus(id, status);
        if(count > 1){
            throw new ServiceException("更新异常");
        }
        return count == 1 ? true : false;
    }

    /**
     * TODO 查询产品详情
     *
     * @param id
     * @return
     */
    @Override
    public String selectProductDetail(Long id) {
        return baseMapper.selectDetail(id);
    }

    /***
     * TODO 保存产品详情
     * @param id
     * @param html
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean saveDetail(Long id, String html) {

        int count = baseMapper.saveDetail(id, html);

        if(count != 1){
            throw new ServiceException("保存失败");
        }

        return count == 1 ? true : false;
    }
}
