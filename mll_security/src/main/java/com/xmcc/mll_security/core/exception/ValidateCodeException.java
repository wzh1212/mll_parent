package com.xmcc.mll_security.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证异常
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long seriaVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
