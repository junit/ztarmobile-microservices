/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ztarmobile.account.controllers.ConstantControllerAttribute.BASIC_AUTHENTICATION;
import static com.ztarmobile.account.controllers.ConstantControllerAttribute.IGNORE_SECURITY;
import static com.ztarmobile.account.controllers.ConstantControllerAttribute.INTROSPECTED_TOKEN;
import static com.ztarmobile.account.controllers.ConstantControllerAttribute.REQUESTED_RESOURCE;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.ztarmobile.openid.connect.ResourceSetScopeService;
import com.ztarmobile.exception.ErrorResponse;
import com.ztarmobile.exception.HttpMessageErrorCode;
import com.ztarmobile.openid.connect.client.OIDCAuthenticationToken;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;
import com.ztarmobile.util.ProtectedResource;

import java.util.List;
import java.util.Set;

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
 * Spring bean to check whether the client has the right scope to access the
 * protected resource.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class AuthScopeServiceInterceptor extends HandlerInterceptorAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AuthScopeServiceInterceptor.class);

    /**
     * Utility to handle the interaction with the server provider.
     */
    @Autowired
    private OIDCAuthenticationToken authenticationToken;

    /**
     * Handles the authorization of the scopes.
     */
    @Autowired
    private ResourceSetScopeService resourceSetScopeService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("Validating Scopes Accesses...");

        // checks whether this interceptor should be executed.
        Boolean ignoreSecurity = (Boolean) request.getAttribute(IGNORE_SECURITY);
        Boolean basicAuth = (Boolean) request.getAttribute(BASIC_AUTHENTICATION);

        if (ignoreSecurity || basicAuth) {
            log.warn("Scope Access validation skipped");
            return true;
        }

        int responseStatus = 0;
        ErrorResponse errorResponse = null;
        ProtectedResource protectedResource = null;

        try {
            // gets the introspected token.
            JsonElement introspectedToken = (JsonElement) request.getAttribute(INTROSPECTED_TOKEN);
            Set<String> scopes = authenticationToken.handleScopeRequest(introspectedToken);

            // gets the requested resource.
            protectedResource = (ProtectedResource) request.getAttribute(REQUESTED_RESOURCE);
            resourceSetScopeService.validateScope(scopes, protectedResource);

            log.debug("The scope was successful!!!");
            return true;
        } catch (AuthorizationServiceException e) {
            HttpMessageErrorCode msg = e.getHttpMessageErrorCode();

            errorResponse = new ErrorResponse(msg.getEvaluatedMessage(), msg.getNumber());
            errorResponse.setError("forbidden");
            responseStatus = msg.getHttpCode();
        } catch (RuntimeException e) {
            errorResponse = new ErrorResponse(e.getMessage());
            responseStatus = Integer.parseInt(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setHeader(WWW_AUTHENTICATE, createHeaderResponse(protectedResource, request));
        response.setStatus(responseStatus);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
        return false;
    }

    private String createHeaderResponse(ProtectedResource protectedResource, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (protectedResource != null) {
            sb.append("Bearer realm=");
            sb.append(request.getRemoteHost());
            sb.append(":");
            sb.append(request.getServerPort());
            sb.append(", ");
            sb.append("error=\"insufficient_scope\"");
            sb.append(", ");
            sb.append("scope=");

            List<String> scopesRequired = resourceSetScopeService.getScopesByResource(protectedResource);
            if (scopesRequired.isEmpty()) {
                sb.append("\"No scopes found\"");
            } else {
                sb.append("\"" + scopesRequired + "\"");
            }
        } else {
            sb.append("Not available");
        }
        return sb.toString();
    }
}
