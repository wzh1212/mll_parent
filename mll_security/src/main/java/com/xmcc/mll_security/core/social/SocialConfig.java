package com.xmcc.mll_security.core.social;

import com.xmcc.mll_security.core.social.qq.QQConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableSocial
@Slf4j
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        //第三个参数 是加密的 我们不做任何操作
        JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        //如果需要给表加前缀
        // jdbcUsersConnectionRepository.setTablePrefix("xxxx");

        if (connectionSignUp != null){
            jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }

        log.info("success。。。");
        return jdbcUsersConnectionRepository;
    }

    @Bean
    public SpringSocialConfigurer springSocialConfigurer(){
        return new SpringSocialConfigurer();
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment){
        connectionFactoryConfigurer.addConnectionFactory(createConnectionFactory());
    }

    public ConnectionFactory<?> createConnectionFactory() {
        return new
                //参数1：appid 参数2：appSecurity
                QQConnectionFactory("101548802","6ac7e06dc0b0fb04da42e0c36c14ae29");
    }
    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
}

