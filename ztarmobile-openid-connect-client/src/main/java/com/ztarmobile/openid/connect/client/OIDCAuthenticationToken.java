/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.client;

import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ACCESS_TOKEN_FOUND;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ACTIVE_AVAILABLE;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ACTIVE_TOKEN;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_VALID_JSON;
import static com.ztarmobile.openid.connect.client.OpenIdConnectUtil.requestTokenIntrospection;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles all the interaction with OpenId provider (Authorization Server).
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class OIDCAuthenticationToken {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(OIDCAuthenticationToken.class);

    // gets the issuer URL so that the relying party can authorize the request
    private String introspectionEndpointUri;
    // the client id of the resource server.
    private String clientId;
    // the client secret of the resource server.
    private String clientSecret;

    /**
     * Check whether the request is valid based on the access token. It should
     * be desirable to catch this exception
     * <code>AuthorizationServiceException</code> in order to know what the
     * reason is when the client was not authorized.
     * 
     * @param request
     *            The standard request, the access token should be in the header
     *            or the body of the request.
     * @see OIDCAuthenticationToken#handleAuthorizationRequest(String)
     */
    public void handleAuthorizationRequest(HttpServletRequest request) {
        this.handleAuthorizationRequest(seachForAccessToken(request));
    }

    /**
     * Check whether the request is valid based on the access token. It should
     * be desirable to catch this exception
     * <code>AuthorizationServiceException</code> in order to know what the
     * reason is when the client was not authorized.
     * 
     * @param accessToken
     *            The access token.
     * @see OIDCAuthenticationToken#handleAuthorizationRequest(HttpServletRequest)
     */
    public void handleAuthorizationRequest(String accessToken) {
        String jsonString = requestTokenIntrospection(accessToken, clientId, clientSecret, introspectionEndpointUri);
        log.debug("Token Introspection Response: " + jsonString);
        // we assume we have a valid response
        JsonElement jsonRoot = new JsonParser().parse(jsonString);
        if (!jsonRoot.isJsonObject()) {
            throw new AuthorizationServiceException(new HttpMessageErrorCodeResolver(NO_VALID_JSON, jsonRoot));
        }
        boolean active = false;
        JsonObject tokenResponse = jsonRoot.getAsJsonObject();
        if (tokenResponse.has("active")) {
            active = tokenResponse.get("active").getAsBoolean();
        } else {
            throw new AuthorizationServiceException(new HttpMessageErrorCodeResolver(NO_ACTIVE_AVAILABLE, jsonRoot));
        }
        // we check whether the token is active or not.
        if (!active) {
            throw new AuthorizationServiceException(new HttpMessageErrorCodeResolver(NO_ACTIVE_TOKEN, jsonRoot));
        }
    }

    /**
     * Search for the access token in the header first, then in the body. If
     * this token was not found, then it throws a
     * <code>AuthorizationServiceException</code>.
     * 
     * @param request
     *            The HTTPRequest.
     * @return The access token when it was found.
     */
    private String seachForAccessToken(HttpServletRequest request) {
        String token = null;
        final String TOKEN_NAME = "access_token";

        // > As an HTTP Authorization header
        // > As a form-encoded request body parameter
        // > As a URL-encoded query parameter

        // check the authorization header first
        String auth = request.getHeader(AUTHORIZATION);
        if (auth != null && !auth.trim().isEmpty() && (auth.toLowerCase().startsWith("bearer"))) {
            token = auth.substring(6).trim();
        } else {
            // not in the header, check in the form body
            token = request.getParameter(TOKEN_NAME);
            if (token == null) {
                try (BufferedReader br = request.getReader()) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains(TOKEN_NAME)) {
                            int init = line.indexOf(TOKEN_NAME);
                            int end = line.indexOf("=", init);
                            if (end != -1) {
                                token = line.substring(end + 1);
                                if (token.indexOf("&") != -1) {
                                    token = token.substring(0, token.indexOf("&"));
                                }
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    // we do nothing here... just log the error, at the end,
                    // there was not token found
                    log.debug(e.getMessage());
                }
                if (token == null) {
                    // check the parameters
                    token = request.getParameter(TOKEN_NAME);
                }
            }
        }
        if (token == null || token.trim().isEmpty()) {
            log.error(NO_ACCESS_TOKEN_FOUND.toString());
            throw new AuthorizationServiceException(NO_ACCESS_TOKEN_FOUND);
        }
        return token;
    }

    /**
     * @param introspectionEndpointUri
     *            the introspectionEndpointUri to set
     */
    public void setIntrospectionEndpointUri(String introspectionEndpointUri) {
        this.introspectionEndpointUri = introspectionEndpointUri;
    }

    /**
     * @param clientId
     *            the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @param clientSecret
     *            the clientSecret to set
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
