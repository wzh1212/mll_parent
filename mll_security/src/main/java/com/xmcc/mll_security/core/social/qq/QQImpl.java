package com.xmcc.mll_security.core.social.qq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;
import java.io.IOException;

@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ{

    // s%：占位符
    // 获得openId的url
    private static final String GET_OPID_URL="https://graph.qq.com/oauth2.0/me?access_token=%s";

    //获得用户信息的url  去掉access_token 因为在构造方法中已经交给父类了
    //oauth_consumer_key就是appId
    private static final String GET_USERINFO_URL="https://graph.qq.com/user/get_user_info?\n" +
            "oauth_consumer_key=%s&\n" +
            "openid=%s";

    //登录用户独有的标识appId 通过access_token去请求
    private String openId;

    //系统去申请qq互联功能的时候 会给一个appid
    private String appId;
    //    private String accessToken;

    @Autowired
    private ObjectMapper objectMapper;

    public QQImpl(String accessToken, String appId) {
        //TokenStrategy.ACCESS_TOKEN_PARAMETER:因为官网要求的格式是参数传递,指定access_Token传递策略为参数,
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;
        //用accessToken替换掉s%
        String url = String.format(GET_OPID_URL, accessToken);
        //查看官网:PC网站接入时，获取到用户OpenID，返回包如下：
        //callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
        String forObject = getRestTemplate().getForObject(url, String.class);
        log.info("通过请求获得字符串为，{}", forObject);
        //获取openId
        openId = StringUtils.substringBetween(forObject, "\"openid\":\"", "\"}");
        log.info("截取出的openId为:{}", openId);
//        this.accessToken = accessToken;
    }

    @Override
    public QQUserInfo getQQUserInfo() {
        //组装userInfoUrl，替换s%
        String userInfoUrl = String.format(GET_USERINFO_URL, appId, openId);
        String userInfoStr = getRestTemplate().getForObject(userInfoUrl, String.class);
        log.info("请求用户的地址为:{}",userInfoUrl);
        log.info("获得qq登录用户的字符串为:{}",userInfoStr);
        QQUserInfo qqUserInfo = null;
        try {
            qqUserInfo = objectMapper.readValue(userInfoStr, QQUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("获得qq登录用户信息为:{}",qqUserInfo);
        return qqUserInfo;
    }
}
