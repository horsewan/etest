package com.eningqu.modules.api.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.im.MsgUtil;
import com.eningqu.common.kit.MapSortKit;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.domain.system.SysCity;
import com.eningqu.modules.api.mapper.UserMapper;
import com.eningqu.modules.api.service.IBleService;
import com.eningqu.modules.api.service.IUserService;
import com.eningqu.modules.system.service.ISysCityService;
import com.eningqu.modules.system.vo.CityGroupVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/5/19 16:11
 **/
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserInfo> implements IUserService {

    @Autowired
    private IBleService bleService;

    @Autowired
    private ISysCityService sysCityService;

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByUid(Long uid) throws ServiceException {
        baseMapper.deleteOauthUser(uid);
        return true;
    }

    @Override
    public boolean updateDelStatus(Long uid, String sta) {
        baseMapper.updateDelStatus(uid, sta);
        return true;
    }

    @Override
    public UserInfo getUserInfoByPhone(String mobile) {
        return baseMapper.getUserInfoByPhone(mobile);
    }

    @Override
    public Integer getRegisterCount(String agentNo) {
        return baseMapper.selectCount(new Wrapper<UserInfo>() {
            @Override
            public String getSqlSegment() {
                String sql = " where del_status='Y'";
                if (!StringUtils.isEmpty(agentNo)) {
                    sql += " and agent_no like '" + agentNo + "%'";
                }
                return sql;
            }
        });
    }

    /**
     * 处理普通用户有经纬度数据，但分配的个代为A00000则需要重新分配个代。（需要判断是否为市级代理的用户）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAgentNo() {
        Page pageHelper = new Page(1, 50);
        List<UserInfo> infoList = baseMapper.selectByAgentNoIsDefault(pageHelper);

        //TODO 分配客服,获取出来的客服是为分配的,ble_sn is not null的个代才能绑定注册用户，但目前总个代数量是：2293434
        //TODO 根据用户经纬度进行获得所在城市，在分配该城市下的个代
        for (UserInfo userInfo : infoList) {
            boolean bool = false;
            if (!StringUtils.isEmpty(userInfo.getAddressX()) && !StringUtils.isEmpty(userInfo.getAddressY())) {
                String str = HttpUtil.get("http://restapi.amap.com/v3/geocode/regeo?key=1a32c0167e8db25ecacb5bec6ccbef74&location=" + userInfo.getAddressX() + "," + userInfo.getAddressY());
                if (str != null) {
                    String city = null;
                    JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
                    String info = jsonobject.getString("info");
                    if ("OK".equals(info)) {
                        JSONObject jsonArray = jsonobject.getJSONObject("regeocode");
                        if (jsonArray != null) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject("addressComponent");
                            if (jsonObject2 != null) {
                                city = jsonObject2.getString("city");
                                if ("[]".equals(city)) {
                                    city = jsonObject2.getString("province");
                                }
                            }
                        }
                        //TODO 得到城市名称，获取该市级代理下的所有个代（随机分配未绑定用户的个代）
                        SysCity sysCity = sysCityService.selectByCityName(city);
                        if (sysCity != null) {
                            String cityAgentNo = sysCity.getAgentNo();//市级编号
                            if (cityAgentNo != null && !"".equals(cityAgentNo)) {
                                if (cityAgentNo.contains("=")) {
                                    String[] agentNoArr = cityAgentNo.split("=");
                                    //TODO 多个市级代理则进行平均分配
                                    //统计多个市级代理现有绑定用户
                                    Map<String, Integer> agentMap = new TreeMap<String, Integer>();
                                    for (String tempAgent : agentNoArr) {
                                        if (tempAgent != null && !"".equals(tempAgent)) {
                                            int count = bleService.selectCount(new Wrapper<BleDevice>() {
                                                @Override
                                                public String getSqlSegment() {
                                                    return " where ausn like '" + tempAgent + "%' and enable_status='Y' and ble_sn is null";
                                                }
                                            });
                                            if (count == 0) {
                                                agentMap.put(tempAgent, count);
                                                break;
                                            }
                                            agentMap.put(tempAgent, count);
                                        }
                                    }

                                    BleDevice bleDevice = null;
                                    agentMap = MapSortKit.sortByValueDescending(agentMap);
                                    for (Map.Entry<String, Integer> entry : agentMap.entrySet()) {
//                                            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                                        bleDevice = bleService.selectOne(new Wrapper<BleDevice>() {
                                            @Override
                                            public String getSqlSegment() {
                                                return " where ausn like '" + entry.getKey() + "%' and enable_status='Y' and ble_sn is null";
                                            }
                                        });
                                        if (bleDevice != null) {
                                            break;
                                        }
                                    }

                                    if (bleDevice != null) {
                                        bool = true;
                                        userInfo.setAgentNo(bleDevice.getAusn());
                                        bleService.updateBleSn(bleDevice.getId(), userInfo.getId() + "");
                                    }
                                } else {
                                    String agentNo = city;
                                    BleDevice bleDevice = bleService.selectOne(new Wrapper<BleDevice>() {
                                        @Override
                                        public String getSqlSegment() {
                                            return " where ausn like '" + agentNo + "%' and enable_status='Y' and ble_sn is null";
                                        }
                                    });
                                    if (bleDevice != null) {
                                        userInfo.setAgentNo(bleDevice.getAusn());
                                        bleService.updateBleSn(bleDevice.getId(), userInfo.getId() + "");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!bool) {//系统随机分配个代（未绑定用户的个代）
                BleDevice bleDevice = bleService.getBleDeviceOne();
                if (bleDevice != null) {
                    userInfo.setAgentNo(bleDevice.getAusn());
                    bleService.updateBleSn(bleDevice.getId(), userInfo.getId() + "");
                }
            }
            baseMapper.updateById(userInfo);
        }
    }

    /**
     * 定时处理普通用户上传的联系人，对联系人分类
     */
    @Override
    public void updateUserContact() {

        //1，是否已注册成普通用户，用户角色（商户、个代、团代、市级代理）判断

        //2，联系人状态：未注册、已注册（用户角色）、是好友


    }

    /**
     * 定时提醒所有未领取全部红包的用户，推送消息方式
     */
    @Override
    public void sendMsgToUnGetProductUser() {
        System.out.println("定时推送现在开始。。。。");
        List<UserInfo> infoList = baseMapper.selectUnGetProductUser();
        for (UserInfo user : infoList) {
            MsgUtil.sendMsg(String.valueOf(user.getId()), "您今天还有红包未领取,请及时领取。");
        }
    }

    @Override
    public BigDecimal selectAllUserMoneyTotal(String agentNo) {
        return baseMapper.selectAllUserMoneyTotal(agentNo);
    }

    /**
    * 功能描述: 查询用户全国分布
    * @Author: lvbu
    * @Date: 2019/9/9 13:46
    */
    @Override
    public List<CityGroupVo> selectUserCityGroup(){
        return baseMapper.selectCityNums();
    }

}
