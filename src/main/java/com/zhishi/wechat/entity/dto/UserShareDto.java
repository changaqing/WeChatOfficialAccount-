package com.zhishi.wechat.entity.dto;

public class UserShareDto {
    private Integer type;//0、智慧奇股 1、天津知柿
    private String url;//分享的url

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
