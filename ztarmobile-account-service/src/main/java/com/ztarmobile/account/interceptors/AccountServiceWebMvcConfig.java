/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configures the Interceptors for this service.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Configuration
public class AccountServiceWebMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AccountServiceWebMvcConfig.class);

    /**
     * Logs all the incoming request and responses.
     */
    @Autowired
    private LoggerServiceInterceptor loggerServiceInterceptor;

    /**
     * Authorize the request against the OpenId Provider (Authorization Server).
     */
    @Autowired
    private AuthTokenServiceInterceptor authTokenServiceInterceptor;

    /**
     * Check whether the request has the right scope.
     */
    @Autowired
    private AuthScopeServiceInterceptor authScopeServiceInterceptor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("Setting up interceptors for this service...");

        // add all the interceptors here.
        registry.addInterceptor(loggerServiceInterceptor);
        registry.addInterceptor(authTokenServiceInterceptor);
        registry.addInterceptor(authScopeServiceInterceptor);
        // add more interceptors here when needed
    }
}
