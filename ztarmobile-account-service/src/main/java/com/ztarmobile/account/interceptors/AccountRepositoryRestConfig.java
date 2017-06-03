/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import static com.ztarmobile.account.repository.CustomRepositoryPath.RESOURCE_SET_SCOPE_MAPPING;
import static com.ztarmobile.account.repository.CustomRepositoryPath.USER_ACCOUNT_MAPPING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * Configures the Interceptors for the repositories rest services.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Configuration
public class AccountRepositoryRestConfig {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AccountRepositoryRestConfig.class);
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
     * Determines whether the security will be applicable or not.
     */
    @Autowired
    private AuthInitialServiceInterceptor authInitialServiceInterceptor;

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
     * @return The map interceptor.
     * @see com.ztarmobile.account.interceptors.AccountServiceWebMvcConfig#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Bean
    public MappedInterceptor mappedInterceptor() {
        log.debug("Setting up interceptors for the rest repositories...");

        final String SLASH = "/";
        final String SLASH_ALL = "/**";

        String[] includePatterns = new String[2];
        includePatterns[0] = basePath + SLASH + USER_ACCOUNT_MAPPING + SLASH_ALL;
        includePatterns[1] = basePath + SLASH + RESOURCE_SET_SCOPE_MAPPING + SLASH_ALL;

        // add interceptors into this one, in the same order as
        // AccountServiceWebMvcConfig.
        RepositoryInterceptor repositoryInterceptor = new RepositoryInterceptor();
        // the order of the interceptors MUST be the same as class:
        // AccountServiceWebMvcConfig
        repositoryInterceptor.addInterceptor(loggerServiceInterceptor);
        repositoryInterceptor.addInterceptor(authInitialServiceInterceptor);
        repositoryInterceptor.addInterceptor(authTokenServiceInterceptor);
        repositoryInterceptor.addInterceptor(authScopeServiceInterceptor);
        return new MappedInterceptor(includePatterns, repositoryInterceptor);
    }
}
