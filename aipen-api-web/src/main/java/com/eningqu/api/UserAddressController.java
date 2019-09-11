package com.eningqu.api;

import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.vo.LoginInfoHelper;
import com.eningqu.domain.api.UserAddress;
import com.eningqu.service.IUserAddressService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 *
 * @desc TODO  用户地址
 * @author     Yanghuangping
 * @since      2018/8/13 14:54
 * @version    1.0
 *
 **/
@RestController
@RequestMapping("/api/address")
public class UserAddressController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserAddressService userAddressService;

    /**
     * 新增、更新地址
     * 新增则不需要传地址id=adid
     * 更新则需要传地址id=adid
     *  sta   1=新增   2=修改
     * @param adid
     * @param nickName
     * @param aPhone
     * @param aAddress
     * @param sta
     * @return
     */
    @PostMapping("/updateByAddressId")
    public RJson updateByOrderId(@RequestParam(required = false) Long adid, @RequestParam String nickName, @RequestParam String aPhone,
                                 @RequestParam String aAddress, @RequestParam Long sta, @RequestParam String isDef){

        if(nickName==null&&"".equals(nickName))
            return RJson.error("收件人参数错误");
        if(aPhone==null&&"".equals(aPhone))
            return RJson.error("收件人手机号参数错误");
        if(aAddress==null&&"".equals(aAddress))
            return RJson.error("收件人地址参数错误");
        if(sta!=1&&sta!=2)
            return RJson.error("操作参数错误");
        Long uId = LoginInfoHelper.getUserID();

        //新增
        if(sta==1){
            UserAddress userAddress = new UserAddress();
            userAddress.setaNick(nickName);
            userAddress.setaAddress(aAddress);
            userAddress.setaPhone(aPhone);
            userAddress.setuId(uId);
//            UserAddress tempUserAddress = userAddressService.selectUserAddressByUid(uId);
//            if(isDef==null&&"".equals(isDef)){
//                userAddress.setIsDef("N");
//            }else{
                userAddress.setIsDef(isDef);
//            }
            if("Y".equalsIgnoreCase(isDef)){
                //修改之前的Y为N
                UserAddress tempUserAddress = userAddressService.selectUserAddressByUid(uId);
                if(tempUserAddress!=null){
                    tempUserAddress.setIsDef("N");
                    userAddressService.updateById(tempUserAddress);
                }
            }

            userAddressService.insert(userAddress);
            UserAddress tempUserAddressId = userAddressService.selectIdByUid(uId);
            if(tempUserAddressId!=null) {
                return RJson.ok("新增地址成功").setData(tempUserAddressId.getId());
            }
            return RJson.ok("新增地址成功");
        }else{
            if(adid<0)
                return RJson.error("地址id参数错误");

            UserAddress userAddress = userAddressService.selectUserAddressByAUID(uId,adid);
            if(userAddress==null)
                return RJson.error("该地址不存在");

            userAddress.setaNick(nickName);
            userAddress.setaAddress(aAddress);
            userAddress.setaPhone(aPhone);
            userAddress.setIsDef(isDef);
            if("Y".equalsIgnoreCase(isDef)){
                //修改之前的Y为N
                UserAddress tempUserAddress = userAddressService.selectUserAddressByUid(uId);
                if(tempUserAddress!=null){
                    tempUserAddress.setIsDef("N");
                    userAddressService.updateById(tempUserAddress);
                }
            }
            userAddressService.updateById(userAddress);
            return RJson.ok("更新地址成功");
        }
    }

    @PostMapping("/del")
    public RJson delAddressById(@RequestParam Long adid){
        Long uId = LoginInfoHelper.getUserID();

        UserAddress userAddress = userAddressService.selectUserAddressByAUID(uId,adid);
        if(userAddress==null)
            return RJson.error("该地址不属于当前用户不存在");

       boolean bool = userAddressService.deleteById(adid);
       if(bool){
           return RJson.ok("删除地址成功");
       }else{
           return RJson.error("删除地址失败");
       }
    }

    @PostMapping("/getDef")
    public RJson getDefUserAddress(){
        Long uId = LoginInfoHelper.getUserID();

        UserAddress tempUserAddress = userAddressService.selectUserAddressByUid(uId);
        if(tempUserAddress==null){
            tempUserAddress = userAddressService.selectUserAddressByUidAndTime(uId);
        }
        return RJson.ok().setData(tempUserAddress);
    }


    /*@PostMapping("/updateDef")
    public RJson updateDef(@RequestParam Long adidNew,@RequestParam Long uId){
        if(adidNew<0)
            return RJson.error("地址id参数错误");

        UserAddress tempUserAddress = userAddressService.selectUserAddressByUid(uId);
        if(tempUserAddress!=null){
            tempUserAddress.setIsDef("N");
            userAddressService.updateById(tempUserAddress);
        }

        UserAddress userAddress = userAddressService.selectById(adidNew);
        userAddress.setIsDef("Y");
        boolean bool = userAddressService.updateById(userAddress);
        if(bool){
            return RJson.ok("默认地址设置成功");
        }else{
            return RJson.error("默认地址设置失败");
        }

    }*/

    @PostMapping("/list")
    public RJson list(@RequestParam int page,@RequestParam int limit){
        Long uId = LoginInfoHelper.getUserID();

        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_EQ + "uid", uId);

        DataTable<UserAddress> dataTable = new DataTable();
        dataTable.setFields(new String[]{"id","a_nick","a_phone","a_address","is_def"});
        dataTable.setPage(page);
        dataTable.sortDesc("create_time");
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        userAddressService.pageSearch(dataTable);
        return RJson.ok().setData(dataTable);
    }

}
