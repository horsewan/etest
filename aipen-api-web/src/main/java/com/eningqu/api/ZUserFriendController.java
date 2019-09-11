package com.eningqu.api;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.im.UserVo;
import com.eningqu.vo.LoginInfoHelper;
import com.eningqu.common.im.MobileVO;
import com.eningqu.common.im.MsgUtil;
import com.eningqu.common.im.QiniuCloudUtil;
import com.eningqu.common.kit.WebKit;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.domain.api.UserFriend;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.service.IUserFriendService;
import com.eningqu.service.IUserInfoService;
import com.eningqu.vo.LoginInfo;
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
import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@RestController
@RequestMapping("/api/friend")
public class ZUserFriendController {

    private static Logger logger = LoggerFactory.getLogger(ZUserFriendController.class);

    @Autowired
    private IUserFriendService userFriendService;
    @Autowired
    private IUserInfoService userInfoService;

    @ApiOperation("得到好友列表-通过用户手机号")
    @ApiImplicitParam(name="mobile",value="手机号",required = true,dataType="String")
    @PostMapping("/friendbymobile")
    public RJson selectFriendByMobile(@RequestParam  String mobile) {
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        if (mobile.isEmpty()) {
            logger.error("手机号码为空");
            return RJson.error().setCode(1);
        }

        List<UserFriend> friendList =  userFriendService.selectFriendByMobile(mobile);
        return  RJson.ok().setCode(1).setData(friendList);

    }

    @ApiOperation("得到好友列表-通过用户ID")
    @PostMapping("/friendbyid")
    public RJson selectFriendByUserId() {
        Long uId = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectFriendByUserId(uId);
        return  RJson.ok().setCode(1).setData(friendList);

    }


    @ApiOperation("得到还不是好友的-通过用户ID")
    @ApiImplicitParam(name="user_id",value="用户ID",required = true,dataType="Long")
    @PostMapping("/userbyid")
    public RJson selectUserByUserId(@RequestParam Long user_id) {
        //Long user_id = LoginInfoHelper.getUserID();
        List<UserInfo> friendList = userFriendService.selectUserByUserId(user_id);
        return  RJson.ok().setCode(1).setData(friendList);

    }


    @ApiOperation("输入搜索条件搜索所有用户")
    @ApiImplicitParam(name="s_condition",value="搜索条件",required = true,dataType="String")
    @PostMapping("/search")
    public RJson selectUserByCondition(@RequestParam String s_condition) {

        List<UserInfo> userList = userFriendService.selectByCondition(s_condition);
        return  RJson.ok().setCode(1).setData(userList);

    }

    @ApiOperation("输入搜索条件搜索除了自己和好友之外的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="s_condition",value="搜索条件",required = true,dataType="String")
    })

    @PostMapping("/searchf")
    public RJson selectUserByIdCondition(@RequestParam String s_condition) {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserInfo> userList = userFriendService.selectByIdCondition(user_id,s_condition);
        return  RJson.ok().setCode(1).setData(userList);

    }

   /*
    @ApiOperation("加好友(旧的接口，即将废弃)")
    @ApiImplicitParams({
    @ApiImplicitParam(name="user_id",value="from 用户id",required = true,dataType="Long"),
    @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    @ApiImplicitParam(name="friend_mobile",value="好友手机",required = true,dataType="String"),
    @ApiImplicitParam(name="friend_nickname",value="好友昵称",required = false,dataType="String")
    })
    @PostMapping("/addfriend")
    public RJson addFriend(@RequestParam Long user_id, @RequestParam  Long user_friend_id, @RequestParam  String friend_mobile, @RequestParam String friend_nickname) {
        try{
            System.out.println("=="+user_id+"="+user_friend_id+"=="+friend_mobile+"=="+friend_nickname);

            List<UserFriend> userFriends= userFriendService.selectFriendExists(user_id,user_friend_id);

            if(userFriends!=null && userFriends.size()>0){
                  return RJson.error().setCode(3).setMsg("用户已经是你的好友");
            }else {
                UserInfo me = userFriendService.getUserInfoById(user_id);
                UserInfo friend = userFriendService.getUserInfoById(user_friend_id);
                userFriendService.addFriend(user_id, user_friend_id, friend.getMobile(), friend.getNickName(),"N");
                userFriendService.addFriend(user_friend_id, user_id, me.getMobile(), me.getNickName(),"N");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);

    }
   */

    @ApiOperation("加好友（新）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/addfriendnew")
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
        return  RJson.ok().setCode(1);

    }

    @ApiOperation("加好友（AI）----- 把AI加为好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/addfriendai")
    public RJson addFriendAI(@RequestParam  Long user_friend_id) {
        Long user_id = LoginInfoHelper.getUserID();
        try{
            System.out.println("=="+user_id+"="+user_friend_id+"==");
            List<UserFriend> userFriends= userFriendService.selectFriendExists(user_id,user_friend_id);
            if(userFriends!=null && userFriends.size()>0){
                return RJson.error().setCode(3).setMsg("用户已经是你的好友");
            }else {
                UserInfo me = userFriendService.getUserInfoById(user_id);
                UserInfo friend = userFriendService.getUserInfoById(user_friend_id);
                userFriendService.addFriend(user_id, user_friend_id, friend.getMobile(), friend.getNickName(),"Y");
                userFriendService.addFriend(user_friend_id, user_id, me.getMobile(), me.getNickName(),"Y");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }



    @ApiOperation("删除好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/delfriend")
    public RJson delFriend(@RequestParam  Long user_friend_id) {
        Long user_id = LoginInfoHelper.getUserID();
        try{
            List<UserFriend> friendList = userFriendService.selectFriendExists(user_id,user_friend_id);
            if(friendList==null || friendList.size()<=0){
                return  RJson.error().setCode(2).setMsg("不是好友不能删除");
            }else{
                if(friendList.get(0).getIsai().equals("N")) {
                    userFriendService.delFriend(user_id,user_friend_id);
                    userFriendService.delFriend(user_friend_id,user_id);
                }else  return  RJson.error().setCode(2).setMsg("是AI助手不会删除");
            }

        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }

    @ApiOperation("删除单边好友--对方仍然看到自己是好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/delfriendsingle")
    public RJson delFriendSingle(@RequestParam  Long user_friend_id) {
        Long user_id = LoginInfoHelper.getUserID();
        try{
            List<UserFriend> friendList = userFriendService.selectFriendExists(user_id,user_friend_id);
            //System.out.println(friendList);
            //System.out.println(friendList.size());
            //System.out.println(friendList.get(0));
            //System.out.println(friendList.get(0).getIsai());
            if(friendList==null || friendList.size()<=0){
                return  RJson.error().setCode(2).setMsg("不是好友不能删除");
            }else{
                if(friendList.get(0).getIsai().equals("N")) {
                    userFriendService.delFriend(user_id,user_friend_id);
                    //userFriendService.delFriend(user_friend_id,user_id);
                }else return  RJson.error().setCode(2).setMsg("是AI关系不能删除");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }



    @ApiOperation("得到上传token")
    @PostMapping("/gettoken")
    public RJson getUploadTocken() {
        return RJson.ok().setData(QiniuCloudUtil.getUpToken());
    }

    @ApiOperation("发送即时消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="发消息的对象",required = true,dataType="Long"),
            @ApiImplicitParam(name="msg",value="消息内容",required = true,dataType="String"),
    })
    @PostMapping("/im")
    public RJson sendMsg(@RequestParam String user_friend_id, @RequestParam  String msg) {
        MsgUtil.sendMsg(user_friend_id,msg);
        return RJson.ok().setData("已发送 ["+msg+"] to ->"+ user_friend_id);
    }


    @ApiOperation("发送不同类型的通知消息给制定的客户   typeu=101 红包消息  typu=102 系统消息" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="发消息的对象",required = true,dataType="Long"),
            @ApiImplicitParam(name="msg",value="消息内容",required = true,dataType="String"),
            @ApiImplicitParam(name="typeu",value="消息类型",required = true,dataType="int"),
    })
    @PostMapping("/imtype")
    public RJson sendMsg(@RequestParam String user_friend_id, @RequestParam  String msg, @RequestParam  int typeu) {
        MsgUtil.sendMsg(user_friend_id,msg,typeu);
        return RJson.ok().setData("已发送 ["+msg+"] to ->"+ user_friend_id);
    }


    @ApiOperation("发送不同类型的通知消息 typeu=101 红包消息  typu=102 系统消息" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="msg",value="消息内容",required = true,dataType="String"),
            @ApiImplicitParam(name="typeu",value="消息类型",required = true,dataType="int"),
    })
    @PostMapping("/imsysmsg")
    public RJson sendGroupMsg( @RequestParam  String msg, @RequestParam  int typeu) {

        List<UserInfo> userList = userFriendService.selectByCondition("1");
        for (UserInfo userinfo:userList) {
            MsgUtil.sendMsg(userinfo.getId().toString(),msg,typeu);
        }
        //MsgUtil.sendMsg(user_friend_id,msg,typeu);
        return RJson.ok().setData("已发送 ["+msg+"] to -> All User");
    }



    @ApiOperation("拉黑某个好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/setfriendblack")

    public RJson setFriendBlack(@RequestParam  Long user_friend_id){
        Long user_id = LoginInfoHelper.getUserID();
        try{
            List<UserFriend> friendList = userFriendService.selectFriendExists(user_id,user_friend_id);
            if(friendList==null || friendList.size()<=0){
                return  RJson.error().setCode(2);
            }else{
                if(friendList.get(0).getIsai().equals("N"))  userFriendService.setFriendBlack(user_id,user_friend_id);
                else  return  RJson.error().setCode(2);
            }

        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }

    @ApiOperation("拉白某个好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/setfriendwhite")

    public RJson setFriendWhite(@RequestParam  Long user_friend_id){
        Long user_id = LoginInfoHelper.getUserID();
        try{
            List<UserFriend> friendList = userFriendService.selectFriendExists(user_id,user_friend_id);
            if(friendList==null || friendList.size()<=0){
                return  RJson.error().setCode(2);
            }else{
                if(friendList.get(0).getIsai().equals("N"))  userFriendService.setFriendWhite(user_id,user_friend_id);
                else  return  RJson.error().setCode(2);
            }

        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }

    @ApiOperation("得到好友黑名单列表")
    @PostMapping("/selectblacklist")
    public RJson selectBlackList() {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectBlackList(user_id);
        return  RJson.ok().setCode(1).setData(friendList);

    }

    @ApiOperation("得到好友白名单列表")
    @PostMapping("/selectwhitelist")
    public RJson selectWhiteList() {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectWhiteList(user_id);
        return  RJson.ok().setCode(1).setData(friendList);

    }


    @ApiOperation("设置好友分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
            @ApiImplicitParam(name="ftype",value="分类类别  S=生活类 J=工作类",required = true,dataType="String"),
    })
    @PostMapping("/setfriendtype")
    public RJson setFriendFType(@RequestParam  Long user_friend_id,@RequestParam  String ftype){
        Long user_id = LoginInfoHelper.getUserID();
        try{
            userFriendService.setFriendFType(user_id,user_friend_id,ftype);
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }
    @ApiOperation("批量设置好友分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friends",value="好友用户ID列表 ID用逗号,分开 ",required = true,dataType="String"),
            @ApiImplicitParam(name="ftype",value="分类类别  S=生活类 J=工作类",required = true,dataType="String"),
    })
    @PostMapping("/setfriendtypes")
    public RJson setFriendFTypes(@RequestParam  String user_friends,@RequestParam  String ftype){
        Long user_id = LoginInfoHelper.getUserID();
        try{
            String[] strFriends = user_friends.split(",");
            for (int i = 0; i < strFriends.length; ++i){
                Long user_friend_id= Long.valueOf(strFriends[i]);
                userFriendService.setFriendFType(user_id,user_friend_id,ftype);
            }

        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }

    @ApiOperation("得到某个分类的【工作类,生活类】所有好友列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ftype",value="分类类别  S=生活类 J=工作类",required = true,dataType="String"),
    })
    @PostMapping("/selectfriendlistbytype")
    public RJson selectFriendListByFtype(@RequestParam  String ftype) {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectFriendListByFtype(user_id,ftype);
        return  RJson.ok().setCode(1).setData(friendList);

    }


    @ApiOperation("得到某个分类的【工作类,生活类】好友白名单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ftype",value="分类类别  S=生活类 J=工作类",required = true,dataType="String"),
    })
    @PostMapping("/selectwhitelistbytype")
    public RJson selectWhiteListByFtype(@RequestParam  String ftype) {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectWhiteListByFtype(user_id,ftype);
        return  RJson.ok().setCode(1).setData(friendList);

    }

    @ApiOperation("得到非某个分类的【工作类,生活类】好友白名单列表, 比如传入工作类，那么显示生活类和未分类的")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ftype",value="分类类别  S=生活类 J=工作类",required = true,dataType="String"),
    })
    @PostMapping("/selectwhitelistbynftype")
    public RJson selectWhiteListBynftype(@RequestParam  String ftype) {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectWhiteListByNFtype(user_id,ftype);
        return  RJson.ok().setCode(1).setData(friendList);

    }


    @ApiOperation("查询是否是好友，如果是好友，显示好友的关系以及所有好友信息，如果不是，返回空列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/getfriendinfo")

    public RJson getFriendInfo(@RequestParam  Long user_id,@RequestParam  Long user_friend_id){
       // Long user_id = LoginInfoHelper.getUserID();
        List<UserFriend> friendList = userFriendService.selectFriendExists(user_id,user_friend_id);
        return  RJson.ok().setCode(1).setData(friendList);
    }

    @ApiOperation("设置好友的标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
            @ApiImplicitParam(name="remarks",value="好友标签",required = true,dataType="String"),
    })
    @PostMapping("/setfriendremarks")

    public RJson setFriendRemarks(@RequestParam  Long user_friend_id, @RequestParam  String remarks){
        Long user_id = LoginInfoHelper.getUserID();
        try{
            userFriendService.setFriendRemarks(user_id,user_friend_id,remarks);
         }catch (Exception e){
           e.printStackTrace();
           return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1);
    }


    @ApiOperation("得到AI人工助手ID")
    @ApiImplicitParams({
    })
    @PostMapping("/getai")

    public RJson getAI() {
        Long user_id = LoginInfoHelper.getUserID();
        List<UserInfo> userinfos = null;
        try{
            String mobile = userFriendService.getAIid(user_id); // 找到个代的电话号码
            if(mobile == null || mobile.length() < 10 )  mobile = userFriendService.getAIidByTeam(user_id);      // 没找到个代号码找团代号码
            if(mobile == null || mobile.length() < 10 )  mobile = userFriendService.getAIidBySys(user_id);      // 没找到个代号码找市代号码

            if (mobile == null || mobile.length() < 10) {
                RJson.error().setCode(2).setMsg("个代手机号码有误");
            } else {
                userinfos= userFriendService.selectUserByMobile(mobile);
                if (userinfos == null || userinfos.size() < 1) {
                    RJson.error().setCode(2).setMsg("个代手机号码有误");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            RJson.error().setCode(3).setMsg("Null point 没有个代数据");
        }
        if(userinfos== null || userinfos.size()<1){
            return RJson.error().setCode(3).setMsg("没有个代数据");
        }else{
            // 第一次的时候把AI助手加为好友
            Long ai_user_id =userinfos.get(0).getId();
            //String ai_user_id=userinfos.get(0).getId().toString();
            List<UserFriend> friendList = userFriendService.selectFriendExists(user_id,ai_user_id);

            if(friendList==null || friendList.size()<=0){

                UserInfo me = userFriendService.getUserInfoById(user_id);
                UserInfo friend = userFriendService.getUserInfoById(ai_user_id);
                userFriendService.addFriend(user_id, ai_user_id, friend.getMobile(), friend.getNickName(),"Y");
                List<UserFriend> tfriendList= userFriendService.selectFriendExists(ai_user_id,user_id);
                if(tfriendList!=null && tfriendList.size()>0){
                    userFriendService.setFriendAI(ai_user_id,user_id,"Y");
                }else {
                    userFriendService.addFriend(ai_user_id,ai_user_id , me.getMobile(), me.getNickName(),"Y");
                }
            }else{
                userFriendService.setFriendAI(user_id,ai_user_id,"Y");
            }
            UserFriend returnfriend = userFriendService.selectFriendExists(user_id,ai_user_id).get(0);
            return RJson.ok().setCode(1).setMsg(String.valueOf(ai_user_id)).setData(returnfriend);
        }

    }

    @ApiOperation("个人发红包给好友,返回红包对象，其中有id用，好友收红包用这个id")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
            @ApiImplicitParam(name="amount",value="金额数目,用字符串为参数(比如5.21),后台自己格式,如果格式不对将返回错误",required = true,dataType="String"),
    })
    @PostMapping("/setgiftmoney")

    public RJson setGiftMoney(@RequestParam  Long user_friend_id, @RequestParam  String  amount,@RequestParam  String  remarks){
        Long user_id = LoginInfoHelper.getUserID();
        double hAmount= 0.00 ;//红包金额
        double mAmount= 0.00; //我的余额
        if(user_id==user_friend_id)  return  RJson.error().setCode(3).setMsg("不能给自己转账或者发红包");
        try{
            String sAmount = userFriendService.getGiftMoneyByID(user_id);
            hAmount=Double.parseDouble(amount.trim());
            mAmount=Double.parseDouble(sAmount.trim());
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
                    ucr.setFid(user_friend_id);
                    ucr.setRemarks(remarks);
                    ucr.setVipHongbao(-1);
                    ucr.setVipStatus(4);
                    ucr.setVipCredit(BigDecimal.valueOf(hAmount));
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


    @ApiOperation("个人收好友红包,用红包ID收取好友红包")
    @ApiImplicitParams({
            //@ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
            @ApiImplicitParam(name="hid",value="红包id",required = true,dataType="Long"),
    })
    @PostMapping("/getgiftmoney")
    public RJson getGiftMoney(@RequestParam  Long  hid){
        Long user_id = LoginInfoHelper.getUserID();
        double hAmount= 0.00 ;//红包金额
        double mAmount= 0.00; //我的余额
        UserCreditRecord senducr = new UserCreditRecord();
        try{
            String sAmount = userFriendService.getGiftMoneyByID(user_id);
            senducr=userFriendService.getSendedGiftMoneyByID(hid);
            if(senducr.getVipStatus()==2){
                return  RJson.error().setCode(4).setMsg("你的红包已经被领过了");
            }
            senducr.getVipCredit().doubleValue();
            hAmount=senducr.getVipCredit().doubleValue() ; //Double.parseDouble(amount.trim());
            mAmount=Double.parseDouble(sAmount.trim());
            mAmount=hAmount+mAmount;
        }catch (Exception e){
            e.printStackTrace();
        }
        try{

            UserCreditRecord ucr =new UserCreditRecord();
            ucr.setUid(user_id);
            ucr.setFid(senducr.getUid());  // 好友id
            ucr.setRemarks("");
            ucr.setVipHongbao(-1);
            ucr.setVipStatus(1);
            ucr.setVipCredit(BigDecimal.valueOf(hAmount));
            userFriendService.insertGiftMoneyDetail(ucr);  //加一条明细收红包
            senducr.setVipStatus(3);
            userFriendService.updateSendedGiftMoneyByID(senducr);  // 更新红包明细
            //userFriendService.insertGiftMoneyDetail(user_friend_id, user_id, amount.trim(),"-1", "2", "");  //更新已发红包
            userFriendService.updateGiftMoneyByID(user_id,String.valueOf(mAmount));  //更新余额

        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2).setMsg("更新失败,服务器问题");

        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        return  RJson.ok().setCode(1).setMsg("红包成功收到").setData(nf.format(mAmount));
    }


    @ApiOperation("好友红包转账")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
            @ApiImplicitParam(name="amount",value="金额数目,用字符串为参数(比如5.21),后台自己格式,如果格式不对将返回错误",required = true,dataType="String"),
    })
    @PostMapping("/sendgiftmoney")
    public RJson sendGiftMoney(@RequestParam  Long user_friend_id, @RequestParam  String  amount){
        Long user_id = LoginInfoHelper.getUserID();
        if(user_id==user_friend_id)  return  RJson.error().setCode(3).setMsg("不能给自己转账或者发红包");
        double hAmount= 0.00 ;//红包金额
        double mAmount= 0.00; //我的余额
        try{
            String sAmount = userFriendService.getGiftMoneyByID(user_id);
            hAmount=Double.parseDouble(amount.trim());
            mAmount=Double.parseDouble(sAmount.trim());
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
                ucr.setFid(user_friend_id);
                ucr.setRemarks("转账");
                ucr.setVipHongbao(-1);
                ucr.setVipStatus(3);
                ucr.setVipCredit(BigDecimal.valueOf(hAmount));
                userFriendService.insertGiftMoneyDetail(ucr);  //加一我的红包明细条明细  //user_id, user_friend_id, amount.trim(),"-1", "3", remarks
                userFriendService.updateGiftMoneyByID(user_id,String.valueOf(mAmount));  //更新我的余额

                UserCreditRecord fucr =new UserCreditRecord();
                fucr.setUid(user_friend_id);
                fucr.setFid(user_id);
                fucr.setRemarks("");
                fucr.setVipHongbao(-1);
                fucr.setVipStatus(1);
                fucr.setVipCredit(BigDecimal.valueOf(hAmount));
                String fsAmount = userFriendService.getGiftMoneyByID(user_friend_id);
                double fAmount=Double.parseDouble(fsAmount.trim());
                fAmount=fAmount+hAmount;
                userFriendService.insertGiftMoneyDetail(fucr);  //加一好友的条明细收红包
                userFriendService.updateGiftMoneyByID(user_friend_id,String.valueOf(fAmount));  //更新好友的余额
            }catch (Exception e){
                e.printStackTrace();
                return  RJson.error().setCode(2).setMsg("更新失败,服务器问题");
            }
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            ucr.setBalance(nf.format(mAmount));

            return  RJson.ok().setCode(1).setMsg("转账完成").setData(nf.format(mAmount));
        }else{
            return  RJson.error().setCode(2).setMsg("你的余额不够");
        }
        //return  RJson.ok().setCode(1).setMsg("红包成功收到").setData(nf.format(mAmount));
    }




    @ApiOperation("查询好友之间所有发出去但是没有收到的红包")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_friend_id",value="好友用户id",required = true,dataType="Long"),
    })
    @PostMapping("/getsendedmoney")
    public RJson getFriendAllSendedGiftMoney(@RequestParam  Long user_friend_id){
        Long user_id = LoginInfoHelper.getUserID();
        return  RJson.ok().setCode(1).setData(userFriendService.getufGiftMoneyList(user_id,user_friend_id,"3"));
    }

    @ApiOperation("根据ID查询红包情况 vip_status=3表示未领红包,vip_status=2表示已经领过了")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="红包id",required = true,dataType="Long"),
    })
    @PostMapping("/getsendedmoneybyid")
    public RJson getFriendAllSendedGiftMoneyByID(@RequestParam Long id){
        UserCreditRecord senducr=userFriendService.getSendedGiftMoneyByID(id);
        return  RJson.ok().setCode(1).setData(senducr);
    }

    @ApiOperation("批量导入通讯录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_mobiles",value="通讯录好友手机号码列表，用逗号分开 ",required = true,dataType="String"),
    })
    @PostMapping("/getmobilereturn")
    public RJson getMobileReturn(@RequestParam  String user_mobiles){
        Long user_id = LoginInfoHelper.getUserID();
        ArrayList<MobileVO> mobilevoList = new ArrayList<MobileVO>();
        try{
            String[] strMobiles = user_mobiles.split(",");
            List<UserFriend> userFriendList=userFriendService.selectFriendByUserId(user_id);
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
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1).setData(mobilevoList);
    }
    public boolean isFriend(String mobile,List<UserFriend> userFriendList){
        for(int i=0;i<userFriendList.size();i++){
            if(userFriendList.get(i).getFriendMobile().equals(mobile)){
                return true;
            }
        }
        return false;
    }

    

    @ApiOperation("批量获取用户头像,用户自己昵称")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_ids",value="用户ID列表，用逗号分开 ",required = true,dataType="String"),
    })
    @PostMapping("/getUsernh")
    public RJson getUsernh(@RequestParam  String user_ids){

        ArrayList<UserVo> uservoList = new ArrayList<UserVo>();
        try{
            String[] strids = user_ids.split(",");
            for (int i = 0; i < strids.length; i++){
                String user_id= strids[i];
                UserVo userVO = new UserVo();
                userVO.setUserId(user_id);
                //if(isFriend(mobile, strids)) mobileVO.setIsFriend("Y");
                //else mobileVO.setIsFriend("N");
                List<UserInfo> userinfos = userFriendService.selectUserByUserId(Long.valueOf(user_id));
                userVO.setHeadImg(userinfos.get(0).getHeadImg());
                userVO.setNickName(userinfos.get(0).getNickName());
                uservoList.add(i,userVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            return  RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1).setData(uservoList);
    }

}
