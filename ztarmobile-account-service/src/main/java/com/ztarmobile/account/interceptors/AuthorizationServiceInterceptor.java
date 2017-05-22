/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Spring bean to authorize incoming requests against the OpenId provider
 * (Authorization Server).
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 2.0
 */
@Component
public class AuthorizationServiceInterceptor extends HandlerInterceptorAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AuthorizationServiceInterceptor.class);
    
    /**
     * Utility to handle the interaction with the server provider.
     */
    @Autowired
    private OIDCAuthenticationToken authenticationToken;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("Validating Request...");
        return true;
    }
}
