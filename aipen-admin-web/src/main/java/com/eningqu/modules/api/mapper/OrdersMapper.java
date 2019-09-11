package com.eningqu.modules.api.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.eningqu.domain.api.FinanceListResult;
import com.eningqu.domain.api.Orders;
import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.modules.api.vo.OrderDetailVO;
import com.eningqu.modules.system.vo.OrderExcelVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/7/6 16:15
 **/

public interface OrdersMapper extends CrudDao<Orders> {

    /**
     * TODO 查询订单详情
     *
     * @param orderNumber
     * @return
     */
    List<OrderDetailVO> selectOrderDetailList(@Param("orderNumber") String orderNumber);

    /**
     * TODO 更新物流信息
     *
     * @param orderNumber
     * @param expCode
     * @param expNo
     * @return
     */
    int updateExp(@Param("orderNumber") String orderNumber, @Param("expCode") String expCode, @Param("expNo") String expNo);

    /**
     * 查询普通用户结算管理列表
     *
     * @return
     */
    List<FinanceListResult> selectFinanceList(Pagination pagination, @Param("searchMap") Map searchMap);

    /**
     * 查询用户常访问的3个商家
     *
     * @param uId
     * @return
     */
    String selectBusinessNameByUser(@Param("uId") Integer uId);

    /**
     * 查询市代结算管理列表
     *
     * @return
     */
    List<FinanceListResult> selectCityFinanceList(Pagination pagination, @Param("searchMap") Map searchMap);

    /**
     * 查询团代结算管理列表
     *
     * @return
     */
    List<FinanceListResult> selectTeamFinanceList(Pagination pagination, @Param("searchMap") Map searchMap);

    /**
     * 查询商家结算管理列表
     *
     * @return
     */
    List<FinanceListResult> selectShopFinanceList(Pagination pagination, @Param("searchMap") Map searchMap);

    /**
     * 查询商家下的用户
     *
     * @param businessId
     * @return
     */
    List<Integer> selectShopActiveUser(@Param("businessId") Integer businessId);

    /**
     * 查询当月交易总额
     *
     * @param agentNo
     * @return
     */
    BigDecimal selectCityTotal(@Param("agentNo") String agentNo, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 功能描述: 批量修改订单状态为已取消
     *
     * @Param ordersList 已过期订单
     * @Author: lvbu
     * @Date: 2019/9/4 15:24
     */
    void updateOrderStatus(@Param("list") List<Orders> ordersList);

    /**
     * 功能描述: 退还用户取消订单零钱总额
     *
     * @Author: lvbu
     * @Date: 2019/9/6 19:27
     */
    void updateUserVipCredit(@Param("list") List<Orders> ordersList);

    /**
     * 功能描述: 返回用户已取消的订单抵扣红包金额明细
     *
     * @Author: lvbu
     * @Date: 2019/9/6 19:31
     */
    void insertUserCreditRecord(@Param("list") List<Orders> ordersList);

    List<OrderExcelVo> selectExcelList(@Param("param") Map param);
}
