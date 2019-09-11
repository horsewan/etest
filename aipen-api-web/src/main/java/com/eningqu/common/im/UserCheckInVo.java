package com.eningqu.common.im;

public class UserCheckInVo {
    String userId;

    public String getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(String checkInCount) {
        this.checkInCount = checkInCount;
    }

    public String getLastCheckInDate() {
        return lastCheckInDate;
    }

    public void setLastCheckInDate(String lastCheckInDate) {
        this.lastCheckInDate = lastCheckInDate;
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(String giftCount) {
        this.giftCount = giftCount;
    }

    String checkInCount;
    String lastCheckInDate;
    String giftCount;  // 这次应该给的次数或者签到的元

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
