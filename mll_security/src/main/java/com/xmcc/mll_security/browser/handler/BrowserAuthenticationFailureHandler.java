package com.xmcc.mll_security.browser.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmcc.mll_security.browser.response.LoginType;
import com.xmcc.mll_security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class BrowserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("登录失败");
        httpServletResponse.setStatus(500);

        if (securityProperties.getBrowser().getLoginType().equals(LoginType.JSON)){
            // 我们可以在这里实现自己的业务逻辑
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(e));
        }else {
            super.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
        }


    }
}
