package com.xmcc.mll_security.core.filter;

import com.xmcc.mll_security.core.controller.ValidateCodeController;
import com.xmcc.mll_security.core.exception.ValidateCodeException;
import com.xmcc.mll_security.core.vaidate.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片验证过滤
 */
public class ValidateImageCodeFilter extends OncePerRequestFilter implements InitializingBean{

    // 处理 session 的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();//处理session的工具类

    private AuthenticationFailureHandler browserAuthenticationFailureHandler;
    //通过set方法，注入失败处理器
    public void setBrowserAuthenticationFailureHandler(AuthenticationFailureHandler browserAuthenticationFailureHandler) {
        this.browserAuthenticationFailureHandler = browserAuthenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/authentication/form", httpServletRequest.getRequestURI())
                && StringUtils.endsWithIgnoreCase(httpServletRequest.getMethod(), "post")){

            try {
                validate(new ServletWebRequest(httpServletRequest));
            }catch (ValidateCodeException e){
                //调用失败处理器处理异常
                browserAuthenticationFailureHandler.onAuthenticationFailure(
                        httpServletRequest, httpServletResponse, e);
                //失败以后直接return
                return;
            }
        }
        //如果不是登录请求，直接放行，不做任何验证
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    //校验验证码
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        //从session中获取imageCode
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,ValidateCodeController.SESSION_KEY);
        //从请求中获取imageCode的值
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");


        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空（图片）");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException( "验证码不存在（图片）");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException( "验证码已过期（图片）");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配（图片）");
        }
        sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
    }
}
