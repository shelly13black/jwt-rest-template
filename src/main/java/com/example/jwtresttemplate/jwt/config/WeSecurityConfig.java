package com.example.jwtresttemplate.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WeSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> filterFilterRegistrationBean(){
        FilterRegistrationBean<JwtRequestFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(jwtRequestFilter);
        filterFilterRegistrationBean.addUrlPatterns("/jwt-rest-template/*");
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE -1);
        return filterFilterRegistrationBean;
    }
}
