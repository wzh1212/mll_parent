package com.xmcc.mll_security.core.vaidate.sms.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import sun.security.util.SecurityConstants;
//老规矩还是直接去复制UsernamePasswordAuthenticationFilter。然后来进行修改
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    //因为我们这里只需要手机号
    public static final String XMCC_FORM_MOBILE_KEY = "mobile";

    private String mobileParameter = XMCC_FORM_MOBILE_KEY;
    //是否只处理post请求
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public SmsCodeAuthenticationFilter() {
        //当前过滤器处理的请求
        //即我们在短信登录时的action地址
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    //认证流程
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //判断是否是post请求
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        //获取mobile，删掉获取密码
        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }

        //去掉空格
        mobile = mobile.trim();

        //修改为我们自己的Token
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        //调用authenticationManager
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    //注意；去掉所有和密码相关的代码

    /**
     * 获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the
     * authentication request's details property.
     *
     * @param request
     *            that an authentication request is being created for
     * @param authRequest
     *            the authentication request object that should have its details
     *            set
     */
    //修改成我们自己的token
    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the username from
     * the login request.
     *
     * @param usernameParameter
     *            the parameter name. Defaults to "username".
     */
    public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }


    /**
     * Defines whether only HTTP POST requests will be allowed by this filter.
     * If set to true, and an authentication request is received which is not a
     * POST request, an exception will be raised immediately and authentication
     * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
     * will be called as if handling a failed authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }

}