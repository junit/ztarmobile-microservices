/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztarmobile.exception.ErrorResponse;
import com.ztarmobile.exception.HttpMessageErrorCode;
import com.ztarmobile.openid.connect.client.OIDCAuthenticationToken;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

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
 * @since 3.0
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
        try {
            authenticationToken.handleAuthorizationRequest(request);
            log.debug("authorization successful");
        } catch (AuthorizationServiceException e) {
            HttpMessageErrorCode msg = e.getHttpMessageErrorCode();

            ErrorResponse errorResponse = new ErrorResponse(msg.getMessage(), msg.getNumber());
            errorResponse.setError("Unauthorized");

            response.reset();
            response.setContentType(MediaType.APPLICATION_JSON);
            response.setStatus(msg.getHttpCode());

            ObjectMapper mapper = new ObjectMapper();
            response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
            return false;
        }
        return true;
    }
}
