package com.zhishi.wechat.entity;

/**
 * 用户信息 ，关注公众号后才有全部数据
 */
public class LoginEntity extends ErrorEntity {
    private String openid;       //用户的标识，对当前公众号唯一
    private String session_key;      //会话密钥
    private String unionid; //用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
