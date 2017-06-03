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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.ztarmobile.exception.ErrorResponse;
import com.ztarmobile.exception.HttpMessageErrorCode;
import com.ztarmobile.openid.connect.client.OIDCAuthenticationToken;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
     * Default error description.
     */
    private static final String UNAUTHORIZED = "Unauthorized";

    @Value("${account.openid.context}")
    private String context;

    @Value("${account.openid.keycloak.realm}")
    private String keycloakRealm;

    @Value("${account.openid.keycloak.master-realm}")
    private String keycloakMasterRealm;

    @Value("${account.openid.keycloak.username}")
    private String keycloakUsername;

    @Value("${account.openid.keycloak.password}")
    private String keycloakPassword;

    @Value("${account.openid.keycloak.client-id}")
    private String keycloakClientId;

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
        log.debug("Validating Token Request...");

        // checks whether this interceptor should be executed.
        Boolean ignoreSecurity = (Boolean) request.getAttribute(IGNORE_SECURITY);
        if (ignoreSecurity) {
            log.warn("Token Access validation skipped");
            return true;
        }

        Boolean basicAuth = (Boolean) request.getAttribute(BASIC_AUTHENTICATION);
        if (basicAuth) {
            // it's basic authentication.
            return basicAuthRequest(request, response);
        } else {
            // it's normal auth 2.0
            return processOAuthRequest(request, response);
        }
    }

    /**
     * Process a Basic Authentication.
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @return flag to continue the next interceptor.
     * @throws Exception
     *             If there's an exception during the transaction.
     */
    private boolean basicAuthRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Keycloak kc = null;
        kc = Keycloak.getInstance(context, keycloakMasterRealm, keycloakUsername, keycloakPassword, keycloakClientId);
        ClientsResource clientsResource = kc.realm(keycloakRealm).clients();

        Map<String, String> allClients = new HashMap<>();
        ClientResource cr = null;
        CredentialRepresentation credentialRep = null;
        String clientId = null;
        for (ClientRepresentation clientRepresentation : clientsResource.findAll()) {
            // we exclude some keycloak clients.
            clientId = clientRepresentation.getClientId();
            if (clientId.equals(keycloakClientId) || clientId.equals("security-admin-console")
                    || clientId.equals("account") || clientId.equals("realm-management") || clientId.equals("broker")) {
                continue;
            }

            cr = clientsResource.get(clientRepresentation.getId());
            credentialRep = cr.getSecret();

            allClients.put(clientRepresentation.getClientId(), credentialRep.getValue());
        }

        int responseStatus = 0;
        ErrorResponse errorResponse = null;

        try {
            authenticationToken.handleBasicAuthRequest(request, allClients);
            log.debug("Basic Authentication ok");
            return true;
        } catch (AuthorizationServiceException e) {
            HttpMessageErrorCode msg = e.getHttpMessageErrorCode();

            errorResponse = new ErrorResponse(msg.getEvaluatedMessage(), msg.getNumber());
            errorResponse.setError(UNAUTHORIZED);
            responseStatus = msg.getHttpCode();
        }
        return createJSONResponse(response, errorResponse, responseStatus);
    }

    /**
     * Process a normal OAuth 2.0 request.
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @return flag to continue the next interceptor.
     * @throws Exception
     *             If there's an exception during the transaction.
     */
    private boolean processOAuthRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

            errorResponse = new ErrorResponse(msg.getEvaluatedMessage(), msg.getNumber());
            errorResponse.setError(UNAUTHORIZED);
            responseStatus = msg.getHttpCode();
        } catch (RuntimeException e) {
            errorResponse = new ErrorResponse(e.getMessage());
            responseStatus = Integer.parseInt(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return createJSONResponse(response, errorResponse, responseStatus);
    }

    /**
     * Construct a JSON response based on the error response object.
     * 
     * @param response
     *            The HTTP response.
     * @param errorResponse
     *            The error response.
     * @param The
     *            HTTP status.
     * @return a JSON response.
     * @throws IOException
     *             Exception while trying to write the response.
     */
    private boolean createJSONResponse(HttpServletResponse response, ErrorResponse errorResponse, int httpStatus)
            throws IOException {
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setStatus(httpStatus);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
        return false;
    }
}
