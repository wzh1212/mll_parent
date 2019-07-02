package com.xmcc.mll_security.core.vaidate;

import java.time.LocalDateTime;

/**
 * 图片验证实体类
 */
public class ValidateCode {

    private String code;

    //用于判断验证码是否过期
    private LocalDateTime expireTime;

    //这个地方提供一个 int类型的过期时间，然后转成LocalDateTime类型
    public ValidateCode(String code, int expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
    public ValidateCode(String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
    }
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

}
