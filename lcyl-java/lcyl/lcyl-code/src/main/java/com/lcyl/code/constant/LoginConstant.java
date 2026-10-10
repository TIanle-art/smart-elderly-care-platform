package com.lcyl.code.constant;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcyl.common.exception.base.BaseException;
import com.lcyl.common.utils.http.HttpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginConstant
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 14:49
 * @Version 1.0
 */
public class LoginConstant {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //public static final String LOGIN_USER = "loginUser";
    public static final String LOGIN_APPID = "";
    public static final String LOGIN_SECRET = "";
    public static final String LOGIN_GRANT_TYPE = "authorization_code";
    public static final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + LOGIN_APPID + "&secret=" + LOGIN_SECRET + "&js_code=";
    public static final String LOGIN_PHONE_URL ="https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&";

    public String getOpenIdURL(String code){
        return LOGIN_URL + code + "&grant_type=" + LOGIN_GRANT_TYPE;
    }

    public String getLoginPhoneUrl(String token){
        return LOGIN_PHONE_URL + token ;
    }

    public String getTokenUrl() {
        return TOKEN_URL + "appid=" + LOGIN_APPID + "&secret=" + LOGIN_SECRET;
    }
    public String getOpenId(String url){
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            // 解析 JSON
            JsonNode jsonNode = objectMapper.readTree(result);
            String openid = jsonNode.get("openid").asText();
            System.out.println("这是openid" + openid);
            return openid;
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccessToken(String tokenURL){

        String result = HttpUtils.sendGet(tokenURL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String access_token = jsonObject.get("access_token").toString();
        return access_token;
    }

    public String getPhone(String phoneUrl,String phoneCode){
        Map<String, Object> param = new HashMap<>();
        param.put("code", phoneCode);

        String result = HttpUtils.sendPost(phoneUrl, JSONObject.toJSONString(param));
        JSONObject jsonObject = JSONObject.parseObject(result);
        //如果code不正确，则失败
        if (jsonObject.getInteger("errcode") != 0) {
            throw new BaseException("获取手机号失败：" + jsonObject.getString("errmsg"));
        }
        return jsonObject.getJSONObject("phone_info").getString("purePhoneNumber");
    }

}
