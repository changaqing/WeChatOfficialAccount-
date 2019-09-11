package com.zhishi.wechat.service.impl;

import com.zhishi.wechat.entity.AccessTokenEntity;
import com.zhishi.wechat.entity.JsapiTicketEntity;
import com.zhishi.wechat.entity.UserEntity;
import com.zhishi.wechat.entity.WebTokenEntity;
import com.zhishi.wechat.service.WxRequestService;
import com.zhishi.wechat.util.DBCacheUtil;
import com.zhishi.wechat.util.RestTemplateUtil;
import com.zhishi.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxRequestServiceImpl implements WxRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxRequestServiceImpl.class);

    @Autowired
    private DBCacheUtil dbCacheUtil;

    @Override
    public AccessTokenEntity requestAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={secret}";
        Map<String, String> params = new HashMap<>();
        params.put("appId", WxUtil.APPID);
        params.put("secret", WxUtil.APPSECRET);
        String cache = dbCacheUtil.getCache(DBCacheUtil.getAccessTokenKey(WxUtil.APPID));
        AccessTokenEntity forObject = new AccessTokenEntity();
        //如果缓存里没有就存进去一份
        if (cache == null) {
            forObject = RestTemplateUtil.getInstance().getForObject(url, AccessTokenEntity.class, params);
            if (forObject.getErrcode() == null) {
                dbCacheUtil.setCache(DBCacheUtil.getAccessTokenKey(WxUtil.APPID), forObject.getAccess_token(), forObject.getExpires_in() - 500);
                LOGGER.info("成功缓存全局AccessToken\tkey：" + DBCacheUtil.getAccessTokenKey(WxUtil.APPID) + "\tvalue：" + forObject.getAccess_token());
                return forObject;
            }
        }
        forObject.setAccess_token(cache);
        return forObject;
    }

    @Override
    public WebTokenEntity requestWebTokenInfo(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appId}&secret={secret}&code={code}&grant_type=authorization_code";
        Map<String, String> params = new HashMap<>();
        params.put("appId", WxUtil.APPID);
        params.put("secret", WxUtil.APPSECRET);
        params.put("code", code);
        return RestTemplateUtil.getInstance().getForObject(url, WebTokenEntity.class, params);
    }

    @Override
    public UserEntity requestUserInfo(String token, String openId) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={token}&openid={openId}&lang=zh_CN";
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("openId", openId);
        return RestTemplateUtil.getInstance().getForObject(url, UserEntity.class, params);
    }

    //暂时用不到
    @Override
    public String processRequest(HttpServletRequest request) {
        String respXml = null;
        try {
            Map<String, String> requestMap = WxUtil.parseXml(request);
            LOGGER.info("解析的请求：" + requestMap);
            String msgType = requestMap.get("MsgType");
            String mes = null;
            if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_TEXT)) {
                mes = requestMap.get("Content");
                if (mes != null && mes.length() < 2) {

                } else if ("我的信息".equals(mes)) {
                    LOGGER.info(requestMap.get("FromUserName"));
                }
            } else {
                String respContent;
                if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                    respContent = "您发送的是图片消息！";
                    respXml = WxUtil.sendTextMsg(requestMap, respContent);
                } else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_VOICE)) {
                    respContent = "您发送的是语音消息！";
                    respXml = WxUtil.sendTextMsg(requestMap, respContent);
                } else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_VIDEO)) {
                    respContent = "您发送的是视频消息！";
                    respXml = WxUtil.sendTextMsg(requestMap, respContent);
                } else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                    respContent = "您发送的是地理位置消息！";
                    respXml = WxUtil.sendTextMsg(requestMap, respContent);
                } else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_LINK)) {
                    respContent = "您发送的是链接消息！";
                    respXml = WxUtil.sendTextMsg(requestMap, respContent);
                } else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_EVENT)) {
                    String eventType = requestMap.get("Event");
                    if (eventType.equals(WxUtil.EVENT_TYPE_SUBSCRIBE)) {
                        respContent = "谢谢您的关注！";
                        respXml = WxUtil.sendTextMsg(requestMap, respContent);
                    } else if (!eventType.equals(WxUtil.EVENT_TYPE_UNSUBSCRIBE) && !eventType.equals(WxUtil.EVENT_TYPE_SCAN) && !eventType.equals(WxUtil.EVENT_TYPE_LOCATION) && eventType.equals(WxUtil.EVENT_TYPE_CLICK)) {
                        LOGGER.info("我点击了...");
                    }
                }
            }

            mes = mes == null ? "发一个字试试。。。" : mes;
            if (respXml == null) {
                respXml = WxUtil.sendTextMsg(requestMap, mes);
            }

            LOGGER.info(respXml);
            return respXml;
        } catch (Exception var9) {
            var9.printStackTrace();
            return "";
        }
    }

    @Override
    public JsapiTicketEntity requestJsapiTicket(String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={access_token}&type=jsapi";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        String cache = dbCacheUtil.getCache(DBCacheUtil.getJsapiTicketKey(WxUtil.APPID));
        JsapiTicketEntity forObject = new JsapiTicketEntity();
        //如果缓存里没有就存进去一份
        if (cache == null) {
            forObject = RestTemplateUtil.getInstance().getForObject(url, JsapiTicketEntity.class, params);
            //微信返回的成功信息还都不一样。。。 这个正确返回的是ok 其它的是null为成功。。。
            if (forObject.getErrmsg().equals("ok")) {
                dbCacheUtil.setCache(DBCacheUtil.getJsapiTicketKey(WxUtil.APPID), forObject.getTicket(), forObject.getExpires_in() - 500);
                LOGGER.info("成功缓存全局JsapiTicket：" + DBCacheUtil.getJsapiTicketKey(WxUtil.APPID) + "\tvalue：" + forObject.getTicket());
                forObject.setErrmsg(null);
                return forObject;
            }
        }
        forObject.setTicket(cache);
        return forObject;
    }

}
