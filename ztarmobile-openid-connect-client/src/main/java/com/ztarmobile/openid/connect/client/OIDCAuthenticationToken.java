/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.client;

import static com.ztarmobile.exception.AuthorizationMessageErrorCode.INVALID_BASIC;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.INVALID_BASIC_CRED;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ACCESS_TOKEN_FOUND;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ACTIVE_AVAILABLE;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ACTIVE_TOKEN;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_ROLE_AVAILABLE;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_SCOPE_AVAILABLE;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_SCOPE_FOUND;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.NO_VALID_JSON;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.UNAUTHORIZED_BASIC;
import static com.ztarmobile.openid.connect.client.OpenIdConnectUtil.requestTokenIntrospection;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

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

    /**
     * Common JSON properties.
     */
    private static final String SCOPE = "scope";
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ACTIVE = "active";
    private static final String TOKEN_NAME = "access_token";

    // gets the issuer URL so that the relying party can authorize the request
    private String introspectionEndpointUri;
    // the client id of the resource server.
    private String clientId;
    // the client secret of the resource server.
    private String clientSecret;

    /**
     * Checks whether there's a basic authentication in the header of the
     * request. The basic authentication is compound by: 'Authorization': 'Basic
     * ' + encodeClientCredentials(client.client_id, client.client_secret). HTTP
     * Basic is a base64 encoded string made by concatenating the client_id and
     * client_secret together, separated by a single colon (:) character.
     * 
     * @param request
     *            The standard request, the authorization must be set as part of
     *            the header of the request.
     * @param allClients
     *            List of all possible clients to be evaluated.
     * @return The credentials for this client, the clientId (index 0) and
     *         clientSecret at (index 1).
     */
    public String[] handleBasicAuthRequest(HttpServletRequest request, Map<String, String> allClients) {
        // check the authorization header
        String auth = request.getHeader(AUTHORIZATION);
        String[] credentials = null;

        if (auth != null && !auth.trim().isEmpty() && (auth.startsWith("Basic"))) {
            auth = auth.substring(5).trim();

            byte[] bytes = null;
            try {
                bytes = DatatypeConverter.parseBase64Binary(auth);
            } catch (Exception e) {
                log.error("The basic auth: [" + auth + "] could not be decoded");
                throw new AuthorizationServiceException(INVALID_BASIC);
            }

            String clientIdSecret = new String(bytes);
            credentials = clientIdSecret.split(":");

            boolean found = false;
            // look up the right credentials
            for (String clientId : allClients.keySet()) {
                String clientSecret = allClients.get(clientId);
                if (clientId.equals(credentials[0]) && clientSecret.equals(credentials[1])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.debug("Unable to validate request with basic authentication");
                throw new AuthorizationServiceException(INVALID_BASIC_CRED);
            }
        } else {
            throw new AuthorizationServiceException(UNAUTHORIZED_BASIC);
        }
        // at this point the credentials were validated correctly.
        // we simply return them.
        return credentials;
    }

    /**
     * Check whether the request is valid based on the access token. It should
     * be desirable to catch this exception
     * <code>AuthorizationServiceException</code> in order to know what the
     * reason is when the client was not authorized.
     * 
     * @param request
     *            The standard request, the access token should be in the header
     *            or the body of the request.
     * @return The introspected active token.
     * @see OIDCAuthenticationToken#handleAuthorizationRequest(String)
     */
    public JsonElement handleAuthorizationRequest(HttpServletRequest request) {
        return this.handleAuthorizationRequest(seachForAccessToken(request));
    }

    /**
     * Check whether the request is valid based on the access token. It should
     * be desirable to catch this exception
     * <code>AuthorizationServiceException</code> in order to know what the
     * reason is when the client was not authorized.
     * 
     * @param accessToken
     *            The access token.
     * @return The actual introspected token payload.
     * @see OIDCAuthenticationToken#handleAuthorizationRequest(HttpServletRequest)
     */
    public JsonElement handleAuthorizationRequest(String accessToken) {
        String jsonString = requestTokenIntrospection(accessToken, clientId, clientSecret, introspectionEndpointUri);
        log.debug("Token Introspection Response: " + jsonString);
        // we assume we have a valid response
        JsonElement jsonTokenIntrospected = new JsonParser().parse(jsonString);
        if (!jsonTokenIntrospected.isJsonObject()) {
            throw new AuthorizationServiceException(
                    new HttpMessageErrorCodeResolver(NO_VALID_JSON, jsonTokenIntrospected));
        }
        boolean active = false;
        JsonObject tokenResponse = jsonTokenIntrospected.getAsJsonObject();
        if (tokenResponse.has(ACTIVE)) {
            active = tokenResponse.get(ACTIVE).getAsBoolean();
        } else {
            throw new AuthorizationServiceException(
                    new HttpMessageErrorCodeResolver(NO_ACTIVE_AVAILABLE, jsonTokenIntrospected));
        }
        // we check whether the token is active or not.
        if (!active) {
            throw new AuthorizationServiceException(NO_ACTIVE_TOKEN);
        }
        // we know the token is active, we simply return it so that it can be
        // used somewhere else.
        return jsonTokenIntrospected;
    }

    /**
     * Check whether the JSON object has defined the scopes to handle the
     * protected resource being requested. If the scopes are found, then it
     * returns a list of elements with the corresponding values. If for some
     * reason the scopes are not present in the JSON object, this exception
     * <code>AuthorizationServiceException</code> is thrown, meaning the client
     * is not authorized or does not have the right permission to access the
     * protected resource.
     * 
     * *** SPECIFICATION_NOTE: Something important here is that, keycloak does
     * not return the scope element as part of the introspection endpoint, hence
     * we need to check in another location too. We check the standard location
     * MITRE (openid specification), if it's not found, we check the
     * 'realm_access' property.
     * 
     * @param jsonElement
     *            The introspected JSON element.
     * @return List of scopes available for this JSON token.
     */
    public Set<String> handleScopeRequest(JsonElement jsonElement) {
        if (jsonElement == null) {
            throw new IllegalArgumentException("JSON element with the token is null");
        }

        JsonObject tokenResponse = jsonElement.getAsJsonObject();
        String scopesFound = null;
        if (tokenResponse.has(SCOPE)) {
            scopesFound = tokenResponse.get(SCOPE).getAsString();
        } else {
            // non standard openid element
            if (tokenResponse.has(REALM_ACCESS)) {
                JsonElement jsonRealmElement = tokenResponse.get(REALM_ACCESS);
                boolean hasRoles = jsonRealmElement.getAsJsonObject().has(ROLES);
                if (hasRoles) {
                    if (jsonRealmElement.getAsJsonObject().get(ROLES).isJsonArray()) {
                        StringBuilder sb = new StringBuilder();
                        JsonArray jsonArray = jsonRealmElement.getAsJsonObject().get(ROLES).getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            sb.append(jsonArray.get(i).getAsString());
                            sb.append(" ");
                        }
                        scopesFound = sb.toString().trim();
                    }
                } else {
                    // no roles were found.
                    throw new AuthorizationServiceException(
                            new HttpMessageErrorCodeResolver(NO_ROLE_AVAILABLE, jsonRealmElement));
                }
            } else {
                // no scopes were found.
                throw new AuthorizationServiceException(NO_SCOPE_AVAILABLE);
            }
            if (scopesFound == null || scopesFound.trim().isEmpty()) {
                // empty scopes.
                throw new AuthorizationServiceException(NO_SCOPE_FOUND);
            }
        }
        // assuming that the scopes are found and separated by spaces.
        StringTokenizer st = new StringTokenizer(scopesFound);
        Set<String> scopes = new HashSet<>();
        while (st.hasMoreElements()) {
            scopes.add(st.nextToken());
        }
        return scopes;
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
