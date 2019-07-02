package com.xmcc.mll_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MllSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MllSecurityApplication.class, args);
    }

}
