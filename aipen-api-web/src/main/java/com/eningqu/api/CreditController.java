package com.eningqu.api;

import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.vo.LoginInfoHelper;
import com.eningqu.domain.api.UserCredit;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.service.IOrderService;
import com.eningqu.service.IUserCreditRecordService;
import com.eningqu.service.IUserCreditService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @desc TODO  红包（积分）API接口
 * @author     Yanghuangping
 * @date       2018/5/8 11:36
 * @version    1.0
 *
 **/
@RestController
@RequestMapping("/api/credit")
public class CreditController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserCreditService userCreditService;

    @Autowired
    private IUserCreditRecordService userCreditRecordService;

    @Autowired
    private IOrderService orderService;

    /**
     * 获取当前用户可用红包总额
     * @return
     */
    @PostMapping("/getCreditByUserId")
    public RJson selectByUid(){
        Long userId = LoginInfoHelper.getUserID();
        if(userId==null){
            return RJson.error("该用户未登录");
        }
        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(userId);
        if(mUserCredit!=null){
            return RJson.ok().setData(mUserCredit.getVipCredit()==null?0:mUserCredit.getVipCredit());
        }else{
            return RJson.ok().setData(0);
        }
    }

    /**
     * 领取红包
     * 当天只能领取一次
     * @return
     */
    @PostMapping("/updateCreditByUserId")
    public RJson updateCreditByUserId(@RequestParam String creditVal, @RequestParam(required = false) long hongbaoId,@RequestParam int creditSta){
        Long userId = LoginInfoHelper.getUserID();
        if(creditVal==null||"".equals(creditVal)){
            return RJson.error("该用户红包金额不合规。");
        }
//        if(creditVal1.doubleValue()<=0.00){
//            return RJson.error("该用户红包金额不合规。");
//        }
        if(creditSta!=1&&creditSta!=2){
            return RJson.error("该用户操作红包不合法。");
        }
        if (creditSta==1){
            if(hongbaoId<0){
                return RJson.error("该用户红包不存在。");
            }
        }

        BigDecimal creditValReq = new BigDecimal(creditVal);
        if(creditValReq!=null){
            creditValReq = creditValReq.setScale(2);
        }
        //判断是否有红包数据
        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(userId);
        if(mUserCredit==null){
            mUserCredit = new UserCredit();
            mUserCredit.setUid(userId);
            mUserCredit.setVipLevel(0);
            mUserCredit.setVipCredit(new BigDecimal(0));
            userCreditService.insert(mUserCredit);
            if (creditSta==2){
                return RJson.error("该用户没有可使用红包。");
            }
        }

        if (creditSta==2){
            //扣除红包（积分）  creditVal
            BigDecimal vipCredit = null;
            BigDecimal tempUserCreditVal = new BigDecimal(0);
            if(mUserCredit.getVipCredit()!=null){
                tempUserCreditVal = mUserCredit.getVipCredit().multiply(new BigDecimal(100)).setScale(2);
            }
            logger.error("++++++++++++++++++++++++++，tempUserCreditVal：{}", tempUserCreditVal);

            if(tempUserCreditVal!=null){
                if(tempUserCreditVal.doubleValue()>=creditValReq.doubleValue()){
                    vipCredit = new BigDecimal(tempUserCreditVal.doubleValue()-creditValReq.doubleValue());
                    BigDecimal tempVipCredit = vipCredit.divide(new BigDecimal(100));
                    mUserCredit.setVipCredit(tempVipCredit);
                    userCreditService.updateById(mUserCredit);
                }else{
                    return RJson.error("该用户红包不够抵扣折扣金额。");
                }
            }else{
                return RJson.error("该用户红包不够抵扣折扣金额。");
            }

            UserCreditRecord userCreditRecord = new UserCreditRecord();
            userCreditRecord.setUid(userId);
            String vipCreditStr = String.valueOf(creditValReq.divide(new BigDecimal(100)));

            userCreditRecord.setVipCredit(new BigDecimal(vipCreditStr));
            userCreditRecord.setVipHongbao(hongbaoId);
            userCreditRecord.setVipStatus(creditSta);
            userCreditRecordService.insert(userCreditRecord);

            return RJson.ok("该用户成功使用该红包。").setData(vipCredit);
        }
        //判断用户当前红包是否已领取过
        UserCreditRecord userCreditRecord = userCreditRecordService.selectUserCreditRecordById(userId,hongbaoId);
        if(userCreditRecord==null){
            userCreditRecord = new UserCreditRecord();
            userCreditRecord.setUid(userId);
            userCreditRecord.setVipCredit(creditValReq.divide(new BigDecimal(100)));
            userCreditRecord.setVipHongbao(hongbaoId);
            userCreditRecord.setVipStatus(creditSta);
            userCreditRecordService.insert(userCreditRecord);
        }else{
            return RJson.error("该用户已领取过该红包。");
        }

        BigDecimal tempCreditVal = mUserCredit.getVipCredit();
        if(tempCreditVal!=null){
            tempCreditVal = tempCreditVal.multiply(new BigDecimal(100)).add(creditValReq);
            BigDecimal addCreditVal = tempCreditVal.divide(new BigDecimal(100));
            mUserCredit.setVipCredit(addCreditVal);
        }else{
            mUserCredit.setVipCredit(creditValReq.divide(new BigDecimal(100)));
        }

        boolean updateCredit = userCreditService.updateOne(mUserCredit);
        return RJson.ok().setCode(updateCredit?1:2).setSuccess(updateCredit?true:false).setMsg(updateCredit?"领取成功":"领取失败").setData(mUserCredit.getVipCredit().multiply(new BigDecimal(100)));
    }

    @PostMapping("/list")
    public RJson list(@RequestParam int page,@RequestParam int limit){
        Long userId = LoginInfoHelper.getUserID();
        if(userId==null){
            return RJson.error("该用户未登录");
        }
        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_EQ + "uid", userId);

        DataTable<UserCreditRecord> dataTable = new DataTable();
        dataTable.setFields(new String[]{"id","vip_hongbao","vip_credit","vip_status","create_time"});
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        dataTable.sortDesc("create_time");

        userCreditRecordService.pageSearch(dataTable);

        return RJson.ok().setData(dataTable);
    }


}
