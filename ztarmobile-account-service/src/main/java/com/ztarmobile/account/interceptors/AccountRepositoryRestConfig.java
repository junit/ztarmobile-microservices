/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

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
     * Authorize the request against the OpenId Provider (Authorization Server).
     */
    @Autowired
    private AuthorizationServiceInterceptor authorizationServiceInterceptor;

    /**
     * @return The map interceptor.
     * @see com.ztarmobile.account.interceptors.AccountServiceWebMvcConfig#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Bean
    public MappedInterceptor mappedInterceptor() {
        log.debug("Setting up interceptors for the rest repositories...");
        String[] includePatterns = new String[] { basePath + "/" + USER_ACCOUNT_MAPPING + "/**" };

        // add interceptors into this one, in the same order as
        // AccountServiceWebMvcConfig.
        RepositoryInterceptor repositoryInterceptor = new RepositoryInterceptor();
        // the order of the interceptors MUST be the same as class:
        // AccountServiceWebMvcConfig
        repositoryInterceptor.addInterceptor(loggerServiceInterceptor);
        repositoryInterceptor.addInterceptor(authorizationServiceInterceptor);
        return new MappedInterceptor(includePatterns, repositoryInterceptor);
    }
}
