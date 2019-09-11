package com.eningqu.api;

import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.kit.RedisKit;
import com.eningqu.domain.api.DyConstant;
import com.eningqu.domain.api.DyUserFriendSetting;
import com.eningqu.domain.api.DyUserSetting;
import com.eningqu.service.DyConstantService;
import com.eningqu.service.DyUserFriendSettingService;
import com.eningqu.service.DyUserSettingService;
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

import java.util.List;

@EnableSwagger2
@RestController
@RequestMapping("/api/systemconfig")
public class ZSystemConfigController {

    @Autowired
    private DyConstantService dyConstantService;

    @Autowired
    private DyUserSettingService dyUserSettingService;

    @Autowired
    private DyUserFriendSettingService dyUserFriendSettingService;


    private static Logger logger = LoggerFactory.getLogger(ZSystemConfigController.class);

    @Autowired
    private RedisKit redisKit;

    @ApiOperation("设置变量--如果已有变量就更新一条，如果没有就新增一条")
    @PostMapping("/setvalue")
    public RJson setSystemValue(@RequestParam String code,@RequestParam String name ,@RequestParam String module  ,@RequestParam String url , @RequestParam String remark ) {


        List<DyConstant> dyConstants =  dyConstantService.selectByCode(code);
        if(dyConstants!=null && dyConstants.size()>0){
            DyConstant dyConstant=dyConstants.get(0);
            dyConstant.setConstantName(name);
            dyConstant.setModule(module);
            dyConstant.setUrl(url);
            dyConstant.setRemarks(remark);
            dyConstant.setUpdateId(0L);
            dyConstant.setCreateId(0L);
            dyConstantService.updateByPrimaryKey(dyConstant);

        }else{
            DyConstant dyConstant= new DyConstant();
            dyConstant.setConstantCode(code);
            dyConstant.setConstantName(name);
            dyConstant.setModule(module);
            dyConstant.setUrl(url);
            dyConstant.setRemarks(remark);
            dyConstant.setUpdateId(0L);
            dyConstant.setCreateId(0L);
            dyConstantService.insertDyConstant(dyConstant);

        }

        return RJson.ok("1").setData("");
    }

    @ApiOperation("得到变量")
    @PostMapping("/getvalue")
    public RJson getSystemValue(@RequestParam String code) {
        List<DyConstant> dyConstants =  dyConstantService.selectByCode(code);
        return RJson.ok("1").setData(dyConstants);
    }

    @ApiOperation("得到AndroidApp版本变量")
    @PostMapping("/getandroidappvalue")
    public RJson getAndroidAppValue() {
        List<DyConstant> dyConstants =  dyConstantService.selectByCode("Android-APP");
        return RJson.ok("1").setData(dyConstants);

    }
    @ApiOperation("得到IOS App版本变量")
    @PostMapping("/getiosappvalue")
    public RJson getIosAppValue() {
        List<DyConstant> dyConstants =  dyConstantService.selectByCode("IOS-APP");
        return RJson.ok("1").setData(dyConstants);
    }


    @ApiOperation("设置用户自身变量和值")
    @PostMapping("/setuservalue")
    public RJson setUserKeyValue(@RequestParam Long user_id ,@RequestParam String code,@RequestParam String value ,@RequestParam String module  , @RequestParam String remark ) {

        if(module == null || module.length()<1) module="IM";
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,code);
        if(dyUserSettings!=null && dyUserSettings.size()>0){
            DyUserSetting dyUserSetting=dyUserSettings.get(0);
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(code);
            dyUserSetting.setSettingValue(value);
            dyUserSetting.setModule(module);
            dyUserSetting.setRemarks(remark);
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.updateByPrimaryKey(dyUserSetting);
        }else{
            DyUserSetting dyUserSetting= new DyUserSetting();
            dyUserSetting.setUserId(user_id);
            dyUserSetting.setSettingCode(code);
            dyUserSetting.setSettingValue(value);
            dyUserSetting.setModule(module);
            dyUserSetting.setRemarks(remark);
            dyUserSetting.setUpdateId(0L);
            dyUserSetting.setCreateId(0L);
            dyUserSettingService.insertDyConstant(dyUserSetting);
        }
        return RJson.ok("1").setData("");
    }

    @ApiOperation("得到用户变量")
    @PostMapping("/getuservalue")
    public RJson getUserValue(@RequestParam Long user_id,@RequestParam String code) {
        List<DyUserSetting> dyUserSettings =  dyUserSettingService.selectByCode(user_id,code);
        return RJson.ok("1").setData(dyUserSettings);
    }


    @ApiOperation("设置用户用户好友的变量和值")
    @PostMapping("/setuserfriendvalue")
    public RJson setUserFriendKeyValue(@RequestParam Long user_id ,@RequestParam Long user_friend_id ,@RequestParam String code,@RequestParam String value ,@RequestParam String module  , @RequestParam String remark ) {

        if(module == null || module.length()<1) module="IM";
        List<DyUserFriendSetting> dyUserFriendSettings =  dyUserFriendSettingService.selectByCode(user_id,user_friend_id,code);
        if(dyUserFriendSettings!=null && dyUserFriendSettings.size()>0){
            DyUserFriendSetting dyUserFriendSetting=dyUserFriendSettings.get(0);
            dyUserFriendSetting.setUserId(user_id);
            dyUserFriendSetting.setUserFriendId(user_friend_id);
            dyUserFriendSetting.setSettingCode(code);
            dyUserFriendSetting.setSettingValue(value);
            dyUserFriendSetting.setModule(module);
            dyUserFriendSetting.setRemarks(remark);
            dyUserFriendSetting.setUpdateId(0L);
            dyUserFriendSetting.setCreateId(0L);
            dyUserFriendSettingService.updateByPrimaryKey(dyUserFriendSetting);
        }else{
            DyUserFriendSetting dyUserFriendSetting= new DyUserFriendSetting();
            dyUserFriendSetting.setUserId(user_id);
            dyUserFriendSetting.setUserFriendId(user_friend_id);
            dyUserFriendSetting.setSettingCode(code);
            dyUserFriendSetting.setSettingValue(value);
            dyUserFriendSetting.setModule(module);
            dyUserFriendSetting.setRemarks(remark);
            dyUserFriendSetting.setUpdateId(0L);
            dyUserFriendSetting.setCreateId(0L);
            dyUserFriendSettingService.insertDyConstant(dyUserFriendSetting);
        }
        return RJson.ok("1").setData("");
    }

    @ApiOperation("得到用户好友变量")
    @PostMapping("/getuserfriendvalue")
    public RJson getUserFriendValue(@RequestParam Long user_id,@RequestParam Long user_friend_id,@RequestParam String code) {
        List<DyUserFriendSetting> dyUserFriendSettings =  dyUserFriendSettingService.selectByCode(user_id,user_friend_id,code);
        return RJson.ok("1").setData(dyUserFriendSettings);
    }


    @ApiOperation("举报某个好友")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_id",value="用户Id",required = true,dataType="Long"),
            @ApiImplicitParam(name="user_friend_id",value="好友Id",required = true,dataType="Long"),
            @ApiImplicitParam(name="topic",value="举报标题 -- 不要超过50字符",required = true,dataType="String"),
            @ApiImplicitParam(name="remarks",value="举报内容 -- 不要超过250 字符",required = true,dataType="String"),
    })
    @PostMapping("/reportuserfriend")
    public RJson reportUserFriend(@RequestParam Long user_id,@RequestParam Long user_friend_id,@RequestParam String topic,@RequestParam String remarks) {

        String module="DaoYu";
        if(module == null || module.length()<1) module="IM";
        String code ="ReportF";
        //List<DyUserFriendSetting> dyUserFriendSettings =  dyUserFriendSettingService.selectByCode(user_id,user_friend_id,code);
        /*if(dyUserFriendSettings!=null && dyUserFriendSettings.size()>0){
            DyUserFriendSetting dyUserFriendSetting=dyUserFriendSettings.get(0);
            dyUserFriendSetting.setUserId(user_id);
            dyUserFriendSetting.setUserFriendId(user_friend_id);
            dyUserFriendSetting.setSettingCode(code);
            dyUserFriendSetting.setSettingValue(topic);
            dyUserFriendSetting.setModule(module);
            dyUserFriendSetting.setRemarks(remarks);
            dyUserFriendSetting.setUpdateId(0L);
            dyUserFriendSetting.setCreateId(0L);
            dyUserFriendSettingService.updateByPrimaryKey(dyUserFriendSetting);
        }else{

         */
            DyUserFriendSetting dyUserFriendSetting= new DyUserFriendSetting();
            dyUserFriendSetting.setUserId(user_id);
            dyUserFriendSetting.setUserFriendId(user_friend_id);
            dyUserFriendSetting.setSettingCode(code);
            dyUserFriendSetting.setSettingValue(topic);
            dyUserFriendSetting.setModule(module);
            dyUserFriendSetting.setRemarks(remarks);
            dyUserFriendSetting.setUpdateId(0L);
            dyUserFriendSetting.setCreateId(0L);
            dyUserFriendSettingService.insertDyConstant(dyUserFriendSetting);
        //}
        return RJson.ok("1").setData("举报成功");
    }

}
