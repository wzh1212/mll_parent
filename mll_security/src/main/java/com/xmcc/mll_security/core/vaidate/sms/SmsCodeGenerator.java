package com.xmcc.mll_security.core.vaidate.sms;

import com.xmcc.mll_security.core.vaidate.ValidateCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证
 */
@Component
public class SmsCodeGenerator {
    /**
     * 当然这些地方的短信验证码长度和过期时间，我们也可以通过编写配置文件来完成，就像最开始我们，通过配置文件完成登录页那样
     * 请自己参照之前的代码完成，我这里就直接写死了
     */

    public static ValidateCode generate(ServletWebRequest request) {
        //生成6位随机数
        String code = RandomStringUtils.randomNumeric(6);
        //生成短信验证码
        return new ValidateCode(code,60);
    }
}
