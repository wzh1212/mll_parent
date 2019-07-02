package com.xmcc.mll_security.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {

    // 如果需要操作数据库，直接注入 dao 即可
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("用户名密码登录----------username:{}",s);
        return builderUser(s);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        log.info("社交登录-------------username:{}",s);
        return builderUser(s);
    }

    public SocialUserDetails builderUser(String userId){
        /**
         * 参数1：用户名
         * 参数2：密码
         * 参数3：是否可用 参数4：用户是否过期 参数5：密码是否过期 参数6：用户是否冻结
         * 参数7：授权相关
         */
        String password = passwordEncoder.encode("123456");
        log.info("password:{}",password);
        return new SocialUser(userId, password,true,true,true,true
                , AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        // 根据 用户名查找用户信息
//        log.info("username:{}",username);
//        // 注意当前 这 User 是 security 提供的，是 UserDetails 的子类
//        /**
//         * 参数 1：用户名
//         * 参数 2：密码
//         * 参数 3：是否可用
//         * 参数 4：用户是否过期
//         * 参数 5：密码是否过期
//         * 参数 6：用户是否冻结
//         * 参数 7：授权相关
//         */
//        return new User(username,passwordEncoder.encode("123456"),true,true,true,true,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//
////        return new User(username,new BCryptPasswordEncoder().encode("123456"),
////                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//    }
}
