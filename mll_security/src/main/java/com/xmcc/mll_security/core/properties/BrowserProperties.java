package com.xmcc.mll_security.core.properties;

import com.xmcc.mll_security.browser.response.LoginType;
import lombok.Data;

@Data
public class BrowserProperties {

    // 设置默认登录页面
    private String loginPage = "/xmcc_login.html";

    // 默认返回 json
    private LoginType loginType = LoginType.JSON;

}
