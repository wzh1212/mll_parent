package com.xmcc.mll_security.browser.controller;

import com.xmcc.mll_security.browser.response.ResultResponse;
import com.xmcc.mll_security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {

    private RequestCache requestCache = new HttpSessionRequestCache(); // 获取请求路径
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  // 实现页面重定向

    @Autowired
    private SecurityProperties properties;  // 获取配置文件的值


    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)  // 401 未授权
    public ResultResponse requireAuthentication(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String target = null;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null){
            // 获取引发跳转的URL
            target = savedRequest.getRedirectUrl();
            // 判断是否是页面请求，如果是，直接重定向到登录页
            if (StringUtils.endsWithIgnoreCase(target,".html")){
                // 参数3：实现自己选择的登录页面
                redirectStrategy.sendRedirect(request,response,properties.getBrowser().getLoginPage());
            }
        }

        //如果不是，返回消息给前端，交给前端处理
        return new ResultResponse("访问的服务需要身份认证，请引导用户到登录页");
    }


}
