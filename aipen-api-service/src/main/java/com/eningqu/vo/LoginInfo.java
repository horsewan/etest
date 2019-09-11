package com.eningqu.vo;

import java.io.Serializable;

/**
 *
 * @desc TODO  登录的用户信息
 * @author     Yanghuangping
 * @since      2018/5/12 18:21
 * @version    1.0
 *
 **/

public class LoginInfo implements Serializable{

    private Long id;
    private String mobile;
    private String agentNo;

    private LoginInfo(Builder builder){
        this.id = builder.id;
        this.mobile = builder.mobile;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {

        private Long id;
        private String mobile;
        private String agentNo;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder agentNo(String agentNo) {
            this.agentNo = agentNo;
            return this;
        }


        public LoginInfo build() {
            return new LoginInfo(this);
        }
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
