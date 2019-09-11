package com.eningqu.modules.api.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.FinanceListResult;
import com.eningqu.domain.api.Orders;
import com.eningqu.modules.api.vo.OrderDetailVO;
import com.eningqu.modules.system.vo.OrderExcelVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  订单service
 * @author     Yanghuangping
 * @since      2018/7/5 15:26
 * @version    1.0
 *
 **/

public interface IOrderService extends IBaseService<Orders>{

    /**
     * TODO 表格分页查询
     * @param dataTable
     */
    void tablePageSearch(DataTable<Orders> dataTable);

    /**
     * TODO 查询订单详情
     * @param orderNumber
     * @return
     */
    List<OrderDetailVO> selectOrderDetaiList(String orderNumber);

    /**
     * TODO 更新物流信息
     * @param orderNumber
     * @param expCode
     * @throws ServiceException
     */
    void updateExp(String orderNumber, String expCode, String expNo) throws ServiceException;

    /**
     * 查询结算列表
     */
    void  selectFinanceList(DataTable<FinanceListResult> dataTable);

    /**
     * 查看当月市级代理交易总额
     * @return
     */
    BigDecimal selectCityTotal();


    void returnMoneyOnNoPay();

    /**
    * 功能描述:导出订单列表
    * @Author: lvbu
    * @Date: 2019/9/7 15:53
    */
    List<OrderExcelVo> selectExcelList(Map param);
}
