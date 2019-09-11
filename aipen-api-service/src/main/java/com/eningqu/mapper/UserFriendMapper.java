package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.domain.api.UserFriend;
import com.eningqu.domain.api.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @desc TODO  用户好友Mapper
 * @author     Wanzhibo
 * @date       2019/7/30 19:38
 * @version    1.0
 */
public interface UserFriendMapper extends CrudDao<UserFriend> {


    /**
     * TODO 根据查询条件查找所有用户
     * @param s_condition
     * @return
     */
    List<UserInfo> selectByCondition(@Param("s_condition") String s_condition);

    /**
     * TODO 根据查询条件查找没有成为自己好友的用户
     * @param s_condition
     * @return
     */
    List<UserInfo> selectByIdCondition(@Param("user_id") Long user_id,@Param("s_condition") String s_condition);

    /**
     * TODO 通过手机号码列出自己的好友
     * @param mobile
     * @return
     */
    List<UserFriend> selectFriendByMobile(@Param("mobile") String mobile);

    /**
     * TODO 通过手机号码列出自己的好友
     * @param mobile
     * @return
     */
    Long selectUserIDByMobile(@Param("mobile") String mobile);


    /**
     * TODO 通过手机号码列出自己的好友
     * @param user_id
     * @return
     */
    List<UserFriend> selectFriendByUserId(@Param("user_id") Long user_id);

    /**
     * TODO 加好友
     * @param user_id,friend_user_id,friend_mobile,friend_nickname
     * @return
     */
    void addFriend(@Param("user_id") Long user_id,@Param("friend_user_id") Long friend_user_id,@Param("friend_mobile") String friend_mobile,@Param("friend_nickname") String friend_nickname,@Param("isai") String isai);

    /**
     * TODO 删除
     * @param user_id,friend_user_id
     * @return
     */

    void delFriend(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id );

    /**
     * TODO 查询用户信息
     * @param user_id
     * @return
     */
    UserInfo getUserInfoById (@Param("user_id") Long user_id);


    /**
     * 选择是否有该好友的存在
     * @param user_id,friend_user_id
     * @return
     */

    List<UserFriend> selectFriendExists (@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id);

    /**
     * TODO 拉黑某个用户
     * @param user_id,friend_user_id
     * @return
     */

    void setFriendBlack(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id);

    /**
     * TODO 拉白某个用户
     * @param user_id,friend_user_id
     * @return
     */

    void setFriendWhite(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id);


    /**
     * TODO 得到好友黑名单列表
     * @param user_id
     * @return
     */

    List<UserFriend> selectBlackList(@Param("user_id") Long user_id);

    /**
     * TODO 得到好友白名单列表
     * @param user_id
     * @return
     */
    List<UserFriend> selectWhiteList(@Param("user_id") Long user_id);

    /**
     * TODO 设置好友分类
     * @param user_id,friend_user_id,ftype
     * @return
     */

    void setFriendFType(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id,@Param("ftype") String ftype);


    /**
     * TODO 设置好友分类
     * @param user_id,friend_user_id,isai
     * @return
     */

    void setFriendAI(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id,@Param("isai") String isai);



    /**
     * TODO 得到某个分类的所有好友列表
     * @param user_id,ftype
     * @return
     */
    List<UserFriend> selectFriendListByFtype(@Param("user_id") Long user_id,@Param("ftype") String ftype);

    /**
     * TODO 得到某个分类的所有好友的白名单列表
     * @param user_id,ftype
     * @return
     */
    List<UserFriend> selectWhiteListByFtype(@Param("user_id") Long user_id,@Param("ftype") String ftype);

    /**
     * TODO 得到某个分类的所有好友的白名单列表
     * @param user_id,ftype
     * @return
     */
    List<UserFriend> selectWhiteListByNFtype(@Param("user_id") Long user_id,@Param("ftype") String ftype);

    /**
     * TODO 设置好友标签
     * @param user_id,friend_user_id,ftype
     * @return
     */

    void setFriendRemarks(@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id,@Param("remarks") String remarks);


    /**
     * TODO 用户ID查询详细信息
     * @param user_id
     * @return
     */
    List<UserInfo> selectUserByUserId(@Param("user_id") Long user_id);

    /**
     * TODO 用户ID查询详细信息
     * @param mobile
     * @return
     */
    List<UserInfo> selectUserByMobile(@Param("mobile") String mobile);


    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    String getAIid(@Param("user_id") Long user_id);

    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    String getAIidByTeam(@Param("user_id") Long user_id);

    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    String getAIidBySys(@Param("user_id") Long user_id);


    /**
     * TODO 查询红包余额
     * @param user_id
     * @return
     */

    String getGiftMoneyByID(@Param("user_id") Long user_id);

    /**
     * TODO 更新红包余额
     * @param user_id,amount
     * @return
     */

    void updateGiftMoneyByID(@Param("user_id") Long user_id,@Param("amount") String amount);

    /**
     * TODO 添加红包余额明细
     * @param
     * @return
     */

    void insertGiftMoneyDetail( UserCreditRecord userCreditRecord);
    //@Param("user_id") Long user_id,@Param("user_friend_id") Long user_friend_id,@Param("vip_credit") String vip_credit,@Param("vip_hongbao") String vip_hongbao,@Param("vip_status") String  vip_status,@Param("remarks") String remarks

    /**
     * TODO 通过ID得到已发红包
     * @param
     * @return
     */

    UserCreditRecord getSendedGiftMoneyByID( Long id);

    /**
     * TODO 通过好友和用户得到所有已发红包或者已收红包
     * @param uid,fid,,vipStatus
     * @return
     */

    List<UserCreditRecord> getufGiftMoneyList(@Param("uid") Long uid,@Param("fid") Long fid,@Param("vipStatus") String vipStatus);


    /**
     * TODO 更新红包余额明细
     *
     * @return
     */
    void updateSendedGiftMoneyByID(UserCreditRecord userCreditRecord);  //@Param("id") Long id,@Param("vipStatus") String vipStatus


    /**
     * TODO 查询群红包中已抢红包记录
     * @return
     */

    List<UserCreditRecord> getGroupGiftMoneyList(@Param("pvid") Long pvid,@Param("user_id") Long user_id);



}
