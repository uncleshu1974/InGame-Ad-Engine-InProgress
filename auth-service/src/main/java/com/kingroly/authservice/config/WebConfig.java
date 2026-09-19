package com.kingroly.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Web Configuration.
 * 
 * DESIGN PATTERN: Configuration Class (Spring).
 * By implementing WebMvcConfigurer, we can customize Spring MVC's default configuration, 
 * such as registering custom interceptors that run before hitting the Controllers.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    /**
     * Registers the RateLimitInterceptor in the Spring MVC lifecycle.
     * We limit it exclusively to the "/api/auth/login" endpoint to prevent 
     * brute-force attacks against user credentials, without slowing down other APIs.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/auth/login"); // Limit only to the login route
    }
}
