package com.zhishi.wechat.service;

import com.zhishi.wechat.entity.AccessTokenEntity;
import com.zhishi.wechat.entity.JsapiTicketEntity;
import com.zhishi.wechat.entity.UserEntity;
import com.zhishi.wechat.entity.WebTokenEntity;

import javax.servlet.http.HttpServletRequest;

public interface WxRequestService {

    /**
     * 获取全局token
     *
     * @return
     */
    AccessTokenEntity requestAccessToken();

    /**
     * 获取网页权限凭证信息
     *
     * @param code 临时的code 从微信接口获取
     * @return
     */
    WebTokenEntity requestWebTokenInfo(String code);

    /**
     * 获取用户信息
     *
     * @param token  全局token
     * @param openId 用户标志
     * @return
     */
    UserEntity requestUserInfo(String token, String openId);


    /**
     * 请求处理
     *
     * @param request 接收微信方请求接口返回的用户操作信息
     * @return
     */
    String processRequest(HttpServletRequest request);


    /**
     * 调用js接口的临时票据
     *
     * @param token 全局token
     * @return
     */
    JsapiTicketEntity requestJsapiTicket(String token);
}
