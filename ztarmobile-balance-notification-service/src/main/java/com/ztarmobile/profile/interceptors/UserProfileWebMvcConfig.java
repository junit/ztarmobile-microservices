/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configures the Interceptors for this service.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Configuration
public class UserProfileWebMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserProfileWebMvcConfig.class);

    /**
     * Based path.
     */
    @Value("${spring.data.rest.base-path}")
    private String basePath;

    /**
     * Logs all the incoming request and responses.
     */
    @Autowired
    private LoggerServiceInterceptor loggerServiceInterceptor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("Setting up interceptors for this service...");
        String[] includePatterns = new String[] { basePath + "/**" };

        // add all the interceptors here.
        registry.addInterceptor(loggerServiceInterceptor).addPathPatterns(includePatterns);
        // add more interceptors here when needed
    }
}
