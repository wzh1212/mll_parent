package com.xmcc.mll_security.browser.config;

import com.xmcc.mll_security.browser.handler.BrowserAuthenticationFailureHandler;
import com.xmcc.mll_security.browser.handler.BrowserAuthenticationSuccessHandler;
import com.xmcc.mll_security.core.filter.ValidateImageCodeFilter;
import com.xmcc.mll_security.core.filter.ValidateSmsCodeFilter;
import com.xmcc.mll_security.core.properties.SecurityProperties;
import com.xmcc.mll_security.core.vaidate.sms.authentication.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityProperties properties;

    @Autowired
    BrowserAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    BrowserAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    SpringSocialConfigurer springSocialConfigurer;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 指定表单登录，所有的请求都要经过身份认证后，才能访问
         */

        ValidateImageCodeFilter imagefilter  = new ValidateImageCodeFilter();
        imagefilter .setBrowserAuthenticationFailureHandler(authenticationFailureHandler);

        ValidateSmsCodeFilter smsfilter = new ValidateSmsCodeFilter();
        smsfilter.setBrowserAuthenticationFailureHandler(authenticationFailureHandler);

        // 表单格式登录
        // 告诉 security 在执行用户名密码验证之前，先执行验证码的校验
        http.addFilterBefore(imagefilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsfilter,UsernamePasswordAuthenticationFilter.class)

                .formLogin()
                .loginPage("/authentication/require") //自定义登录页面
                .loginProcessingUrl("/authentication/form")  // 修改请求路径
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/authentication/require",
                        properties.getBrowser().getLoginPage(),
                        "/code/*").permitAll()  // 访问这个页面不需要认证
                .anyRequest()
                .authenticated()

                .and()
                .csrf().disable()  // 关闭跨站防伪验证

                //在这里添加我们自己短信验证的配置
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(springSocialConfigurer);


        // 弹窗登录
//        http.httpBasic()
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();

    }


}
