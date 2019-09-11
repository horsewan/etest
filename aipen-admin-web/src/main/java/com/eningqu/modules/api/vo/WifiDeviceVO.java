package com.eningqu.modules.api.vo;
/**
 *
 * 描    述：  TODO   
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/27 16:04
 *
 */
public class WifiDeviceVO {

    private String nickname;
    private String deviceId;
    private Integer firstBindDate;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getFirstBindDate() {
        return firstBindDate;
    }

    public void setFirstBindDate(int firstBindDate) {
        this.firstBindDate = firstBindDate;
    }
}
