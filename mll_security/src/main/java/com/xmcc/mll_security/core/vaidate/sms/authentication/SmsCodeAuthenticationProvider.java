package com.xmcc.mll_security.core.vaidate.sms.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    //声明，并且生成getset方法
    private UserDetailsService userDetailsService;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.authentication.AuthenticationProvider#
     * authenticate(org.springframework.security.core.Authentication)
     *
     * 实现身份认证的逻辑：
     *        我们需要用到UserDetailsService 来获取用户信息，然后封装成一个已认证的用户信息返回
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取我们自己的tonken
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        //从token中获取信息
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        //判断是否为空
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        //重新封装成已认证的token 传入用户信息和用户权限
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        //返回
        return authenticationResult;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.authentication.AuthenticationProvider#
     * supports(java.lang.Class)
     *
     *
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //判断传入的tonken是否是短信验证的token
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}
