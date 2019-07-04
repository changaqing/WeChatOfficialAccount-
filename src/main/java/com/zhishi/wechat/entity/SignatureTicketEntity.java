package com.zhishi.wechat.entity;

/**
 * 签名&票据
 */
public class SignatureTicketEntity extends ErrorEntity {

    private String appId;//公众号
    private Long timestamp;//时间戳
    private String noncestr;//随机字符串
    private String signature;//签名

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
