package com.kingroly.campaignservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to register custom Servlet Filters.
 * 
 * DESIGN PATTERN: Intercepting Filter / Filter Chain.
 * Since this microservice doesn't use Spring Security, we must manually register 
 * our custom JwtValidatorFilter into the standard Java Servlet container filter chain.
 */
@Configuration
public class FilterConfig {

    @Autowired
    private JwtValidatorFilter jwtValidatorFilter;

    /**
     * Registers the JwtValidatorFilter and maps it to specific URL patterns.
     */
    @Bean
    public FilterRegistrationBean<JwtValidatorFilter> jwtFilter() {
        FilterRegistrationBean<JwtValidatorFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtValidatorFilter);
        
        // Apply this filter ONLY to the APIs under /api/campaigns/*
        registrationBean.addUrlPatterns("/api/campaigns/*");
        
        // Execute it first (Order 1) to protect the APIs immediately before reaching the controllers.
        registrationBean.setOrder(1); 
        return registrationBean;
    }
}
