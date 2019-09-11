package com.eningqu.api;

import com.eningqu.common.annotation.Log;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.InfoResultTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.PayWayStatusEnum;
import com.eningqu.domain.api.*;
import com.eningqu.service.IUserCreditService;
import com.eningqu.vo.LoginInfoHelper;
import com.eningqu.common.kit.IdWorkerKit;
import com.eningqu.service.IBusinessService;
import com.eningqu.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  快递地址接口
 * @author     Yanghuangping
 * @since      2018/7/7 11:01
 * @version    1.0
 *
 **/
@RestController
@RequestMapping("/api/business")
public class BusinessController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IBusinessService businessService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserCreditService userCreditService;

    /**
     * TODO 扫码二维码获得商家信息和订单基础数据
     * @return
     */
    @PostMapping("/getBQRVal")
    @ResponseBody
    @Log("下载商户二维码信息")
    public RJson getBusinessInfoByQRVal(@RequestParam String qrval){
        Long userId = LoginInfoHelper.getUserID();

        if(qrval==null||"".equals(qrval))
            return RJson.error("商家二维码数据错误");

        String tempQrval = qrval.replace("& lt;","<").replace("& gt;",">");
        //解析密文 3>ticket>1>bId>2<<singleNo
        String[] oneArr = tempQrval.split("<<");
        if(oneArr==null||oneArr.length==0)
            return RJson.error("商家二维码数据错误");

        String[] twoArr = oneArr[0].split(">");
        if(twoArr==null||twoArr.length==0)
            return RJson.error("商家二维码数据错误");

        StringBuffer sbBusiness = new StringBuffer();
        sbBusiness.append(twoArr[2]);
        sbBusiness.append(twoArr[4]);
        sbBusiness.append(twoArr[0]);

        //检验解析得到商家数据是否合法
        BusinessInfo businessInfo = businessService.selectByTicketAndSingleNo(sbBusiness.toString(),oneArr[1]);
        if(businessInfo==null){
            return RJson.error("商家二维码不合法");
        }

        BusinessOrderResult businessOrderResult = new BusinessOrderResult();
        //创建订单（基础信息）
//        待支付	1
//        已支付	2
        Orders orders = new Orders();
        orders.setUid(userId);
        //自动生成订单号，流水号，
        String uuid = System.currentTimeMillis()+"";
        orders.setOrderNumber(IdWorkerKit.uniqueId());
        orders.setSerialNumber(new StringBuilder(uuid).reverse().toString());
        orders.setOrderStatus(OrderStatusEnum.COMPLETED.getValue());//线下付
        orders.setBusinessId(businessInfo.getId());
        orders.setOrderTime(new Date(System.currentTimeMillis()));
        orderService.insert(orders);

        //返回商家基本信息和订单信息
        businessOrderResult.setbId(businessInfo.getId());
        businessOrderResult.setoId(orders.getId());
        businessOrderResult.setbName(businessInfo.getbName());
        businessOrderResult.setbOrderNumber(orders.getOrderNumber());
        businessOrderResult.setbOrderStatus(orders.getOrderStatus());
        businessOrderResult.setbOrderTime(orders.getOrderTime());
        businessOrderResult.setbSolePrice(businessInfo.getbSolePrice());

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(userId);
        if(mUserCredit!=null){
            businessOrderResult.setbCreditTotal(mUserCredit.getVipCredit()==null?new BigDecimal(0) :mUserCredit.getVipCredit());
        }else{
            businessOrderResult.setbCreditTotal(new BigDecimal(0));
        }

        return RJson.ok("商户二维码订单创建成功").setData(businessOrderResult);
    }

    @PostMapping("/getBusinessNearFriend")
    public RJson getBusinessNearFriend(@RequestParam int page,@RequestParam int limit,@RequestParam String addressX,@RequestParam String addressY,@RequestParam String dis){

        if(addressX==null||"".equals(addressX)){
            return RJson.error("该用户经纬度参数错误");
        }
        if(addressY==null||"".equals(addressY)){
            return RJson.error("该用户经纬度参数错误");
        }
        double longitude=Double.valueOf(addressX);
        double latitude=Double.valueOf(addressY);

        //先计算查询点的经纬度范围
        double r = 6371;//地球半径千米
        double tempDis = Double.valueOf(dis);//0.5千米距离
        double dlng =  2*Math.asin(Math.sin(tempDis/(2*r))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI;//角度转为弧度
        double dlat = tempDis/r;
        dlat = dlat*180/Math.PI;
        double minlat =latitude-dlat;
        double maxlat = latitude+dlat;
        double minlng = longitude -dlng;
        double maxlng = longitude + dlng;

        DataTable<BusinessInfo> dataTable = new DataTable();
        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_x"+SearchParam.BETWEEN_START_WITH,minlng);
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_x"+SearchParam.BETWEEN_END_WITH,maxlng);
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_y"+SearchParam.BETWEEN_START_WITH,minlat);
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_y"+SearchParam.BETWEEN_END_WITH,maxlat);

        dataTable.setFields(new String[]{"id","b_name","b_type","b_sole_price","address_x","address_y"});
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        businessService.pageSearch(dataTable);

        InfoResultTable<BusinessInfoNear> infoResultTable = new InfoResultTable();
        infoResultTable.setCount(dataTable.getCount());
        infoResultTable.setLimit(dataTable.getLimit());
        infoResultTable.setPage(dataTable.getPage());

        List<BusinessInfo> businessInfoList = dataTable.getData();
        List<BusinessInfoNear> businessInfoNearList =  new ArrayList<>();
        if(businessInfoList!=null&&businessInfoList.size()>0){
            for (BusinessInfo businessInfo:businessInfoList) {
                BusinessInfoNear businessInfoNear = new BusinessInfoNear();
                businessInfoNear.setAddressX(businessInfo.getAddressX());
                businessInfoNear.setAddressY(businessInfo.getAddressY());
                businessInfoNear.setbName(businessInfo.getbName());
                businessInfoNear.setId(businessInfo.getId());
                businessInfoNear.setbType(businessInfo.getbType());
                businessInfoNear.setbSolePrice(businessInfo.getbSolePrice());
                businessInfoNearList.add(businessInfoNear);
            }
            infoResultTable.setData(businessInfoNearList);
        }else{
            return RJson.ok();
        }
        return RJson.ok().setData(infoResultTable);
    }

    @PostMapping("/getBusinessFinance")
    @ResponseBody
    @Log("商户Finance")
    public RJson getBusinessFinance(){
        String mobile = LoginInfoHelper.getUserMobile();
        BusinessInfo businessInfo = businessService.selectByPhone(mobile);
        if(businessInfo==null){
            return RJson.error("未查询到该商家结算数据");
        }
        return RJson.ok().setData(businessService.selectBusinessByPhone(mobile));
    }

    /**
     * 获取余额明细记录
     *
     * @return
     */
    @PostMapping("/getBalanceList")
    @Log("商户余额明细列表")
    public RJson getBalanceList(@RequestParam(value = "page",required = true) int page, @RequestParam(value = "limit",required = true) int limit) {

        return businessService.selectUserBalanceList(page,limit);
    }
}
