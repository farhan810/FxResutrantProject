package com.tablecheck.service;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

//@Configuration  
public class MvcConfigurer {//extends WebMvcConfigurerAdapter {  
//  
//    @Override  
    public void addViewControllers(ViewControllerRegistry registry) {  
//        registry.addViewController("/error").setViewName("error.html");
    	registry.addViewController("/TableCheck").setViewName("TableCheck.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  
    }  
  
//    @Override  
    public void configurePathMatch(PathMatchConfigurer configurer) {  
//        super.configurePathMatch(configurer);  
        configurer.setUseSuffixPatternMatch(false);  
    }  
  
  
}