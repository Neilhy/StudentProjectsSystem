package com.scut.cs.config;

import com.scut.cs.config.security.CustomUserService;
import com.scut.cs.web.RequestUrls;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;
import static org.hibernate.criterion.Restrictions.and;

/**
 * Created by NeilHY on 2016/8/17.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test","/test1").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage(RequestUrls.LoginUrl)
                    .failureUrl(RequestUrls.LoginUrl+"?error")
                    .permitAll()
                .and()
                .logout().permitAll();
    }
}
