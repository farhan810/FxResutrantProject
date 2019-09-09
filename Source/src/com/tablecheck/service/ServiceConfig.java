package com.tablecheck.service;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ServiceConfig {
//	@Bean
    public FilterRegistrationBean greetingFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("greeting");
        RequestFilter greetingFilter = new RequestFilter();
        registrationBean.setFilter(greetingFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
