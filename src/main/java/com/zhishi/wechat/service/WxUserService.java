package com.zhishi.wechat.service;

import com.zhishi.wechat.entity.SignatureTicketEntity;
import com.zhishi.wechat.entity.UserEntity;

import java.io.UnsupportedEncodingException;

public interface WxUserService {

    /**
     * 获取用户信息
     *
     * @param code
     * @return
     */
    UserEntity getUserInfo(String code);


    /**
     * 用户分享
     *
     * @param url
     * @return
     */
    SignatureTicketEntity userShare(String url) throws UnsupportedEncodingException;
}
