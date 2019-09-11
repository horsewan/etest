package com.eningqu.modules.api.service;

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
     * TODO 更新状态
     * @param id
     * @param status
     */
    boolean updateStatus(Long id, String status);

    /**
     * 查询产品详情
     * @param id
     * @return
     */
    String selectProductDetail(Long id);

    /***
     * TODO 保存产品详情
     * @param id
     * @param html
     * @return
     */
    boolean saveDetail(Long id, String html);
}
