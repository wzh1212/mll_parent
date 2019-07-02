package com.xmcc.mll_security.browser.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmcc.mll_security.browser.response.LoginType;
import com.xmcc.mll_security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
//SavedRequestAwareAuthenticationSuccessHandler security默认的处理器
public class BrowserAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;


    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication   用户信息，认证信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");

        //如果是 json 按我们自己的方式处理
        if (securityProperties.getBrowser().getLoginType().equals(LoginType.JSON)){
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else {
            //调用父类方法实现跳转
            super.onAuthenticationSuccess(httpServletRequest,httpServletResponse,authentication);
        }

    }
}
