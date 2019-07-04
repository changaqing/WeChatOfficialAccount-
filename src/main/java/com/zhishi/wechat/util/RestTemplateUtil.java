package com.zhishi.wechat.util;

import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class RestTemplateUtil {
    public static RestTemplate getInstance() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);//连接超时时间
        requestFactory.setReadTimeout(1000);//读取超时时间
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}

/**
 * 不支持[text/plain;charset=UTF-8]类型，
 * 需要继承 MappingJackson2HttpMessageConverter
 * 并在构造过程中设置其支持的 MediaType 类型
 */
class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public WxMappingJackson2HttpMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);//微信返回的数据类型就是这个 气人。。。
        mediaTypes.add(MediaType.TEXT_HTML);
        super.setSupportedMediaTypes(mediaTypes);
    }
}