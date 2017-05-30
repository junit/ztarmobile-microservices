/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ztarmobile.account.controllers.ConstantControllerAttribute.INTROSPECTED_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
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
import org.springframework.http.HttpStatus;
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
public class AuthTokenServiceInterceptor extends HandlerInterceptorAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AuthTokenServiceInterceptor.class);

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
        int responseStatus = 0;
        ErrorResponse errorResponse = null;

        try {
            JsonElement introspectedToken = authenticationToken.handleAuthorizationRequest(request);
            log.debug("Token was found active");
            log.debug("Token payload: " + introspectedToken);
            // we continue with the next interceptor, but before doing that, we
            // save the json object.
            request.setAttribute(INTROSPECTED_TOKEN, introspectedToken);
            return true;
        } catch (AuthorizationServiceException e) {
            // there was an error while introspecting the access token, was not
            // active?
            HttpMessageErrorCode msg = e.getHttpMessageErrorCode();

            errorResponse = new ErrorResponse(msg.getMessage(), msg.getNumber());
            errorResponse.setError("Unauthorized");
            responseStatus = msg.getHttpCode();
        } catch (RuntimeException e) {
            errorResponse = new ErrorResponse(e.getMessage());
            responseStatus = Integer.parseInt(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setStatus(responseStatus);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
        return false;
    }
}
