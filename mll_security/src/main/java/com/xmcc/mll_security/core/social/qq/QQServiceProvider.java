package com.xmcc.mll_security.core.social.qq;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    //完成图中的第1步将用户导入认证服务器,获取授权码的地址，通过官网即可查看
    private static final String OUTHORIZE_URL="https://graph.qq.com/oauth2.0/authorize";

    //完成图中的第四步 获取access_token,通过官网即可查看
    private static final String ACCESS_TOKEN_URL="https://graph.qq.com/oauth2.0/token";

    private String appId;

    /**
     *
     */
    public QQServiceProvider(String appId,String appSecurity) {
        /**
         * appId:为系统去申请qq互联的时候由qq发放的
         * appSecurity：为密码
         * authorizUrl：导入认证服务器，获取授权码接口地址
         * accessTokenUrl：获取accessToken的地址
         */
        super(new QQOAuth2Template(appId,appSecurity,OUTHORIZE_URL,ACCESS_TOKEN_URL));
        this.appId=appId;
    }


    /**
     * @param accessToken：完成图中的前六步 这个springsecurity会帮我们完成 并将accessToken作为参数传递给我们
     * @return 只需要返回我们自定义的绑定API就可以了
     */
    @Override
    public QQ getApi(String accessToken) {
        //因为用户都有单独的appId所以为了避免线程问题，需要每次都new
        return new QQImpl(accessToken,appId);
    }
}
