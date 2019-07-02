package com.xmcc.mll_security.core.social.qq;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     * 前面已经将QQConnectionFactory需要的组件都完成了，所以就是这么简单
     */
    public QQConnectionFactory(String appId,String appSecurity) {
        //我们这里直接写qq就可以了 如果有其他的服务提供商在提取出来就可以了
        super("qq", new QQServiceProvider(appId,appSecurity), new QQAdapter());
    }
}
