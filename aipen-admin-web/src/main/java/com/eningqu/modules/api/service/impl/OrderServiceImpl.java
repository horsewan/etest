package com.eningqu.modules.api.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.enums.FinanceTypeEnum;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.SysUserTypeEnum;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.kit.DateTimeKit;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.domain.api.*;
import com.eningqu.modules.api.mapper.OrdersMapper;
import com.eningqu.modules.api.service.IOrderService;
import com.eningqu.modules.api.vo.OrderDetailVO;
import com.eningqu.modules.api.vo.OrdersVO;
import com.eningqu.modules.system.vo.OrderExcelVo;
import com.eningqu.modules.system.vo.ShiroUser;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.text.resources.cldr.ti.FormatData_ti_ER;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/7/6 16:20
 **/
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrdersMapper, Orders> implements IOrderService {

    /**
     * TODO 表格分页查询
     *
     * @param dataTable
     */
    @Override
    public void tablePageSearch(DataTable<Orders> dataTable) {
        Page<OrdersVO> page = new Page<>(dataTable.getPage(), dataTable.getLimit());
        //baseMapper.selectListPage(page, );
    }

    /**
     * TODO 查询订单详情
     *
     * @param orderNumber
     * @return
     */
    @Override
    public List<OrderDetailVO> selectOrderDetaiList(String orderNumber) {
        return baseMapper.selectOrderDetailList(orderNumber);
    }

    /**
     * TODO 更新物流信息
     *
     * @param orderNumber
     * @param expCode
     * @throws ServiceException
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public void updateExp(String orderNumber, String expCode, String expNo) throws ServiceException {
        int count = baseMapper.updateExp(orderNumber, expCode, expNo);
        if (count != 1) {
            throw new ServiceException("物流信息更新失败");
        }
    }

    /**
     * 查询结算列表
     */
    @Override
    public void selectFinanceList(DataTable<FinanceListResult> dataTable) {
        Page<FinanceListResult> page = new Page<FinanceListResult>(dataTable.getPage(), dataTable.getLimit());
        String userType = String.valueOf(dataTable.getSearchParams().get("userType"));
        //普通用户结算
        if (FinanceTypeEnum.Normal.getValue().equals(userType)) {
            List<FinanceListResult> resultList = baseMapper.selectFinanceList(page, dataTable.getSearchParams());
            resultList.forEach(item -> {
                item.setMoney(castDouble(item.getShopPercentage().multiply(new BigDecimal(0.003))));
                item.setUserPercentage(castDouble(item.getAmountRealpay().multiply(new BigDecimal(0.03))));
//                item.setShopSort(baseMapper.selectBusinessNameByUser(item.getuId()));
            });
            page.setRecords(resultList);
        }
        //商家结算
        if (Objects.equals(userType, FinanceTypeEnum.Shop.getValue())) {
            List<FinanceListResult> resultList = baseMapper.selectShopFinanceList(page, dataTable.getSearchParams());
            resultList.forEach(item -> {
//                List<Integer> list = baseMapper.selectShopActiveUser(item.getuId());
                item.setActiveUser(0);
            });
            page.setRecords(resultList);
        }
        //市代结算
        if (Objects.equals(userType, FinanceTypeEnum.City.getValue())) {
            List<FinanceListResult> resultList = baseMapper.selectCityFinanceList(page, dataTable.getSearchParams());
            resultList.forEach(item -> {
                item.setMoney(castDouble(item.getShopPercentage().multiply(new BigDecimal(0.05))));
                item.setUserPercentage(castDouble(item.getAmountRealpay().multiply(new BigDecimal(0.44))));
            });
            page.setRecords(resultList);
        }
        //团代结算
        if (Objects.equals(userType, FinanceTypeEnum.Team.getValue())) {
            List<FinanceListResult> resultList = baseMapper.selectTeamFinanceList(page, dataTable.getSearchParams());
            resultList.forEach(item -> {
//                BigDecimal result1 = item.getAmountRealpay().multiply(new BigDecimal(0.01));
//                item.setDividedMoney(castDouble(result1.multiply(new BigDecimal(0.49))));
                item.setMoney(castDouble(item.getShopPercentage().multiply(new BigDecimal(0.01))));
                item.setUserPercentage(castDouble(item.getAmountRealpay().multiply(new BigDecimal(0.001))));
            });
            page.setRecords(resultList);
        }

        dataTable.setData(page.getRecords());
        dataTable.setCount(page.getTotal());
    }

    @Override
    public BigDecimal selectCityTotal() {
        ShiroUser shiroUser = ShiroKit.loginInfo();
        String agentNo = "";
        if (Objects.equals(SysUserTypeEnum.ORDINARY.getValue(), shiroUser.getUserType())) {
            agentNo = shiroUser.getLoginName();
        }
        String startDate = DateTimeKit.getDayOnThisMonth();//开始日期
        String endDate = DateTimeKit.format(DateTimeKit.getDate(), DateTimeKit.DATE_TIME_PATTERN);//结束日期
        return baseMapper.selectCityTotal(agentNo, startDate, endDate);
    }

    /**
     * 功能描述:超过改时间订单则为已取消状态。并退还抵扣金额
     *
     * @Param
     * @return:
     * @Author: lvbu
     * @Date: 2019/9/3 17:31
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnMoneyOnNoPay() {
        int pageSize = 1;
        int pageLimit = 30;
        DataTable<Orders> dataTable = new DataTable();
        //1.查询支付状态为未支付的订单信息
        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_EQ + "order_status", OrderStatusEnum.UNPAID.getValue());
        dataTable.setFields(new String[]{"id", "order_time", "order_number", "uid"});
        dataTable.setSearchParams(searchParams);
        dataTable.setPage(pageSize);
        dataTable.setLimit(pageLimit);
        this.pageSearch(dataTable);
        List<Orders> ordersList = new ArrayList<>();
        orderStatusUnNoPay(dataTable.getData(), ordersList);

        //2.分页查询剩余支付超时订单
        int size = dataTable.getCount();
        int num = size % pageLimit == 0 ? (size / pageLimit) : (size / pageLimit + 1);
        for (int i = 2; i <= num; i++) {
            dataTable.setFields(new String[]{"id", "order_time", "order_number", "uid"});
            dataTable.setSearchParams(searchParams);
            dataTable.setPage(i);
            dataTable.setLimit(pageLimit);
            this.pageSearch(dataTable);
            if (dataTable.getData().size() > 0) {
                orderStatusUnNoPay(dataTable.getData(), ordersList);
            }
        }
        //3.批次修改已超时的订单状态为已取消
        size = ordersList.size();
        num = size % pageLimit == 0 ? (size / pageLimit) : (size / pageLimit + 1);
        int start = 0;
        int end = 0;
        List<Orders> codeList = new ArrayList();
        for (int i = 1; i <= num; i++) {
            end = (i * pageLimit) > size ? size : (i * pageLimit);
            start = (i - 1) * pageLimit;
            codeList = ordersList.subList(start, end);
            baseMapper.updateOrderStatus(codeList);
            if (codeList.size() > 0) {
                baseMapper.insertUserCreditRecord(codeList);
                baseMapper.updateUserVipCredit(codeList);
            }


        }
    }

    @Override
    public List<OrderExcelVo> selectExcelList(Map param) {

        return baseMapper.selectExcelList(param);
    }


    /**
     * 功能描述: 统计支付超时订单
     *
     * @Param List<Orders>,List<Long>
     * @Author: lvbu
     * @Date: 2019/9/4 15:43
     */
    public void orderStatusUnNoPay(List<Orders> ordersList, List<Orders> list) {
        Date end = new Date();
        for (Orders orders : ordersList) {
            Date begin = orders.getOrderTime();
            try {
                //计算时间差
                long diff = end.getTime() - begin.getTime();
                //计算天数
                long days = diff / (1000 * 60 * 60 * 24);
                //计算小时
                long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                //计算分钟
                long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
                //输出
                //1.判断下单时间是否超过15分钟
                if (days > 0 || minutes > 15 || hours > 0) {
                    list.add(orders);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能描述: 保留两位小数点
     *
     * @Param BigDecimal
     * @Author: lvbu
     * @Date: 2019/9/4 15:45
     */
    public BigDecimal castDouble(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return new BigDecimal(decimalFormat.format(bigDecimal));
    }


}
