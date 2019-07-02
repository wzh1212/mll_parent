package com.xmcc.mll_security.core.config;

import com.xmcc.mll_security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
// 让 SecurityProperties 类的作用生效
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

}
