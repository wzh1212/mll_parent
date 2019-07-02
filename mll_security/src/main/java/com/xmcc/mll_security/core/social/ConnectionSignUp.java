package com.xmcc.mll_security.core.social;

import org.springframework.stereotype.Component;
import org.springframework.social.connect.Connection;


@Component
public class ConnectionSignUp implements org.springframework.social.connect.ConnectionSignUp {
    @Override
    public String execute(Connection<?> connection) {
        return connection.getDisplayName() ;
    }
}
