package com.xmcc.mll_order.config;

import com.xmcc.mll_common.util.SnowFlakeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfig {

    @Bean("orderSnowFlake")
    public SnowFlakeUtils snowFlakeUtils(){
        //第6个机房的第11台机器
        return new SnowFlakeUtils(6,11);
    }

}

