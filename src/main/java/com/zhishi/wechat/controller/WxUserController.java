package com.zhishi.wechat.controller;

import com.zhishi.wechat.entity.AccessTokenEntity;
import com.zhishi.wechat.entity.LoginEntity;
import com.zhishi.wechat.entity.SignatureTicketEntity;
import com.zhishi.wechat.entity.UserEntity;
import com.zhishi.wechat.entity.dto.AccessTokenDto;
import com.zhishi.wechat.entity.dto.LoginDto;
import com.zhishi.wechat.entity.dto.UserDto;
import com.zhishi.wechat.entity.dto.UserShareDto;
import com.zhishi.wechat.response.BaseResponse;
import com.zhishi.wechat.service.WxRequestService;
import com.zhishi.wechat.service.WxUserService;
import com.zhishi.wechat.util.RestTemplateUtil;
import com.zhishi.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RequestMapping("/user")
@RestController
public class WxUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxUserController.class);


    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxRequestService wxRequestService;

    /**
     * 登录
     *
     * @param dto 前端发过来的code
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/login")
    public BaseResponse login(LoginDto dto, HttpServletRequest request) {
        WxUtil.setSR_APPID(dto.getType());
        LOGGER.info("小程序id："+WxUtil.SR_APPID+"\t--\tip发出地址："+request.getRemoteAddr());
        // 根据小程序穿过来的code想这个url发送请求
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WxUtil.SR_APPID + "&secret=" + WxUtil.SR_SECRET + "&js_code=" + dto.getCode() + "&grant_type=authorization_code";
        // 发送请求，返回Json字符串
        LoginEntity loginEntity = RestTemplateUtil.getInstance().getForObject(url, LoginEntity.class);
        // -1	系统繁忙，此时请开发者稍候再试
        //0	请求成功
        //40029	code 无效
        //45011	频率限制，每个用户每分钟100次

        return new BaseResponse(loginEntity, loginEntity.getErrcode() == null ? "0" : loginEntity.getErrcode() + "", loginEntity.getErrmsg() == null ? "成功" : loginEntity.getErrmsg());
    }

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
