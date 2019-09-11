package com.eningqu.service;

import com.eningqu.common.base.service.IBaseService;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.domain.api.UserCreditRecord;
import com.eningqu.domain.api.UserFriend;
import com.eningqu.domain.api.UserInfo;

import java.util.List;


public interface IUserFriendService extends IBaseService<UserFriend> {

    /**
     * TODO 根据查询条件查找所有的用户
     * @param s_condition
     * @return
     */
    List<UserInfo> selectByIdCondition(Long user_id, String s_condition) throws ServiceException;

    /**
     * TODO 根据查询条件查找没有成为好友的用户
     * @param s_condition
     * @return
     */
    List<UserInfo> selectByCondition( String s_condition) throws ServiceException;


    /**
     * TODO 通过手机号码列出自己的好友
     * @param mobile
     * @return
     */
    List<UserFriend> selectFriendByMobile(String mobile) throws ServiceException;


    /**
     * TODO 通过手机号码查出用户ID
     * @param mobile
     * @return
     */
    Long selectUserIDByMobile(String mobile) throws ServiceException;

    /**
     * TODO 通过用户ID列出自己的好友
     * @param user_id
     * @return
     */
    List<UserFriend> selectFriendByUserId(Long user_id) throws ServiceException;

    /**
     * TODO 加好友
     * @param user_id,friend_user_id,friend_mobile,friend_nickname
     * @return
     */
    void addFriend( Long user_id, Long user_friend_id, String friend_mobile, String friend_nickname,String isai) throws ServiceException;

    /**
     * TODO 删除
     * @param user_id,friend_user_id
     * @return
     */

    void delFriend( Long user_id, Long user_friend_id) throws ServiceException;

    /**
     * TODO 得到用户信息
     * @param user_id
     * @return
     */

    UserInfo getUserInfoById(Long user_id) throws ServiceException;

    /**
     * 选择是否有该好友的存在
     * @param user_id,friend_user_id
     * @return
     */

    List<UserFriend> selectFriendExists (Long user_id,  Long user_friend_id) throws ServiceException;


    /**
     * TODO 拉黑某个用户
     * @param user_id,friend_user_id
     * @return
     */

    void setFriendBlack( Long user_id, Long user_friend_id) throws ServiceException;;

    /**
     * TODO 拉白某个用户
     * @param user_id,friend_user_id
     * @return
     */

    void setFriendWhite( Long user_id, Long user_friend_id) throws ServiceException;;


    /**
     * TODO 得到好友黑名单列表
     * @param user_id
     * @return
     */

    List<UserFriend> selectBlackList( Long user_id) throws ServiceException;

    /**
     * TODO 得到好友白名单列表
     * @param user_id
     * @return
     */

    List<UserFriend> selectWhiteList(Long user_id) throws ServiceException;

    /**
     * TODO 设置好友分类
     * @param user_id
     * @return
     */
    void setFriendFType( Long user_id,Long user_friend_id,String ftype) throws ServiceException;

    /**
     * TODO 设置好友分类
     * @param user_id
     * @return
     */
    void setFriendAI( Long user_id,Long user_friend_id,String isai) throws ServiceException;


    /**
     * TODO 得到某个分类的所有好友列表
     * @param user_id,ftype
     * @return
     */
    List<UserFriend> selectFriendListByFtype( Long user_id, String ftype) throws ServiceException;

    /**
     * TODO 得到某个分类的所有好友的白名单列表
     * @param user_id,ftype
     * @return
     */
    List<UserFriend> selectWhiteListByFtype( Long user_id, String ftype) throws ServiceException;


    /**
     * TODO 得到某个分类的所有好友的白名单列表
     * @param user_id,ftype
     * @return
     */
    List<UserFriend> selectWhiteListByNFtype( Long user_id, String ftype) throws ServiceException;


    /**
     * TODO 设置好友分类
     * @param user_id
     * @return
     */
    void setFriendRemarks( Long user_id,Long user_friend_id,String remarks) throws ServiceException;

    /**
     * TODO 用户ID查询详细信息
     * @param user_id
     * @return
     */
    List<UserInfo> selectUserByUserId(Long user_id) throws ServiceException;

    /**
     * TODO 用户ID查询详细信息
     * @param mobile
     * @return
     */
    List<UserInfo> selectUserByMobile(String mobile) throws ServiceException;


    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    String getAIid( Long user_id) throws ServiceException;

    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    String getAIidByTeam( Long user_id) throws ServiceException;

    /**
     * TODO 用户ID查询AI消息
     * @param user_id
     * @return
     */
    String getAIidBySys( Long user_id) throws ServiceException;


    /**
     * TODO 查询红包余额
     * @param user_id
     * @return
     */

    String getGiftMoneyByID( Long user_id) throws ServiceException;

    /**
     * TODO 更新红包余额
     * @param user_id,friend_user_id,ftype
     * @return
     */

    void updateGiftMoneyByID( Long user_id, String amount) throws ServiceException;

    /**
     * TODO 添加红包余额明细
     * @param
     * @return
     */

    void insertGiftMoneyDetail(UserCreditRecord userCreditRecord) throws ServiceException;
    //Long user_id, Long user_friend_id, String vip_credit,String vip_hongbao, String  vip_status, String remarks

    /**
     * TODO 通过ID得到已发红包
     * @param
     * @return
     */

    UserCreditRecord getSendedGiftMoneyByID( Long id) throws ServiceException;

    /**
     * TODO 通过好友和用户得到所有已发红包或者已收红包
     * @param uid,fid,,vipStatus
     * @return
     */

    List<UserCreditRecord> getufGiftMoneyList( Long uid,Long fid, String vipStatus) throws ServiceException;

    /**
     * TODO 更新红包余额明细
     //* @param id,vipStatus
     * @return
     */
    void updateSendedGiftMoneyByID(UserCreditRecord userCreditRecord) throws ServiceException;

    /**
     *抢红包
     *
     */
     int fightGiftMoney(Long hid,Long user_id) throws ServiceException;


    List<UserCreditRecord> getGroupGiftMoneyList( Long pvid,Long user_id) throws ServiceException;

}
