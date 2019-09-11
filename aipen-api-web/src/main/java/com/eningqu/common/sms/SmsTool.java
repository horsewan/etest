package com.eningqu.common.sms;

import cn.hutool.http.HttpRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/10 16:18
 * @version    1.0
 *
 **/

public class SmsTool {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private SmsConfig smsConfig;

    public SmsTool(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    /**
     * TODO 阿里云短信服务
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    public boolean sendSms(String phone, String code){

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        SendSmsResponse response = null;
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfig.getAccessKeyId(), smsConfig.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName(smsConfig.getSignature());
            request.setTemplateCode(smsConfig.getTplCode());
            request.setTemplateParam("{\"code\": "+code+"}");

            //hint 此处可能会抛出异常，注意catch
            response = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            logger.error("手机号["+phone+"],短信发送失败", e);
            return false;
        }

        if(response == null){
            return false;
        }

        if(!"OK".equalsIgnoreCase(response.getCode())){
            logger.error("Code=" + response.getCode() + ",Message=" + response.getMessage());
            return false;
        }
        return true;
    }


    /**
     * TODO 创蓝短信服务
     * @param url           请求地址
     * @param paramJson     JSON数据格式
     * @return
     */
    public static String send(String url, String paramJson){
        return HttpRequest
                .post(url)
                .timeout(-1)
                .body(paramJson, "application/json")
                .execute()
                .body();
    }
}
