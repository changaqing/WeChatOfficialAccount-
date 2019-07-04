package com.zhishi.wechat.service.impl;

import com.zhishi.wechat.entity.UserEntity;
import com.zhishi.wechat.service.AppRequestService;
import com.zhishi.wechat.service.WxRequestService;
import com.zhishi.wechat.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppRequestServiceImpl implements AppRequestService {

    @Autowired
    private WxRequestService wxRequestService;

    @Override
    public UserEntity requestUserInfo(Integer type, String openId) {
        WxUtil.setAPPID(type);
        return wxRequestService.requestUserInfo(wxRequestService.requestAccessToken().getAccess_token(), openId);
    }
}
