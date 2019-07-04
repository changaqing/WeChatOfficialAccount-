package com.zhishi.wechat.util;

import com.zhishi.wechat.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class DBCacheUtil {

    public static final String KEY = "WECHAT";

    @Autowired
    private StringRedisTemplate cache;
    @Resource(name = "redisTemplate")
    public HashOperations<String, String, UserEntity> userInfoCache;

    /**
     * 获取用户sid
     *
     * @param key
     * @return
     */
    public String getCache(String key) {
        return cache.opsForValue().get(key);
    }

    /**
     * 设置缓存+时效
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void setCache(String key, String value, int seconds) {
        cache.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public UserEntity addUserInfo(UserEntity user) {
        UserEntity userEntity = null;
        //关注了公众号
        if (user.getSubscribe() == 1) {
            //检查是否存在
            userEntity = userInfoCache.get(KEY, user.getOpenid());
            if (userEntity == null) {
                userInfoCache.put(KEY, user.getOpenid(), userEntity);
                return userEntity;
            }
            return user;
        }
        return null;
    }

    public static String getAccessTokenKey(String appId) {
        return "AccessToken" + appId;
    }

    public static String getJsapiTicketKey(String appId) {
        return "JsapiTicket" + appId;
    }
}
