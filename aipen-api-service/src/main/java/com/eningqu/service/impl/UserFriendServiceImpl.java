package com.eningqu.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.domain.api.UserFriend;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.mapper.UserFriendMapper;
import com.eningqu.service.IUserFriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;


@Service
public class UserFriendServiceImpl extends BaseServiceImpl<UserFriendMapper, UserFriend> implements IUserFriendService {

    private Logger logger  = LoggerFactory.getLogger(getClass());

    /**
     * TODO 根据手机号码查询用户信息
     *
     * @param mobile
     * @return
     */
    @Override
    public List<UserFriend> selectFriendByMobile(String mobile) {
        return baseMapper.selectFriendByMobile(mobile);
    }

    /**
     * TODO 通过手机号码查出用户ID
     * @param mobile
     * @return
     */
    @Override
    public Long selectUserIDByMobile(String mobile) { return baseMapper.selectUserIDByMobile(mobile);}

    @Override
    public List<UserFriend> selectFriendByUserId(Long user_id) {
        return baseMapper.selectFriendByUserId(user_id);
    }

    @Override
    public List<UserInfo> selectByCondition(String s_condition) {
        return baseMapper.selectByCondition(s_condition);
    }

    @Override
    public List<UserInfo> selectByIdCondition(Long user_id, String s_condition) {
        return baseMapper.selectByIdCondition(user_id,s_condition);
    }

    @Override
    public void addFriend(Long user_id, Long user_friend_id, String friend_mobile, String friend_nickname,String isai) {
            //System.out.println("=="+user_id+"="+user_friend_id+"=="+friend_mobile+"=="+friend_nickname);
            baseMapper.addFriend(user_id, user_friend_id, friend_mobile, friend_nickname,isai);
            //baseMapper.addFriend(user_friend_id, user_id, "", friend_nickname);

    }

    @Override
    public void delFriend(Long user_id, Long user_friend_id) {
        System.out.println("=="+user_id+"="+user_friend_id);
        baseMapper.delFriend(user_id, user_friend_id);
    }

    @Override
    public UserInfo getUserInfoById(Long user_id){
        return baseMapper.getUserInfoById(user_id);
    }

    @Override
    public List<UserFriend> selectFriendExists (Long user_id,  Long user_friend_id){
        return baseMapper.selectFriendExists(user_id,user_friend_id);
    }

    /**
     * TODO 拉黑某个用户
     * @param user_id,friend_user_id
     * @return
     */
    @Override
    public void setFriendBlack( Long user_id, Long user_friend_id){
        baseMapper.setFriendBlack(user_id,user_friend_id);
    }

    /**
     * TODO 拉白某个用户
     * @param user_id,friend_user_id
     * @return
     */
    @Override
    public void setFriendWhite( Long user_id, Long user_friend_id){
        baseMapper.setFriendWhite(user_id,user_friend_id);
    }


    /**
     * TODO 得到好友黑名单列表
     * @param user_id
     * @return
     */
    @Override
    public List<UserFriend> selectBlackList( Long user_id){
       return baseMapper.selectBlackList(user_id);
    }

    /**
     * TODO 得到好友白名单列表
     * @param user_id
     * @return
     */
    @Override
    public List<UserFriend> selectWhiteList(Long user_id){
       return  baseMapper.selectWhiteList(user_id);
    }

    /**
     * TODO 设置好友分类
     * @param user_id
     * @return
     */
    @Override
    public void setFriendFType( Long user_id,Long user_friend_id,String ftype){
        baseMapper.setFriendFType(user_id,user_friend_id,ftype);
    }

    /**
     * TODO 设置好友分类
     * @param user_id
     * @return
     */
    @Override
    public void setFriendAI( Long user_id,Long user_friend_id,String isai){
        baseMapper.setFriendAI(user_id,user_friend_id,isai);
    }

    /**
     * TODO 得到某个分类的所有好友列表
     * @param user_id,ftype
     * @return
     */
    @Override
    public List<UserFriend> selectFriendListByFtype( Long user_id, String ftype){
        return  baseMapper.selectFriendListByFtype(user_id,ftype);
    }
    /**
     * TODO 得到某个分类的所有好友的白名单列表
     * @param user_id,ftype
     * @return
     */
    @Override
    public List<UserFriend> selectWhiteListByFtype( Long user_id, String ftype){
       return baseMapper.selectWhiteListByFtype(user_id,ftype);
    }

    /**
     * TODO 得到某个分类的所有好友的白名单列表
     * @param user_id,ftype
     * @return
     */
    @Override
    public List<UserFriend> selectWhiteListByNFtype( Long user_id, String ftype){
        return baseMapper.selectWhiteListByNFtype(user_id,ftype);
    }


    /**
     * TODO 设置好友分类
     * @param user_id
     * @return
     */
    @Override
   public void setFriendRemarks( Long user_id,Long user_friend_id,String remarks){
         baseMapper.setFriendRemarks(user_id,user_friend_id,remarks);
    }

    /**
     * TODO 用户ID查询详细信息
     * @param user_id
     * @return
     */
    @Override
    public List<UserInfo> selectUserByUserId(Long user_id) {
        return baseMapper.selectUserByUserId(user_id);
    }

    /**
     * TODO 用户ID查询详细信息
     * @param mobile
     * @return
     */
    @Override
    public List<UserInfo> selectUserByMobile(String mobile) {
        return baseMapper.selectUserByMobile(mobile);
    }


    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    @Override
    public String getAIid( Long user_id){
        return baseMapper.getAIid(user_id);
    }

    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    @Override
    public String getAIidByTeam( Long user_id){
        return baseMapper.getAIidByTeam(user_id);
    }

    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    @Override
    public String getAIidBySys( Long user_id){
        return baseMapper.getAIidBySys(user_id);
    }


    /**
     * TODO 查询红包余额
     * @param user_id
     * @return
     */
    @Override
    public String getGiftMoneyByID( Long user_id){
        return baseMapper.getGiftMoneyByID(user_id);
    }

    /**
     * TODO 更新红包余额
     * @param user_id,friend_user_id,ftype
     * @return
     */
    @Override
    public void updateGiftMoneyByID( Long user_id, String amount){
        baseMapper.updateGiftMoneyByID(user_id,amount);
    }

    /**
     * TODO 添加红包余额明细
     * @param
     * @return
     */
    @Override
    public void insertGiftMoneyDetail(UserCreditRecord userCreditRecord){
        //Long user_id, Long user_friend_id, String vip_credit, String vip_hongbao, String  vip_status, String remarks
        baseMapper.insertGiftMoneyDetail(userCreditRecord);
       //user_id,user_friend_id,vip_credit,vip_hongbao,vip_status,remarks
    }

    /**
     * TODO 通过ID得到已发红包
     * @param
     * @return
     */
    @Override
    public UserCreditRecord getSendedGiftMoneyByID( Long id){
        return baseMapper.getSendedGiftMoneyByID(id);
    }

    /**
     * TODO 通过好友和用户得到所有已发红包或者已收红包
     * @param uid,fid,,vipStatus
     * @return
     */

    public List<UserCreditRecord> getufGiftMoneyList( Long uid,Long fid, String vipStatus){
        return baseMapper.getufGiftMoneyList(uid,fid,vipStatus) ;
    }

    /**
     * TODO 更新红包余额明细
     *
     * @return
     */
    public  void updateSendedGiftMoneyByID(UserCreditRecord userCreditRecord){  //Long id,String vipStatus
        baseMapper.updateSendedGiftMoneyByID(userCreditRecord);
    }

    /**
     * TODO 抢红包
     * @param hid,user_id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = Exception.class)
    @Override
    public int fightGiftMoney(Long hid,Long user_id){

        int rInt=0;
        double hAmount= 0.00 ;//红包金额-- 我抢到的余额
        double mAmount= 0.00; //我的余额
        double gAmount =0.00; //群红包的余额--抢完后剩下的额度
        long gCount = 0;// 群红包的的剩余个数
        long hbCount= 0; // 红包的总个数
        try {
            mAmount = Double.valueOf(baseMapper.getGiftMoneyByID(user_id)).doubleValue();  // 获取我的余额
            UserCreditRecord senducr=baseMapper.getSendedGiftMoneyByID(hid);
            List<UserCreditRecord> meucr=baseMapper.getGroupGiftMoneyList(senducr.getId(),user_id);
            if(meucr!=null && meucr.size()>0 ){
                return 5; //你已经抢过了，不能再抢
            }
            gAmount = senducr.getNamount().doubleValue();
            gCount = senducr.getNcount();
            //  抢红包逻辑
            if(gCount<=0){
                return 1;  // 红包被抢完了
            }
            if(gCount==1)
            {
                hAmount=gAmount;
                senducr.setVipStatus(3); // 更新状态，这是最后一个被抢的红包了

            }else{
                  double tAmount = gAmount - gCount*0.01;
                  int tempInt =new Double(tAmount *100).intValue();
                  Random nR =new Random();  //随机红包·
                  int aTemp = nR.nextInt(tempInt);
                  if (aTemp<1) aTemp=1;
                  hAmount = new Double( aTemp)/100;
                  hAmount = new   BigDecimal(hAmount).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                  // 抢红包成功
            }

            gCount =gCount-1;         // 又被抢了一个
            mAmount=mAmount+hAmount;  // 我的余额

            gAmount=gAmount-hAmount;  // 群红包剩余额度

            senducr.setNamount(BigDecimal.valueOf(gAmount));
            senducr.setNcount(gCount);
            baseMapper.updateSendedGiftMoneyByID(senducr);  // 更新群红包明细

            UserCreditRecord ucr =new UserCreditRecord();
            ucr.setUid(user_id);
            ucr.setFid(senducr.getUid());
            ucr.setRemarks("");
            ucr.setVipHongbao(-1);
            ucr.setVipStatus(1);
            ucr.setVipCredit(BigDecimal.valueOf(hAmount));
            ucr.setPvid(senducr.getId());  // 谁给我的红包
            ucr.setSource("GroupHong");    // 群红包类型
            baseMapper.insertGiftMoneyDetail(ucr);  //加一条我的收红包明细
            baseMapper.updateGiftMoneyByID(user_id,String.valueOf(mAmount));  //更新余额

        }catch (Exception e){
            e.printStackTrace();
            return 2;
        }
        return rInt;
    }

    public List<UserCreditRecord> getGroupGiftMoneyList( Long pvid,Long user_id){
        return baseMapper.getGroupGiftMoneyList(pvid,user_id);
    }

}
