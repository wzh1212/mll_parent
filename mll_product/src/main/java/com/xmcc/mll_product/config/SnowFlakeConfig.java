package com.xmcc.mll_product.config;

import com.xmcc.mll_common.util.SnowFlakeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfig {
    @Bean("productSnowFlake")
    public SnowFlakeUtils snowFlakeUtils(){
        //第9个机房的第20台机器
        return new SnowFlakeUtils(9,20);
    }

}
