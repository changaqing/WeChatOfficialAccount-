package com.zhishi.wechat.entity.dto;

public class UserDto {
    private String code;
    private Integer type;//0、智慧奇股 1、天津知柿

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
