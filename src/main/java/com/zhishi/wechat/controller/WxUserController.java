package com.zhishi.wechat.controller;

import com.zhishi.wechat.entity.AccessTokenEntity;
import com.zhishi.wechat.entity.SignatureTicketEntity;
import com.zhishi.wechat.entity.UserEntity;
import com.zhishi.wechat.entity.dto.AccessTokenDto;
import com.zhishi.wechat.entity.dto.UserDto;
import com.zhishi.wechat.entity.dto.UserShareDto;
import com.zhishi.wechat.response.BaseResponse;
import com.zhishi.wechat.service.WxRequestService;
import com.zhishi.wechat.service.WxUserService;
import com.zhishi.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RequestMapping("/user")
@RestController
public class WxUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxUserController.class);


    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxRequestService wxRequestService;


    //全局Token
    @CrossOrigin
    @PostMapping("/getAccessToken")
    public BaseResponse getToken(@RequestBody AccessTokenDto dto) {
        WxUtil.setAPPID(dto.getType());
        LOGGER.info("使用的APPID：" + WxUtil.APPID + "\t秘钥：" + WxUtil.APPSECRET);
        AccessTokenEntity accessTokenEntity = wxRequestService.requestAccessToken();
        return new BaseResponse(accessTokenEntity, accessTokenEntity.getErrcode(), accessTokenEntity.getErrmsg());
    }

    @CrossOrigin
    @PostMapping("/getUserInfo")
    public BaseResponse getUserInfo(@RequestBody UserDto dto) {
        WxUtil.setAPPID(dto.getType());
        LOGGER.info("使用的APPID：" + WxUtil.APPID + "\t秘钥：" + WxUtil.APPSECRET);
        UserEntity userInfo = wxUserService.getUserInfo(dto.getCode());
        return new BaseResponse(userInfo, userInfo.getErrcode(), userInfo.getErrmsg());
    }

    @CrossOrigin
    @PostMapping("/userShare")
    public BaseResponse userShare(@RequestBody UserShareDto dto) {
        WxUtil.setAPPID(dto.getType());
        LOGGER.info("使用的APPID：" + WxUtil.APPID + "\t秘钥：" + WxUtil.APPSECRET);
        SignatureTicketEntity entity = null;
        try {
            entity = wxUserService.userShare(dto.getUrl());
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("解码错误!!!");
        }
        return new BaseResponse(entity, entity.getErrcode(), entity.getErrmsg());
    }

}
