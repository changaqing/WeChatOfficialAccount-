package com.zhishi.wechat.entity.dto;

public class LoginDto {
    private String code;
    private Integer type;//0、第一个小程序    默认0

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
