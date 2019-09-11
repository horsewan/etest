package com.eningqu.api;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.kit.PasswordKit;
import com.eningqu.common.kit.RSAKit;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.common.sms.CLSmsConfig;
import com.eningqu.common.sms.CLSmsProperties;
import com.eningqu.common.sms.SmsTool;
import com.eningqu.domain.api.*;
import com.eningqu.service.*;
import com.eningqu.vo.LoginInfo;
import com.eningqu.vo.LoginInfoHelper;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  登录API接口
 * @date 2018/5/2 19:38
 **/
@RestController
@RequestMapping("/api")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ISysAPIService sysAPIService;
    @Autowired
    private RedisKit redisKit;
    @Autowired
    private RSAKit rsaKit;
    @Autowired
    private SmsTool smsTool;
    @Autowired
    private CLSmsConfig smsConfig;
    @Autowired
    private IBusinessService businessService;
    @Autowired
    private IUserCreditService userCreditService;
    @Autowired
    private IBleService bleService;
    @Autowired
    private ISysCityService sysCityService;
    @Autowired
    private IUserFriendService userFriendService;

    @Autowired
    private DyUserSettingService dyUserSettingService;


    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String today = format.format(new Date());

    /**
     * @return
     */
    @PostMapping("/login/token")
    public RJson token(@RequestParam String token) {

        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        UserInfo userInfo = (UserInfo) redisKit.get(Global.REDIS_TOKEN_PREFIX + token);
        if (userInfo == null) {
            return RJson.error("已超时，请重新登录");
        }
        // 缓存用户信息
        if (!redisKit.set(Global.REDIS_TOKEN_PREFIX + token, userInfo, Global.SSESSION_EXPIRE_TIME)) {
            logger.error("缓存信息异常");
            return RJson.error("缓存信息异常");
        }
        UserInfoResult userInfoResult = new UserInfoResult();
        userInfoResult.setToken(token);
        userInfoResult.setUid(userInfo.getId());
        userInfoResult.setSex(userInfo.getSex());
        userInfoResult.setRemarks(userInfo.getRemarks());
        userInfoResult.setUserPhone(userInfo.getMobile());
        userInfoResult.setNickName(userInfo.getNickName());
        userInfoResult.setHeadImg(userInfo.getHeadImg());
        userInfoResult.setAgentNo(userInfo.getAgentNo());
        userInfoResult.setAddressD(userInfo.getAddressD());

        userInfoResult.setQRVal(userInfo.getCredential());//个人用户授权签名（移动端用于生产二维码）
        userInfoResult.setType(1l);

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(userInfo.getId());
        if (mUserCredit == null) {
            userInfoResult.setCreditTotal(new BigDecimal(0));
        } else {
            BigDecimal tempCreditTotal = mUserCredit.getVipCredit();
            if (tempCreditTotal != null && tempCreditTotal.doubleValue() > 0) {
                userInfoResult.setCreditTotal(tempCreditTotal.multiply(new BigDecimal(100)));
            } else {
                userInfoResult.setCreditTotal(new BigDecimal(0));
            }
        }
        return RJson.ok("登录成功").setData(userInfoResult);
    }

    /**
     * TODO 手机号码登录
     *
     * @param mobile
     * @param password
     * @return
     */

    @PostMapping("/login/mobile")
    public RJson mobile(@RequestParam String mobile,
                        @RequestParam String password) {

        // 防止频繁提交

        Long time = new Date().getTime();

        Long last_post_time = (Long) redisKit.get("login" + mobile);

        if ((last_post_time != null) && (time - last_post_time) < 3000) {
            // System.out.println(time-last_post_time);
            return RJson.error("操作频繁,请稍后再试!");
        } else {
            redisKit.set("login" + mobile, time);
        }

        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }

        UserInfo userInfo = userInfoService.selectByMobile(mobile);
        if (userInfo == null) {
            logger.error("手机号[" + mobile + "]未注册");
            return RJson.error("[" + mobile + "]未注册或被禁用");
        }

        byte[] salt = Hex.decode(userInfo.getCredential().substring(0, 16));
        if (!StrUtil.equalsIgnoreCase(PasswordKit.entrypt(password, salt), userInfo.getCredential())) {
            logger.error("手机号[" + mobile + "]登录密码错误");
            return RJson.error("[" + mobile + "]登录密码错误");
        }

        // 创建TOKEN
        String token = IdUtil.simpleUUID();

        try {
            LoginInfo loginInfo = LoginInfo.newBuilder()
                    .id(userInfo.getId())
                    .mobile(userInfo.getMobile())
                    .agentNo(userInfo.getAgentNo())
                    .build();

            //long time = System.currentTimeMillis() + 1000 * 2;
            boolean isLock = redisKit.getLock(Global.REDIS_TOKEN_PREFIX + token);
            if (!isLock) {
                throw new RuntimeException("系统正忙");
            }

            if (!redisKit.set(Global.REDIS_TOKEN_PREFIX + token, loginInfo, Global.SSESSION_EXPIRE_TIME)) {
                logger.error("缓存信息异常");
                return RJson.error("缓存信息异常");
            }
            //redisKit.unlock(Global.REDIS_TOKEN_PREFIX + token,String.valueOf(time));
        } catch (Exception e) {
            e.printStackTrace();
            return RJson.error("缓存信息......异常");
        }
        // 缓存用户信息
        UserInfoResult userInfoResult = new UserInfoResult();
        userInfoResult.setToken(token);
        userInfoResult.setUid(userInfo.getId());
        userInfoResult.setSex(userInfo.getSex());
        userInfoResult.setRemarks(userInfo.getRemarks());
        userInfoResult.setUserPhone(userInfo.getMobile());
        userInfoResult.setNickName(userInfo.getNickName());
        userInfoResult.setHeadImg(userInfo.getHeadImg());
        userInfoResult.setAgentNo(userInfo.getAgentNo());
        userInfoResult.setAddressD(userInfo.getAddressD());
        userInfoResult.setPaySign(userInfo.getRealName());//支付密文

        //类型
        BusinessInfo businessInfo = businessService.selectByPhone(userInfo.getMobile());
        if (businessInfo != null) {
//            userInfoResult.setQRVal(businessInfo.getbSign());//商家二维码密文
            userInfoResult.setType(2l);
        } else {
            userInfoResult.setType(1l);
        }
        userInfoResult.setQRVal(userInfo.getCredential());//个人用户授权签名（移动端用于生产二维码）,添加好友二维码
        userInfoResult.setPayQRVal(userInfo.getCredential() + ">>" + userInfo.getId());//个人收付款二维码

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(userInfo.getId());
        if (mUserCredit == null) {
            userInfoResult.setCreditTotal(new BigDecimal(0));
        } else {
            BigDecimal tempCreditTotal = mUserCredit.getVipCredit();
            if (tempCreditTotal != null && tempCreditTotal.doubleValue() > 0) {
                userInfoResult.setCreditTotal(tempCreditTotal.multiply(new BigDecimal(100)));
            } else {
                userInfoResult.setCreditTotal(new BigDecimal(0));
            }
        }

        String invateCode = getInviteCode(userInfoResult.getUid(), null, null);
        userInfoResult.setInvateCode(invateCode);

        try {
            //记录登录用户数
            if (!StringUtils.isEmpty(userInfo.getAgentNo())) {
                String agentNo = userInfo.getAgentNo().substring(0, 2);
                redisKit.setBit(Global.API_LOGIN_INFO + agentNo + ":" + today, userInfo.getId(), true);
            }
            redisKit.setBit(Global.API_LOGIN_INFO + today, userInfo.getId(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 得到邀请码


        return RJson.ok("登录成功").setData(userInfoResult);
    }

    /**
     * TODO 手机号码注册
     *
     * @param mobile   手机号
     * @param password 密码
     * @param appid    包名
     * @param code     验证码
     * @return
     */
    @PostMapping("/mobile/register")
    public RJson register(@RequestParam String mobile, @RequestParam String password, @RequestParam String appid, @RequestParam String code,
                          @RequestParam String agent, @RequestParam String udid, @RequestParam String appversion, @RequestParam String cmd,
                          @RequestParam String osversion, @RequestParam(value = "addressX", required = false) String addressX, @RequestParam(value = "addressY", required = false) String addressY, @RequestParam(value = "inviteCode", required = false)  String inviteCode) {

//        logger.error("+++++++++++++++++++++++++++++++++++"+mobile+"|"+password+"|"+appid+"|"+code+"|"+agent+"|"+udid+"|"+appversion+"|"+cmd+"|"+osversion);

        if (mobile == null && "".equals(mobile))
            return RJson.error("手机号格式错误");
        if (!Validator.isMobile(mobile)) {
            return RJson.error("手机号格式错误");
        }

        if (password == null && "".equals(password))
            return RJson.error("密码错误");
        if (appid == null && "".equals(appid))
            return RJson.error("包名错误");
        if (code == null && "".equals(code))
            return RJson.error("验证码错误");
        if (addressX == null && "".equals(addressX))
            return RJson.error("经度数据错误");
        if (addressY == null && "".equals(addressY))
            return RJson.error("纬度数据错误");

        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectUserRegBySta();
        if (sysAPIInfo != null) {
            return RJson.error("用户注册已被管理员禁用");
        }
        //TODO add userType
        String smsCode = (String) redisKit.get(Global.REDIS_SMS_PREFIX + mobile);
        if (!StrUtil.equalsIgnoreCase(code, smsCode)) {
            logger.error("手机号码注册, 手机号：{}，验证码不正确", mobile);
            return RJson.error(mobile + "验证码不正确");
        }

        if (userInfoService.checkMobile(mobile)) {
            logger.error("手机号码注册, 手机号:{}已注册", mobile);
            return RJson.error(mobile + "该手机号码已注册");
        }

        try {
            userInfoService.register(mobile, password, appid, addressX, addressY);
        } catch (ServiceException e) {
            logger.error("手机号:{}注册异常，{}", mobile, e);
            return RJson.error(mobile + "注册失败");
        }

        // 清除验证码
        redisKit.del(Global.REDIS_SMS_PREFIX + mobile);
        String cityCode = "";
        String cityName = "";
        //内部登录操作
        UserInfo userInfo = userInfoService.selectByMobile(mobile);
        if (userInfo == null) {
            logger.error("手机号[" + mobile + "]未注册");
            return RJson.error("手机号[" + mobile + "]未注册或被禁用");
        } else {
            if (userInfo.getNickName() == null) {
                //随机生成昵称
//                userInfo.setNickName(PasswordKit.getRandomJianHan(5));
                userInfo.setNickName("DY"+userInfo.getMobile().substring(userInfo.getMobile().length()-4));
            }
            /*if (userInfo.getAgentNo() == null) {
                //TODO 分配客服,获取出来的客服是为分配的,ble_sn is not null的个代才能绑定注册用户，但目前总个代数量是：2293434
                //TODO 根据用户经纬度进行获得所在城市，在分配该城市下的个代
                boolean bool = false;
                if ((userInfo.getAddressX() != null && !"".equals(userInfo.getAddressX())
                        && (userInfo.getAddressY() != null && !"".equals(userInfo.getAddressY())))) {
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
                                cityCode = sysCity.getShengNo();//城市编码
                                cityName = sysCity.getShengName();//城市名称
                                if (cityAgentNo != null && !"".equals(cityAgentNo)) {
                                    if (cityAgentNo.contains("=")) {
                                        String[] agentNoArr = cityAgentNo.split("=");
                                        //TODO 多个市级代理则进行平均分配
                                        //统计多个市级代理现有绑定用户
                                        Map<String, Integer> agentMap = new TreeMap<String, Integer>();
                                        for (String tempAgent : agentNoArr) {
                                            if (tempAgent != null && !"".equals(tempAgent)) {
                                                int count = bleService.getBleDeviceCountByAusn(tempAgent);
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
                                            bleDevice = bleService.getBleDeviceOneByAusn(entry.getKey());
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
                                        BleDevice bleDevice = bleService.getBleDeviceOneByAusn(city);
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
            }*/

            userInfoService.updateById(userInfo);
        }

        UserInfoResult userInfoResult = new UserInfoResult();
        String token = IdUtil.simpleUUID();
        userInfoResult.setToken(token);
        userInfoResult.setUid(userInfo.getId());
        userInfoResult.setSex(userInfo.getSex());
        userInfoResult.setRemarks(userInfo.getRemarks());
        userInfoResult.setUserPhone(userInfo.getMobile());
        userInfoResult.setNickName(userInfo.getNickName());
        userInfoResult.setHeadImg(userInfo.getHeadImg());
        userInfoResult.setAgentNo(userInfo.getAgentNo());
        userInfoResult.setAddressD(userInfo.getAddressD());
        userInfoResult.setPaySign(userInfo.getRealName());//支付密文

        //类型
        BusinessInfo businessInfo = businessService.selectByPhone(userInfo.getMobile());
        if (businessInfo != null) {
//            userInfoResult.setQRVal(businessInfo.getbSign());//商家二维码密文
            userInfoResult.setType(2l);
        } else {
            userInfoResult.setType(1l);
        }
        userInfoResult.setQRVal(userInfo.getCredential());//个人用户授权签名（移动端用于生产二维码）
        userInfoResult.setPayQRVal(userInfo.getCredential() + ">>" + userInfo.getId());//个人收付款二维码

        UserCredit mUserCredit = userCreditService.selectUserCreditByUid(userInfo.getId());
        if (mUserCredit == null) {
            userInfoResult.setCreditTotal(new BigDecimal(0));
        } else {
            BigDecimal tempCreditTotal = mUserCredit.getVipCredit();
            if (tempCreditTotal != null && tempCreditTotal.doubleValue() > 0) {
                userInfoResult.setCreditTotal(tempCreditTotal.multiply(new BigDecimal(100)));
            } else {
                userInfoResult.setCreditTotal(new BigDecimal(0));
            }

        }
        // 生成邀请码
        // 得到邀请码
        String myInvateCode = getInviteCode(userInfoResult.getUid(), cityCode, cityName);
        userInfoResult.setInvateCode(myInvateCode);
        // 设定红包返现--给推荐好友或者其他渠道返现
        //new GiftMoneyUtil().setGiftMoneyByInvate();

        if(inviteCode!=null && inviteCode.length()>5){
            setSendGiftMoney(userInfoResult.getUid(),"10",inviteCode);  //好友邀请奖励红包10快
        }


        // 创建TOKEN
        LoginInfo loginInfo = LoginInfo.newBuilder()
                .id(userInfo.getId())
                .mobile(userInfo.getMobile())
                .build();
        // 缓存用户信息
        if (!redisKit.set(Global.REDIS_TOKEN_PREFIX + token, loginInfo, Global.SSESSION_EXPIRE_TIME)) {
            logger.error("缓存信息异常");
            return RJson.error("缓存信息异常");
        }


        return RJson.ok("注册成功").setData(userInfoResult);
    }

    /**
     * TODO 发送手机验证码
     *
     * @param mobile
     */
    @GetMapping("/captcha/{mobile}")
    public RJson sendCaptcha(@PathVariable String mobile) {
        if (mobile == null && "".equals(mobile))
            return RJson.error("手机号格式错误");
        if (!Validator.isMobile(mobile)) {
            return RJson.error("手机号格式错误");
        }
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        // 若验证码没过期，则不发送短信
        String code = (String) redisKit.get(Global.REDIS_SMS_PREFIX + mobile);
        if (!StrUtil.isBlank(code)) {
            return RJson.error("验证码依然可用，不在发送新的短信验证码");
        }
        code = RandomUtil.randomString("123456789", 6);
        if (!smsTool.sendSms(mobile, code)) {
            logger.error("手机号:{}发送验证码失败", mobile);
            return RJson.error("验证码发送失败");
        }
        logger.debug("手机号:{},验证码:{}发送成功", mobile, code);
        // 五分钟验证码过期
        redisKit.set(Global.REDIS_SMS_PREFIX + mobile, code, Global.SMS_EXPIRE_TIME);
        return RJson.ok("验证码发送成功");
    }

    /**
     * TODO 短信发送
     *
     * @param mobile
     * @param tag
     * @param tplNo
     * @return
     */
    @GetMapping("/sms/{tag}/{tplNo}/{mobile}")
    public RJson sendSms(@PathVariable String mobile,
                         @PathVariable String tag,
                         @PathVariable int tplNo) {
        if (mobile == null && "".equals(mobile))
            return RJson.error("手机号格式错误");
        if (!Validator.isMobile(mobile)) {
            return RJson.error("手机号格式错误");
        }
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectUserRegBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        try {
            if (!Validator.isMobile(mobile)) {
                return RJson.error("手机号格式错误");
            }

            // 若验证码没过期，则不发送短信
            String code = (String) redisKit.get(Global.REDIS_SMS_PREFIX + mobile);
            if (!StrUtil.isBlank(code)) {
                return RJson.error("验证码未过期，请使用上一次发送的验证码");
            }

            CLSmsProperties properties = smsConfig.getTag().get(tag);
            if (properties == null) {
                return RJson.error("未找到短信TAG标记");
            }
            if (properties.getTpl().size() < tplNo) {
                return RJson.error("未找到短信模板");
            }

            // 生成随机验证码
            code = RandomUtil.randomString("123456789", 6);

            Map<String, String> json = new HashMap<>();
            json.put("account", properties.getAccount());
            json.put("password", properties.getPassword());
            json.put("msg", properties.getTpl().get(tplNo));
            json.put("params", mobile + "," + code + "," + properties.getTimeout());
            json.put("report", "true");
            SmsTool.send(properties.getUrl(), JSONUtil.toJsonStr(json));

            // 缓存验证码
            redisKit.set(Global.REDIS_SMS_PREFIX + mobile, code, properties.getTimeout() * 60);

            logger.debug("手机号：{}验证码：{}发送成功", mobile, code);

        } catch (AipenException e) {
            logger.error("手机号：{}的验证码发送异常，{}", mobile, e.getMessage());
            return RJson.error(e.getMessage());
        }
        return RJson.ok("短信验证码发送成功");

    }

    /**
     * TODO 检测手机号是否注册
     *
     * @param mobile
     * @return
     */
    @GetMapping("/check/mobile/{mobile}")
    public RJson checkMobile(@PathVariable String mobile) {
        if (mobile == null && "".equals(mobile))
            return RJson.error("手机号格式错误");
        if (!Validator.isMobile(mobile)) {
            return RJson.error("手机号格式错误");
        }
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        // 判断手机号是否已被第三方授权登录用户绑定
        UserInfo userInfo = userInfoService.selectByMobile(mobile);
//        Map<String, Object> result = Maps.newHashMap();
        if (userInfo != null) {
            // 已被注册 但未绑定第三方授权登录的用户
            /*OauthUser oauthUser = oauthUserService.selectByUid(userInfo.getId());
            if(oauthUser == null){
                result.put("isBindOauth", false);
                return RJson.ok("已被注册,未绑定第三方授权账号").setData(result);
            }else{
                return RJson.error("已被注册,未绑定第三方授权账号");
            }*/
            return RJson.error(mobile + "该手机号码已注册");
        }
//        result.put("isBindOauth", true);
        return RJson.ok("手机号未注册");
    }

    /**
     * TODO 重置密码
     *
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    @PostMapping("/forgetPwd")
    public RJson forgetPassword(@RequestParam String mobile,
                                @RequestParam String password,
                                @RequestParam String code) {
        if (mobile == null && "".equals(mobile))
            return RJson.error("[" + mobile + "]手机号格式错误");
        if (!Validator.isMobile(mobile)) {
            return RJson.error("[" + mobile + "]手机号格式错误");
        }
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        String smsCode = (String) redisKit.get(Global.REDIS_SMS_PREFIX + mobile);
        if (!StrUtil.equalsIgnoreCase(code, smsCode)) {
            logger.error("验证码不正确");
            return RJson.error("[" + mobile + "]验证码不正确");
        }

        UserInfo userInfo = userInfoService.selectByMobile(mobile);

        if (userInfo == null) {
            logger.error("[" + mobile + "]不存在");
            return RJson.error("[" + mobile + "]未注册");
        }

        userInfo.setCredential(PasswordKit.entrypt(password));
        userInfoService.updateById(userInfo);

        // 清除验证码
        redisKit.del(Global.REDIS_SMS_PREFIX + mobile);

        return RJson.ok("[" + mobile + "]修改成功");
    }

    @PostMapping("/user/resetPwd")
    public RJson resetPwd(@RequestParam String mobile, @RequestParam String oldPassword, @RequestParam String password) {
        if (mobile == null && "".equals(mobile))
            return RJson.error("[" + mobile + "]手机号格式错误");
        if (!Validator.isMobile(mobile)) {
            return RJson.error("[" + mobile + "]手机号格式错误");
        }
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }

        UserInfo userInfo = userInfoService.selectByMobile(mobile);
        if (userInfo == null) {
            return RJson.error("[" + mobile + "]未注册或被禁用");
        }

        byte[] salt = Hex.decode(userInfo.getCredential().substring(0, 16));
        String oldPwd = PasswordKit.entrypt(oldPassword, salt);
        if (!StrUtil.equalsIgnoreCase(oldPwd, userInfo.getCredential())) {
            return RJson.error("[" + mobile + "]原密码错误");
        }

//        if(StrUtil.equalsIgnoreCase(PasswordKit.entrypt(password), oldPwd)){
//            return RJson.error("手机号["+mobile+"]新密码不能与原密码一致");
//        }
        userInfo.setCredential(PasswordKit.entrypt(password));
        userInfoService.updateById(userInfo);

        return RJson.ok("[" + mobile + "]修改成功");
    }

    @PostMapping("/user/logout")
    public RJson logout(@RequestParam String token) {
        Long uId = LoginInfoHelper.getUserID();
        if (token == null || "".equals(token)) {
            return RJson.error("该用户不存在,请重试。");
        }
        UserInfo userInfo = userInfoService.selectById(uId);
        if (userInfo == null) {
            return RJson.error("该用户不存在");
        }
        Object object = redisKit.get(Global.REDIS_TOKEN_PREFIX + token);
        if (object != null) {
            LoginInfo loginInfo = (LoginInfo) object;
            if (loginInfo != null) {
                Long tempUid = loginInfo.getId();
                if (!uId.equals(tempUid)) {
                    return RJson.error("该用户未登录");
                }
            }
        }
        redisKit.del(Global.REDIS_TOKEN_PREFIX + token);
        try {
            //记录登录用户数
            if (!StringUtils.isEmpty(userInfo.getAgentNo())) {
                String agentNo = userInfo.getAgentNo().substring(0, 2);
                redisKit.setBit(Global.API_LOGIN_INFO + agentNo + ":" + today, userInfo.getId(), false);
            }
            redisKit.setBit(Global.API_LOGIN_INFO + today, userInfo.getId(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RJson.ok("退出成功");
    }

    /**
     * ################ 测试专用 ##################
     */

    @GetMapping("/test")
    @Profile(value = {"dev", "uat"})
    public RJson test() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("openId", "oX6EauJalCGV74dEJWci4D96N9pE");
        map.put("nickName", "道域科技");
        map.put("headImg", "http://wx.qlogo.cn/mmopen/gWicbXPiajJn8iagQShGoD9xfHpexu62ianSNVWJxR9XUNBibAm9jBz3Z6GkamP33FELibaEalzNcz3fOqq5yL9ZckC2D1nzd98l3E/132");
        map.put("sex", "1");
        map.put("pkgName", "com.eningqu.aipen");
        map.put("userType", "1");
        return RJson.ok().setData(rsaKit.encrypt(JSONUtil.toJsonStr(map)));
    }

    @GetMapping("/jiemi")
    @Profile(value = {"dev", "uat"})
    public RJson jiemi(@RequestParam String dataJson) {
        // RSA秘钥解密
        String stringJson = null;
        try {
            stringJson = rsaKit.decrypt(dataJson);
        } catch (Exception e) {
            logger.error("", e);
            return RJson.error("解密失败");

        }
        return StrUtil.isBlank(stringJson) ? RJson.ok("解密失败") : RJson.ok("解密成功").setData(stringJson);
    }

    @PostMapping("/user/update")
    public RJson updateUserInfo(@RequestParam(required = false) String nickName, @RequestParam(required = false) String headImg,
                                @RequestParam(required = false) String addressD, @RequestParam(required = false) String remarks, @RequestParam(required = false) String sex) {
        Long uId = LoginInfoHelper.getUserID();
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        if (uId <= 0) {
            return RJson.error("该用户不存在");
        }

        UserInfo userInfo = userInfoService.selectById(uId);
        if (userInfo == null) {
            return RJson.error("该用户不存在");
        }

        if (nickName != null && !"".equals(nickName))
            userInfo.setNickName(nickName);
        if (headImg != null && !"".equals(headImg))
            userInfo.setHeadImg(headImg);
       /* if(addressX!=null&&!"".equals(addressX))
            userInfo.setAddressX(addressX);
        if(addressY!=null&&!"".equals(addressY))
            userInfo.setAddressY(addressY);*/
        if (addressD != null && !"".equals(addressD))
            userInfo.setAddressD(addressD);
        if (remarks != null && !"".equals(remarks))
            userInfo.setRemarks(remarks);
        if (sex != null && !"".equals(sex) && ("M".equals(sex) || "F".equals(sex)))
            userInfo.setSex(sex);

        userInfoService.updateById(userInfo);

        return RJson.ok("修改成功");
    }

    /**
     * 扫二维码获取得到密文进行添加好友申请
     *
     * @param userQrVal
     * @return
     */
    @PostMapping("/user/getUserQRVal")
    public RJson getUserQRVal(@RequestParam Long uId, @RequestParam String userQrVal) {

        if (uId < 0) {
            return RJson.error("该用户不存在");
        }
        if (userQrVal == null || "".equals(userQrVal)) {
            return RJson.error("该用户不存在,请重试。");
        }

        UserInfo userInfo = userInfoService.selectById(uId);
        if (userInfo == null) {
            return RJson.error("该用户不存在");
        }
        userInfo = userInfoService.selectByuserQrVal(userQrVal);
        if (userInfo == null) {
            return RJson.error("该用户不存在,请重试。");
        }

        UserInfoQRC userInfoQRC = new UserInfoQRC();
        userInfoQRC.setAddressD(userInfo.getAddressD());
        userInfoQRC.setHeadImg(userInfo.getHeadImg());
        userInfoQRC.setId(userInfo.getId());
        userInfoQRC.setMobile(userInfo.getMobile());
        userInfoQRC.setNickName(userInfo.getNickName());
        userInfoQRC.setSex(userInfo.getSex());
        userInfoQRC.setRemarks(userInfo.getRemarks());
        List<UserFriend> userFriendList = userFriendService.selectFriendExists(uId, userInfo.getId());
        if (userFriendList != null && userFriendList.size() > 0) {
            userInfoQRC.setfType("Y");//是好友
            UserFriend userFriend = userFriendList.get(0);
            if (userFriend != null) {
                String blackList = userFriend.getBlacklist();
                if (blackList != null) {
                    if ("Y".equals(blackList)) {
                        userInfoQRC.setIsBlack("Y");
                    } else {
                        userInfoQRC.setIsBlack("N");
                    }
                } else {
                    userInfoQRC.setIsBlack("N");
                }
            }
        } else {
            userInfoQRC.setfType("N");//不是好友
        }
        return RJson.ok().setData(userInfoQRC);
    }

    @PostMapping("/user/getUserNearDetail")
    public RJson getUserQRVal(@RequestParam Long nearId) {
        Long uId = LoginInfoHelper.getUserID();
        if (nearId == null || "".equals(nearId)) {
            return RJson.error("该用户不存在,请重试。");
        }

        UserInfo userInfo = userInfoService.selectById(uId);
        if (userInfo == null) {
            return RJson.error("该用户不存在");
        }
        userInfo = userInfoService.selectById(nearId);
        if (userInfo == null) {
            return RJson.error("该用户不存在,请重试。");
        }

        UserInfoQRC userInfoQRC = new UserInfoQRC();
        userInfoQRC.setAddressD(userInfo.getAddressD());
        userInfoQRC.setHeadImg(userInfo.getHeadImg());
        userInfoQRC.setId(userInfo.getId());
        userInfoQRC.setMobile(userInfo.getMobile());
        userInfoQRC.setNickName(userInfo.getNickName());
        userInfoQRC.setSex(userInfo.getSex());
        userInfoQRC.setRemarks(userInfo.getRemarks());
        List<UserFriend> userFriendList = userFriendService.selectFriendExists(uId, userInfo.getId());
        if (userFriendList != null && userFriendList.size() > 0) {
            userInfoQRC.setfType("Y");//是好友
            UserFriend userFriend = userFriendList.get(0);
            if (userFriend != null) {
                String blackList = userFriend.getBlacklist();
                if (blackList != null) {
                    if ("Y".equals(blackList)) {
                        userInfoQRC.setIsBlack("Y");
                    } else {
                        userInfoQRC.setIsBlack("N");
                    }
                } else {
                    userInfoQRC.setIsBlack("N");
                }
            }
        } else {
            userInfoQRC.setfType("N");//不是好友
        }
        return RJson.ok().setData(userInfoQRC);
    }

    @ApiOperation("用户设置支付密文")
    @PostMapping("/user/setPaySign")
    public RJson updateUserInfoPaySign(@RequestParam String paySign) {
        Long uId = LoginInfoHelper.getUserID();
        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }
        if (uId <= 0) {
            return RJson.error("该用户不存在");
        }
        if (paySign == null || "".equals(paySign))
            return RJson.error("支付密文参数错误");

        UserInfo userInfo = userInfoService.selectById(uId);
        if (userInfo == null) {
            return RJson.error("该用户不存在");
        }
        userInfo.setRealName(paySign);

        userInfoService.updateById(userInfo);

        return RJson.ok("设置成功").setData(paySign);
    }

    @ApiOperation("用户找回支付密文")
    @PostMapping("/user/resetPaySign")
    public RJson resetPaySign(@RequestParam String mobile, @RequestParam String code) {

        //总开关
        SysAPIInfo sysAPIInfo = sysAPIService.selectSysAPIBySta();
        if (sysAPIInfo != null) {
            return RJson.error("该系统已被管理员禁用");
        }

        if (mobile == null || "".equals(mobile))
            return RJson.error("支付密文参数错误");

        if (code == null || "".equals(code))
            return RJson.error("支付密文参数错误");

        //TODO add userType
        String smsCode = (String) redisKit.get(Global.REDIS_SMS_PREFIX + mobile);
        if (!StrUtil.equalsIgnoreCase(code, smsCode)) {
            logger.error("手机号码注册, 手机号：{}，验证码不正确", mobile);
            return RJson.error(mobile + "验证码不正确");
        }

        UserInfo userInfo = userInfoService.selectByMobile(mobile);
        if (userInfo != null) {
            userInfo.setRealName("");
            userInfoService.updateById(userInfo);
            return RJson.ok("找回成功");
        } else {
            return RJson.error("当前用户不存在");
        }
    }

    /**
     * 用户获取附件其它用户数据
     *
     * @param addressX
     * @param addressY
     * @param dis
     * @return
     */
    @PostMapping("/user/getUserNearFriend")
    public RJson getUserNearFriend(@RequestParam String addressX, @RequestParam String addressY, @RequestParam String dis, @RequestParam String sex) {
        Long uId = LoginInfoHelper.getUserID();
        if (addressX == null || "".equals(addressX)) {
            return RJson.error("该用户经纬度参数错误");
        }
        if (addressY == null || "".equals(addressY)) {
            return RJson.error("该用户经纬度参数错误");
        }
        double longitude = Double.valueOf(addressX);
        double latitude = Double.valueOf(addressY);

        //TODO 更新当前用户经纬度信息
        UserInfo userInfoByAddress = userInfoService.selectById(uId);
        if(userInfoByAddress!=null){
            userInfoByAddress.setAddressX(addressX);
            userInfoByAddress.setAddressY(addressY);
            userInfoService.updateById(userInfoByAddress);
        }

        //先计算查询点的经纬度范围
        double r = 6371;//地球半径千米
        double tempDis = Double.valueOf(dis);//0.5千米距离
        double dlng = 2 * Math.asin(Math.sin(tempDis / (2 * r)) / Math.cos(latitude * Math.PI / 180));
        dlng = dlng * 180 / Math.PI;//角度转为弧度
        double dlat = tempDis / r;
        dlat = dlat * 180 / Math.PI;
        double minlat = latitude - dlat;
        double maxlat = latitude + dlat;
        double minlng = longitude - dlng;
        double maxlng = longitude + dlng;

        DataTable<UserInfo> dataTable = new DataTable();
        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_x" + SearchParam.BETWEEN_START_WITH, minlng);
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_x" + SearchParam.BETWEEN_END_WITH, maxlng);
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_y" + SearchParam.BETWEEN_START_WITH, minlat);
        searchParams.put(SearchParam.SEARCH_BETWEEN + "address_y" + SearchParam.BETWEEN_END_WITH, maxlat);
        if (sex != null && !"".equals(sex)) {
            searchParams.put(SearchParam.SEARCH_EQ + "sex", sex);
        }

        dataTable.setFields(new String[]{"id", "nick_name", "head_img", "sex", "remarks", "address_x", "address_y", "update_time"});
        dataTable.setPage(1);
        dataTable.setLimit(50);
        dataTable.setSearchParams(searchParams);

        userInfoService.pageSearch(dataTable);

//        InfoResultTable<UserInfoNear> infoResultTable = new InfoResultTable();
//        infoResultTable.setCount(dataTable.getCount());
//        infoResultTable.setLimit(dataTable.getLimit());
//        infoResultTable.setPage(dataTable.getPage());

        List<UserInfo> userInfoList = dataTable.getData();
        List<UserInfoNear> userInfoNearList = new ArrayList<>();
        if (userInfoList != null && userInfoList.size() > 0) {
            for (UserInfo userInfo : userInfoList) {
                if (userInfo.getId().equals(uId)) {//排除自己
                    continue;
                }
                UserInfoNear userInfoNear = new UserInfoNear();
                userInfoNear.setAddressX(userInfo.getAddressX());
                userInfoNear.setAddressY(userInfo.getAddressY());
                userInfoNear.setHeadImg(userInfo.getHeadImg());
                userInfoNear.setId(userInfo.getId());
                userInfoNear.setNickName(userInfo.getNickName());
                userInfoNear.setRemarks(userInfo.getRemarks());
                userInfoNear.setSex(userInfo.getSex());
                userInfoNear.setUpdateTime(userInfo.getUpdateTime());
                userInfoNearList.add(userInfoNear);
            }
//            infoResultTable.setData(userInfoNearList);
        } else {
            return RJson.ok();
        }
        return RJson.ok().setData(userInfoNearList);
    }


    public String getInviteCode(Long user_id, String cityCode, String cityName) {
        String inviteCode = "inviteCode";
        List<DyUserSetting> dyUserSettings = dyUserSettingService.selectByCode(user_id, inviteCode);
        String invateValue = get6RandomCode();
        if (dyUserSettings == null || dyUserSettings.size() < 1) {

            DyUserSetting dyUserSetting = new DyUserSetting();
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(inviteCode);
            dyUserSetting.setSettingValue(invateValue);
            dyUserSetting.setModule("Global");
            dyUserSetting.setRemarks(".");
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.insertDyConstant(dyUserSetting);

        } else {
            invateValue = dyUserSettings.get(0).getSettingValue();
        }
        if (!StringUtils.isEmpty(cityCode)) {
            DyUserSetting userSetting = new DyUserSetting();
            userSetting.setUserId(user_id);
            userSetting.setSettingCode(cityCode);
            userSetting.setSettingValue(cityName);
            userSetting.setModule("City");
            userSetting.setUpdateId(0L);
            userSetting.setRemarks("用户所在城市");
            userSetting.setCreateId(0L);
            dyUserSettingService.insertDyConstant(userSetting);
        }

        return invateValue;
    }

    public String get6RandomCode() {
        char[] arr = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
        int i = 1;
        String rString = "";
        while (i++ <= 8) { //循环六次，得到六位数的验证码
            char msg = arr[(int) (Math.random() * 62)];
            //System.out.print(msg);
            rString += msg;
        }
        return rString.toLowerCase();
    }

    public void setSendGiftMoney(Long user_id, String amount, String inviteCode) {
        List<DyUserSetting> dyUserSettings = dyUserSettingService.selectByCodeValue("inviteCode", inviteCode);
        Long invite_user_id = null;
        if (dyUserSettings != null && dyUserSettings.size() > 0) {
            invite_user_id = dyUserSettings.get(0).getUserId();
        }
        if (invite_user_id != null) {
            sysSendGiftMoney(invite_user_id, amount, "InVite");
        }
        //if(inviteCode)
    }

    public void sysSendGiftMoney(Long user_id, String amount, String sourceType) {
        double hAmount = 0.00;//红包金额
        double mAmount = 0.00; //我的余额
        String sAmount = userFriendService.getGiftMoneyByID(user_id);
        hAmount = Double.parseDouble(amount.trim());
        mAmount = Double.parseDouble(sAmount.trim());
        mAmount = mAmount + hAmount;
        UserCreditRecord ucr = new UserCreditRecord();
        ucr.setUid(user_id);
        ucr.setFid(0l);
        ucr.setRemarks("");
        ucr.setVipHongbao(-1);
        ucr.setVipStatus(1);
        ucr.setVipCredit(BigDecimal.valueOf(hAmount));
        ucr.setSource(sourceType);
        userFriendService.insertGiftMoneyDetail(ucr);  //加一条明细  //user_id, user_friend_id, amount.trim(),"-1", "3", remarks
        userFriendService.updateGiftMoneyByID(user_id, String.valueOf(mAmount));  //更新余额
    }


}
