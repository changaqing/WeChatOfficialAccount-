package com.zhishi.wechat.service.impl;

import com.google.gson.Gson;
import com.zhishi.wechat.controller.WxUserController;
import com.zhishi.wechat.entity.*;
import com.zhishi.wechat.entity.dto.UserShareDto;
import com.zhishi.wechat.service.WxRequestService;
import com.zhishi.wechat.service.WxUserService;
import com.zhishi.wechat.util.MD5Util;
import com.zhishi.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class WxUserServiceImpl implements WxUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxUserController.class);
    private Gson gson = new Gson();

    @Autowired
    private WxRequestService wxRequestService;

    @Override
    public UserEntity getUserInfo(String code) {
        UserEntity userInfo = new UserEntity();
        //获取网页凭证
        WebTokenEntity webTokenInfo = wxRequestService.requestWebTokenInfo(code);
        //获取网页凭证没有错误
        if (webTokenInfo.getErrcode() == null) {
            LOGGER.info("获取网页凭证得到的数据：" + gson.toJson(webTokenInfo));
            //获取全局token
            AccessTokenEntity accessToken = wxRequestService.requestAccessToken();
            if (accessToken.getErrcode() == null) {
                LOGGER.info("获取全局token得到的数据：" + gson.toJson(accessToken));
                //获取用户信息
                userInfo = wxRequestService.requestUserInfo(accessToken.getAccess_token(), webTokenInfo.getOpenid());
                if (userInfo.getErrcode() == null) {
                    LOGGER.info("获取用户信息得到的数据：" + gson.toJson(userInfo));
                    return userInfo;
                }
                LOGGER.info("获取用户信息得到的错误数据：" + gson.toJson(userInfo));
                return userInfo;
            }
            LOGGER.warn("获取全局token得到的错误数据：" + gson.toJson(accessToken));
            userInfo.setErrcode(accessToken.getErrcode());
            userInfo.setErrmsg(accessToken.getErrmsg());
            return userInfo;
        }
        LOGGER.warn("获取网页凭证得到的错误数据：" + gson.toJson(webTokenInfo));
        userInfo.setErrcode(webTokenInfo.getErrcode());
        userInfo.setErrmsg(webTokenInfo.getErrmsg());
        return userInfo;
    }


    @Override
    public SignatureTicketEntity userShare(String url) throws UnsupportedEncodingException {
        LOGGER.info("得到未解码的url:" + url);
        url = URLDecoder.decode(url, "UTF-8");
        LOGGER.info("得到已解码的url:" + url);
        SignatureTicketEntity entity = new SignatureTicketEntity();
        entity.setAppId(WxUtil.APPID);
        entity.setNoncestr(WxUtil.randomUUID(16));
        entity.setTimestamp(System.currentTimeMillis() / 1000);
        //获取全局token
        AccessTokenEntity accessToken = wxRequestService.requestAccessToken();
        if (accessToken.getErrcode() == null) {
            LOGGER.info("获取全局token得到的数据：" + gson.toJson(accessToken));
            JsapiTicketEntity jsapiTicketEntity = wxRequestService.requestJsapiTicket(accessToken.getAccess_token());
            if (jsapiTicketEntity.getErrmsg() == null) {
                LOGGER.info("获取js接口的临时票据得到的数据：" + gson.toJson(jsapiTicketEntity));
                //jsapi_ticket=&noncestr=&timestamp=&url=
                String string1 = "jsapi_ticket=" + jsapiTicketEntity.getTicket() + "&noncestr=" + entity.getNoncestr() + "&timestamp=" + entity.getTimestamp() + "&url=" + url;
                LOGGER.info("即将计算的sha1数据：" + string1);
//                entity.setSignature(MD5Util.signature(jsapiTicketEntity.getTicket(), entity.getTimestamp() + "", entity.getNoncestr(), url));
                entity.setSignature(MD5Util.sha1(string1));
                LOGGER.info("计算完成的签名：" + entity.getSignature());
                LOGGER.info("返回给前端的结果" + new Gson().toJson(entity));
                return entity;
            }
            entity.setErrcode(jsapiTicketEntity.getErrcode());
            entity.setErrmsg(jsapiTicketEntity.getErrmsg());
            return entity;
        }
        entity.setErrcode(accessToken.getErrcode());
        entity.setErrmsg(accessToken.getErrmsg());
        return entity;
    }


}
