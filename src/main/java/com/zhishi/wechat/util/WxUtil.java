package com.zhishi.wechat.util;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

public class WxUtil {


    private static final String[] APPIDS = {
            "",
            "",
            ""};
    private static final String[] APPSECRETS = {
            "",
            "",
            ""};

    //小程序
    private static final String[] SR_APPIDS = {
            "",
            ""};
    private static final String[] SR_SECRETS = {
            "",
            ""};
    public static String SR_APPID = SR_APPIDS[0];
    public static String SR_SECRET = SR_SECRETS[0];

    public static String APPID = APPIDS[1];//默认是天津知柿
    public static String APPSECRET = APPSECRETS[1];
    public static final String TOKEN = "";
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
    public static final String REQ_MESSAGE_TYPE_LINK = "link";
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";
    public static final String EVENT_TYPE_SUBSCRIBE = "SUBSCRIBE";
    public static final String EVENT_TYPE_UNSUBSCRIBE = "UNSUBSCRIBE";
    public static final String EVENT_TYPE_SCAN = "SCAN";
    public static final String EVENT_TYPE_LOCATION = "LOCATION";
    public static final String EVENT_TYPE_CLICK = "CLICK";
    public static final String FromUserName = "FromUserName";
    public static final String ToUserName = "ToUserName";
    public static final String MsgType = "MsgType";
    public static final String Content = "Content";
    public static final String Event = "Event";

    //设置使用哪一个公众号的信息
    public static void setAPPID(Integer type) {
        if (type != null) {
            APPID = APPIDS[type];
            APPSECRET = APPSECRETS[type];
        }
    }
    //设置使用哪一个小程序的信息
    public static void setSR_APPID(Integer type) {
        if (type != null) {
            SR_APPID = SR_APPIDS[type];
            SR_SECRET = SR_SECRETS[type];
        }
    }
    public static String randomUUID(int endIndex) {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.remove(uuid, "-").substring(0, 16);
    }

    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";

        for (int i = 0; i < byteArray.length; ++i) {
            strDigest = strDigest + byteToHexStr(byteArray[i]);
        }

        return strDigest;
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[]{Digit[mByte >>> 4 & 15], Digit[mByte & 15]};
        String s = new String(tempArr);
        return s;
    }

    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        Iterator var7 = elementList.iterator();

        while (var7.hasNext()) {
            Element e = (Element) var7.next();
            map.put(e.getName(), e.getText());
        }
        inputStream.close();
        return map;
    }

    public static String mapToXML(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        mapToXML2(map, sb);
        sb.append("</xml>");

        try {
            return sb.toString();
        } catch (Exception var3) {
            return null;
        }
    }

    private static void mapToXML2(Map map, StringBuffer sb) {
        Set set = map.keySet();
        Iterator it = set.iterator();

        while (true) {
            while (it.hasNext()) {
                String key = (String) it.next();
                Object value = map.get(key);
                if (null == value) {
                    value = "";
                }

                if (value.getClass().getName().equals("java.util.ArrayList")) {
                    ArrayList list = (ArrayList) map.get(key);
                    sb.append("<" + key + ">");

                    for (int i = 0; i < list.size(); ++i) {
                        HashMap hm = (HashMap) list.get(i);
                        mapToXML2(hm, sb);
                    }

                    sb.append("</" + key + ">");
                } else if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXML2((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + "><![CDATA[" + value + "]]></" + key + ">");
                }
            }

            return;
        }
    }

    public static String sendTextMsg(Map<String, String> requestMap, String content) {
        Map<String, Object> map = new HashMap();
        map.put("ToUserName", requestMap.get("FromUserName"));
        map.put("FromUserName", requestMap.get("ToUserName"));
        map.put("MsgType", "text");
        map.put("CreateTime", (new Date()).getTime());
        map.put("Content", content);
        return mapToXML(map);
    }
}
