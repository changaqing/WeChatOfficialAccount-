package com.zhishi.wechat.service;

import com.zhishi.wechat.entity.UserEntity;

public interface AppRequestService {
    /**
     * 获取用户信息提供给app
     *
     * @param type
     * @param openId
     * @return
     */
    UserEntity requestUserInfo(Integer type, String openId);

}
