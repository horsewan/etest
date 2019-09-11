package com.eningqu.api;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.im.UserCheckInVo;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.domain.api.*;
import com.eningqu.service.DyUserMobilelistService;
import com.eningqu.service.DyUserSettingService;
import com.eningqu.service.IUserFriendService;
import com.eningqu.vo.LoginInfoHelper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

@EnableSwagger2
@RestController
@RequestMapping("/api/giftmoney")
public class ZGiftMoneyController {
    @Autowired
    private DyUserSettingService dyUserSettingService;
    @Autowired
    private RedisKit redisKit;
    @Autowired
    private IUserFriendService userFriendService;
    @Autowired
    private DyUserMobilelistService dyUserMobilelistService;

    private static Logger logger = LoggerFactory.getLogger(ZGiftMoneyController.class);

    @ApiOperation("得到用户邀请码")
    @PostMapping("/getuserinvitecode")
    public RJson getUserInviteCode(@RequestParam Long user_id) {
        String inviteCode="inviteCode";
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,inviteCode);
        String invateValue = get6RandomCode();
        if(dyUserSettings==null || dyUserSettings.size()<1) {

            DyUserSetting dyUserSetting = new DyUserSetting();
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(inviteCode);
            dyUserSetting.setSettingValue(invateValue);
            dyUserSetting.setModule("Global");
            dyUserSetting.setRemarks(".");
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.insertDyConstant(dyUserSetting);
        }
        return RJson.ok("1").setData(invateValue);
    }

    @ApiOperation("更新用户邀请码")
    @PostMapping("/setuserinvitecode")
    public RJson setUserInviteCode(@RequestParam Long user_id,@RequestParam String invatecode) {
        String inviteCode="inviteCode";
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,inviteCode);
        String invateValue = invatecode;
        if(invateValue== null || invateValue.length()<1) invateValue=get6RandomCode();
        if(dyUserSettings==null || dyUserSettings.size()<1) {

            DyUserSetting dyUserSetting = new DyUserSetting();
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(inviteCode);
            dyUserSetting.setSettingValue(invatecode);
            dyUserSetting.setModule("Global");
            dyUserSetting.setRemarks(".");
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.updateByPrimaryKey(dyUserSetting);
        }
        return RJson.ok("1").setData(invateValue);
    }


    public String getInviteCode(Long user_id){
        String inviteCode="inviteCode";
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,inviteCode);
        String invateValue = get6RandomCode();
        if(dyUserSettings==null || dyUserSettings.size()<1) {

            DyUserSetting dyUserSetting = new DyUserSetting();
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(inviteCode);
            dyUserSetting.setSettingValue(invateValue);
            dyUserSetting.setModule("Global");
            dyUserSetting.setRemarks(".");
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.insertDyConstant(dyUserSetting);
        }
        return invateValue;
    }

    public String get6RandomCode(){
        char[] arr = {48,49,50,51,52,53,54,55,56,57, 65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122};
        int i=1;
        String rString="";
        while(i++<=8){ //循环六次，得到六位数的验证码
            char msg =arr[(int)(Math.random()*62)];
            //System.out.print(msg);
            rString += msg;
        }
        return rString.toLowerCase();
    }


    @ApiOperation("邀请码验证")
    @PostMapping("/isinviteCode")
    public RJson isInviteCode(@RequestParam String inviteValue) {
        String inviteCode="inviteCode";
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCodeValue(inviteCode,inviteValue);

        if(dyUserSettings==null || dyUserSettings.size()<1) {
            return RJson.ok("2").setMsg("你的邀请输入错误了");

        }else{
            return RJson.ok("1").setData(dyUserSettings.get(0));
        }
    }


    @ApiOperation("批量存储通讯录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_id",value="传通讯录的用户id",required = true,dataType="Long"),
            @ApiImplicitParam(name="user_mobiles",value="通讯录好友手机号码列表，用逗号分开 ",required = true,dataType="String"),
            @ApiImplicitParam(name="user_names",value="通讯录好友手机号码列表，用逗号分开 ,如果通讯录名称中含有逗号，请先把它转成空格然后传上",required = true,dataType="String"),
    })
    @PostMapping("/putmobilelist")
    public RJson putMobileList(@RequestParam  Long user_id,@RequestParam  String user_mobiles,@RequestParam  String user_names){

        try{
            String[] strMobiles = user_mobiles.split(",");
            String[] strNames = user_names.split(",");

            List<DyUserMobilelist> dyUserMobilelists = dyUserMobilelistService.selectByUserId(user_id);

            if(dyUserMobilelists!=null && dyUserMobilelists.size()>0){
                return  RJson.error().setCode(2).setMsg("你已经上传过了通讯录，不能再次上传");
            }else{
                if(strMobiles.length<1) {
                    RJson.error().setCode(2).setMsg("你的通讯录没有人");
                }
                for (int i = 0; i < strMobiles.length; i++){
                    DyUserMobilelist dyUserMobilelist = new DyUserMobilelist();
                    dyUserMobilelist.setCreateId(0l);
                    dyUserMobilelist.setUpdateId(0L);
                    dyUserMobilelist.setMyMobile(".");
                    dyUserMobilelist.setUserId(user_id);
                    dyUserMobilelist.setListMobile(strMobiles[i]);
                    dyUserMobilelist.setListName(strNames[i]);
                    dyUserMobilelist.setRemarks("...");
                    dyUserMobilelistService.insertDyUserMobilelist(dyUserMobilelist);
                }
            }
            String giftMoney = String.valueOf(strMobiles.length);
            sysSendGiftMoney(user_id,giftMoney,"ListMIn");  // 通讯录导入好友给红包
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2).setMsg("你传的参数有问题");
        }
        return  RJson.ok().setCode(1).setMsg("上传通讯录成功");
    }

   /* public boolean isFriend(String mobile,List<UserFriend> userFriendList){
        for(int i=0;i<userFriendList.size();i++){
            if(userFriendList.get(i).getFriendMobile().equals(mobile)){
                return true;
            }
        }
        return false;
    }

            List<UserFriend> userFriendList=userFriendService.selectFriendByUserId();
            for (int i = 0; i < strMobiles.length; i++){
                String mobile= strMobiles[i];
                MobileVO mobileVO = new MobileVO();
                mobileVO.setMobile(mobile);
                if(isFriend(mobile, userFriendList)) mobileVO.setIsFriend("Y");
                else mobileVO.setIsFriend("N");
                Long userId = userFriendService.selectUserIDByMobile(mobile);
                mobileVO.setUserId(String.valueOf(userId));
                if(userId!=null && userId>0) mobileVO.setIsUser("Y"); else mobileVO.setIsUser("N");

                mobilevoList.add(i,mobileVO);
            }

    */

    @ApiOperation("签到")
    @PostMapping("/checkin")
    public RJson Signin(@RequestParam Long user_id) {

        String checkInCount=getUserSettingValue(user_id,"checkInCount");       // 连续签到次数
        String lastCheckInDate=getUserSettingValue(user_id,"lastCheckInDate"); // 最后签到日期
        String lastGiftCount=getUserSettingValue(user_id,"lastGiftCount"); // 最后签到日期

        java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String nowDate= sf.format(new Date());
        long giftCount=1;
        UserCheckInVo userCheckInVo = new UserCheckInVo();
        userCheckInVo.setUserId(String.valueOf(user_id));
        userCheckInVo.setLastCheckInDate(nowDate);

        if(checkInCount==null || checkInCount.length()<1){  // 还没有设置变量的时候-- 第一次设置变量

            setUserSettingValue(user_id,"checkInCount","1");
            setUserSettingValue(user_id,"lastCheckInDate",nowDate);
            setUserSettingValue(user_id,"lastGiftCount","1");
            userCheckInVo.setCheckInCount("1");
            userCheckInVo.setGiftCount("1");

        }else{
            long dCount =getDateBetween(lastCheckInDate,nowDate);
            if(dCount>1){   // 间隔天数大于一天没有打卡，一切从头开始
                setUserSettingValue(user_id,"checkInCount","1");
                setUserSettingValue(user_id,"lastCheckInDate",nowDate);
                setUserSettingValue(user_id,"lastGiftCount","1");
                userCheckInVo.setCheckInCount("1");
                userCheckInVo.setGiftCount("1");
            }else if(dCount==1) {  // 连续签到
                long checkincount = Long.valueOf(checkInCount).longValue();
                long lastgiftcount = Long.valueOf(lastGiftCount).longValue();
                checkincount = checkincount +1;
                if(lastgiftcount>=7)  lastgiftcount =1; else lastgiftcount=lastgiftcount+1;
                setUserSettingValue(user_id,"checkInCount",String.valueOf(checkincount));
                setUserSettingValue(user_id,"lastCheckInDate",nowDate);
                setUserSettingValue(user_id,"lastGiftCount",String.valueOf(lastgiftcount));
                userCheckInVo.setCheckInCount(String.valueOf(checkincount));
                userCheckInVo.setGiftCount(String.valueOf(lastgiftcount));

            }else{
                return RJson.error("2").setMsg("不能连续签到，一天不能签到两次");
            }
        }
        return RJson.ok("1").setMsg("签到成功").setData(userCheckInVo);
    }

    @ApiOperation("得到上一次的签到信息")
    @PostMapping("/getcheckininfo")
    public RJson getSignin(@RequestParam Long user_id) {
        String checkInCount=getUserSettingValue(user_id,"checkInCount");       // 连续签到次数
        String lastCheckInDate=getUserSettingValue(user_id,"lastCheckInDate"); // 最后签到日期
        String lastGiftCount=getUserSettingValue(user_id,"lastGiftCount"); // 最后签到日期
        UserCheckInVo userCheckInVo = new UserCheckInVo();
        userCheckInVo.setUserId(String.valueOf(user_id));
        if(checkInCount==null || checkInCount.length()<1){
            userCheckInVo.setLastCheckInDate(null);
            userCheckInVo.setCheckInCount("1");
            userCheckInVo.setGiftCount("1");
        }else{
            userCheckInVo.setLastCheckInDate(lastCheckInDate);
            userCheckInVo.setCheckInCount(String.valueOf(checkInCount));
            userCheckInVo.setGiftCount(String.valueOf(lastGiftCount));
        }
        return RJson.ok("1").setMsg("得到信息").setData(userCheckInVo);
    }

public int setUserSettingValue(Long user_id,String code ,String value){
    String  module="Global";
    try{
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,code);
        if(dyUserSettings!=null && dyUserSettings.size()>0){
            DyUserSetting dyUserSetting=dyUserSettings.get(0);
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(code);
            dyUserSetting.setSettingValue(value);
            dyUserSetting.setModule(module);
            dyUserSetting.setRemarks("");
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.updateByPrimaryKey(dyUserSetting);
        }else{
            DyUserSetting dyUserSetting= new DyUserSetting();
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(code);
            dyUserSetting.setSettingValue(value);
            dyUserSetting.setModule(module);
            dyUserSetting.setRemarks("");
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.insertDyConstant(dyUserSetting);
        }
        if(code.equals("lastGiftCount")){
            sysSendGiftMoney(user_id,value,"CheckIn");   // 签到给红包
        }

    }catch (Exception e){
        e.printStackTrace();
        return 1;
    }
    return 0;
  }

public String getUserSettingValue(Long user_id,String code){
        String  module="Global";
        String retValue=null;
        try{
            List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,code);
            if(dyUserSettings!=null && dyUserSettings.size()>0){
                retValue = dyUserSettings.get(0).getSettingValue();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return retValue;
    }

public long getDateBetween(String date1,String date2){

    //String date1 ="2015-05-28";
    //String date2 ="2015-05-30";
    long betweenDate=0;
    try{
        java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date beginDate=sf.parse(date1);
        Date endDate=sf.parse(date2);
        betweenDate = (endDate.getTime()-beginDate.getTime())/(1000*60*60*24);
        System.out.println(betweenDate);
    }catch (Exception e){
        e.printStackTrace();
    }
    betweenDate =1;  // for test
    return betweenDate;

}

public void sysSendGiftMoney(Long user_id,String amount,String sourceType){
    double hAmount= 0.00 ;//红包金额
    double mAmount= 0.00; //我的余额
    String sAmount = userFriendService.getGiftMoneyByID(user_id);
    hAmount=Double.parseDouble(amount.trim());
    mAmount=Double.parseDouble(sAmount.trim());
    mAmount =mAmount +hAmount;
    UserCreditRecord ucr =new UserCreditRecord();
    ucr.setUid(user_id);
    ucr.setFid(0l);
    ucr.setRemarks("");
    ucr.setVipHongbao(-1);
    ucr.setVipStatus(1);
    ucr.setVipCredit(BigDecimal.valueOf(hAmount));
    ucr.setSource(sourceType);
    userFriendService.insertGiftMoneyDetail(ucr);  //加一条明细  //user_id, user_friend_id, amount.trim(),"-1", "3", remarks
    userFriendService.updateGiftMoneyByID(user_id,String.valueOf(mAmount));  //更新余额
  }


    @ApiOperation("通过附近的人加好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/addfriend")
    public RJson addFriend(@RequestParam  Long user_friend_id) {

        try{
            Long user_id = LoginInfoHelper.getUserID();
            if( user_id.longValue() == user_friend_id.longValue() ) {
                return RJson.error().setCode(3).setMsg("不能加自己为好友");
            }

            List<UserFriend> userFriends= userFriendService.selectFriendExists(user_id,user_friend_id);

            if(userFriends!=null && userFriends.size()>0){
                return RJson.error().setCode(3).setMsg("用户已经是你的好友");
            }else {
                if(user_id.longValue() != user_friend_id.longValue()){
                    UserInfo me = userFriendService.getUserInfoById(user_id);
                    UserInfo friend = userFriendService.getUserInfoById(user_friend_id);
                    userFriendService.addFriend(user_id, user_friend_id, friend.getMobile(), friend.getNickName(),"N");
                    List<UserFriend> tfriendList= userFriendService.selectFriendExists(user_friend_id,user_id);
                    if(tfriendList!=null && tfriendList.size()>0){

                    }else {
                        userFriendService.addFriend(user_friend_id, user_id, me.getMobile(), me.getNickName(),"N");
                    }
                }else{
                    return RJson.error().setCode(3).setMsg("不能加自己为好友");
                }

            }


        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        sysSendGiftMoney(user_friend_id,"5","NearBy");  //  附近的人加好友奖励红包
        return  RJson.ok().setCode(1);

    }


    @ApiOperation("群发红包")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_ids",value="群中好友id,很多个的时候中间用逗号隔开",required = true,dataType="String"),
            @ApiImplicitParam(name="amount",value="金额数目,用字符串为参数(比如5.21),后台自己格式,如果格式不对将返回错误",required = true,dataType="String"),
            @ApiImplicitParam(name="remarks",value="红包描述",required = true,dataType="String"),
            @ApiImplicitParam(name="hCount",value="红包个数",required = true,dataType="Long"),
    })
    @PostMapping("/setgroupgiftmoney")
    public RJson setGroupGiftMoney(@RequestParam  String user_friend_ids, @RequestParam  String  amount,@RequestParam  String  remarks,@RequestParam  Long hCount){
        Long user_id = LoginInfoHelper.getUserID();
        double hAmount= 0.00 ;//红包金额
        double mAmount= 0.00; //我的余额
        String [] user_fids =null;
        //if(user_id==user_friend_id)  return  RJson.error().setCode(3).setMsg("不能给自己转账或者发红包");

        try{
            String sAmount = userFriendService.getGiftMoneyByID(user_id);
            hAmount=Double.parseDouble(amount.trim());
            mAmount=Double.parseDouble(sAmount.trim());
            user_fids=user_friend_ids.split(",");
            if(user_fids.length<=1){
                return  RJson.error().setCode(3).setMsg("人数太少,不合适发红包");
            }
            if(hAmount<user_fids.length*0.01){
                return  RJson.error().setCode(3).setMsg("钱太少了,不够分呀");
            }
            if(hCount>user_fids.length){
                return  RJson.error().setCode(3).setMsg("红包数量不能多于群的总人数");
            }
            //mAmount=hAmount+mAmount;
        }catch (Exception e){
            return  RJson.error().setCode(3).setMsg("红包金额不对或者你没有余额或者输入金额格式错误");
        }
        if(mAmount>=hAmount){
            mAmount=mAmount-hAmount;
            //Long returnId;
            UserCreditRecord ucr =null;
            try{
                ucr =new UserCreditRecord();
                ucr.setUid(user_id);
                ucr.setFid(-1L*hCount);//
                ucr.setRemarks(remarks);
                ucr.setVipHongbao(-1);
                ucr.setVipStatus(4);
                ucr.setVipCredit(BigDecimal.valueOf(hAmount));
                ucr.setNamount(BigDecimal.valueOf(hAmount));
                ucr.setHbcount(hCount);
                ucr.setNcount(hCount);
                userFriendService.insertGiftMoneyDetail(ucr);  //加一条明细  //user_id, user_friend_id, amount.trim(),"-1", "3", remarks
                userFriendService.updateGiftMoneyByID(user_id,String.valueOf(mAmount));  //更新余额

            }catch (Exception e){
                e.printStackTrace();
                return  RJson.error().setCode(2).setMsg("更新失败,服务器问题");
            }
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            ucr.setBalance(nf.format(mAmount));
            return  RJson.ok().setCode(1).setMsg("红包发送完成").setData(ucr);
        }else{
            return  RJson.error().setCode(2).setMsg("你的余额不够");
        }
    }

    @ApiOperation("个人抢群里的红包")
    @ApiImplicitParams({
            //@ApiImplicitParam(name="user_friend_ids",value="群中好友id,很多个的时候中间用逗号隔开",required = true,dataType="String"),
            @ApiImplicitParam(name="hid",value="红包id",required = true,dataType="Long"),
    })
    @PostMapping("/getgroupgiftmoney")
    public RJson getGroupGiftMoney( @RequestParam  Long  hid){
        Long user_id = LoginInfoHelper.getUserID();
        int info=userFriendService.fightGiftMoney(hid,user_id);
        if(info==0){
            return  RJson.ok().setCode(1).setMsg("红包成功抢到").setData(userFriendService.getGroupGiftMoneyList(hid,null));
        }else if(info==1){
            return  RJson.error().setCode(2).setMsg("红包被抢完了");
        }else if(info==3){
            return  RJson.error().setCode(3).setMsg("更新数据错误");
        }else if(info==5){
             return  RJson.error().setCode(5).setMsg("你已经抢过了，太贪心了把");
        }
        else{
            return  RJson.error().setCode(4).setMsg("系统错误");
        }

    }

    @ApiOperation("查询群红包被抢信息")
    @ApiImplicitParams({
            //@ApiImplicitParam(name="user_friend_ids",value="群中好友id,很多个的时候中间用逗号隔开",required = true,dataType="String"),
            @ApiImplicitParam(name="hid",value="红包id",required = true,dataType="Long"),
    })
    @PostMapping("/getgroupgiftmoneyinfo")
    public RJson getGroupGiftMoneyInfo( @RequestParam  Long  hid){

        return  RJson.ok().setCode(1).setMsg("获取成功").setData(userFriendService.getGroupGiftMoneyList(hid,null));
    }


}
