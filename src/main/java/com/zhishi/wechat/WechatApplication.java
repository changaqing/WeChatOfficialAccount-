package com.zhishi.wechat;

import com.zhishi.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WechatApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
        LOGGER.info("系统启动...使用的APPID：" + WxUtil.APPID + "\t秘钥：" + WxUtil.APPSECRET);
    }

}
