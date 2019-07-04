package com.zhishi.wechat.controller;


import com.zhishi.wechat.service.WxRequestService;
import com.zhishi.wechat.util.MD5Util;
import com.zhishi.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class WxPortalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPortalController.class);

    @Value("${redirect.url}")
    private String redirectUrl;

    @Autowired
    private WxRequestService wxRequestService;

    @RequestMapping("/redirectUri")
    public Object getCode(String code, String state) {
        // 验证这个链接
        // https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx240ae221054c61d5&redirect_uri=http://www.zhihuiqigu.com/redirectUri&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
        LOGGER.info("----------------回调接收的code：--------------------" + code);
        //重定向到指定的地址
        return new ModelAndView(redirectUrl + "?code=" + code);
    }

    @GetMapping("")
    public void wxAuth(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) {
        LOGGER.info("wx-param:signature={},timestamp={},nonce={},echostr={}", signature, timestamp, nonce, echostr);

        try {
            if (!StringUtils.isEmpty(signature) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(nonce)) {
                String token = WxUtil.TOKEN;
                List<String> sort = new ArrayList<>();
                sort.add(token);
                sort.add(timestamp);
                sort.add(nonce);
                Collections.sort(sort);
                String auth = sort.get(0).concat(sort.get(1)).concat(sort.get(2));
                LOGGER.info("wx-:auth={}", auth);
                String sha1 = MD5Util.sha1(auth);
                LOGGER.info("wx-:sha1={}", sha1);
                if (!StringUtils.isEmpty(signature) && signature.equals(sha1)) {
                    if (!StringUtils.isEmpty(echostr)) {
                        try {
                            response.getWriter().print(echostr);
                            response.getWriter().flush();
                        } catch (IOException var11) {
                            LOGGER.info("wx-response-failure");
                        }
                        LOGGER.info("wx-success");
                    } else {
                        this.noReply(response);
                    }
                }
            }
        } catch (Exception var12) {
            this.noReply(response);
            LOGGER.error("wx-failure, e = {}", var12);
        }
    }

    private void noReply(HttpServletResponse response) {
        try {
            LOGGER.info("wx-noReply");
            response.getWriter().print("success");
            response.getWriter().flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    @PostMapping("")
    @ResponseBody
    public String processMsg(HttpServletRequest request) {
        return wxRequestService.processRequest(request);
    }
}
