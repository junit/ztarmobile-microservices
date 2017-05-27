/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.client;

import static com.ztarmobile.exception.AuthorizationMessageErrorCode.UNAUTHORIZED_ACCESS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.nimbusds.jose.util.Base64;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

/**
 * This class handle the common operations sent to the identity provider.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class OpenIdConnectUtil {
    /**
     * Timeout supported for all transactions.
     */
    public static final int HTTP_SOCKET_TIMEOUT = 30000;
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(OpenIdConnectUtil.class);

    /**
     * Sends a request to validate the token using basic based authorization.
     * 
     * @param accessToken
     *            The access token.
     * @param clientId
     *            The client id.
     * @param clientSecret
     *            The client secret.
     * @param introspectEndpointUri
     *            The introspect uri.
     * @return Response of the request.
     */
    public static String requestTokenIntrospection(String accessToken, String clientId, String clientSecret,
            String introspectEndpointUri) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("token", accessToken);

        HttpComponentsClientHttpRequestFactory factory = getClientHttpRequestFactory();
        RestTemplate restTemplate = createBasicRestTemplate(factory, clientId, clientSecret);

        return sendRawHttpRequest(restTemplate, introspectEndpointUri, form);
    }

    /**
     * Creates a basic rest template.
     * 
     * @param factory
     *            The factory.
     * @param clientId
     *            A client id.
     * @param clientSecret
     *            The client secret.
     * @return The rest template.
     */
    private static RestTemplate createBasicRestTemplate(HttpComponentsClientHttpRequestFactory factory, String clientId,
            String clientSecret) {
        // use BASIC auth if configured to do so
        RestTemplate restTemplate = new RestTemplate(factory) {
            @Override
            protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
                ClientHttpRequest httpRequest = super.createRequest(url, method);
                httpRequest.getHeaders().add(AUTHORIZATION,
                        String.format("Basic %s",
                                Base64.encode(String.format("%s:%s", UriUtils.encodePathSegment(clientId, "UTF-8"),
                                        UriUtils.encodePathSegment(clientSecret, "UTF-8")))));
                return httpRequest;
            }
        };
        return restTemplate;
    }

    /**
     * Sends a raw request to an external resource.
     * 
     * @param restTemplate
     *            The rest template.
     * @param url
     *            The url.
     * @param form
     *            The parameters.
     * @return The response received for the external resource.
     */
    private static String sendRawHttpRequest(RestTemplate restTemplate, String url,
            MultiValueMap<String, String> form) {
        String jsonString = null;
        try {
            log.info("endpointURI = " + url);
            log.debug("form = " + form);

            jsonString = restTemplate.postForObject(url, form, String.class);
        } catch (RestClientException e) {
            if (e.getMessage().contains(HttpStatus.UNAUTHORIZED.toString())) {
                // Handle error
                log.error("Not allowed to access this resource");
                throw new AuthorizationServiceException(UNAUTHORIZED_ACCESS);
            } else {
                // Handle error
                log.error("Unable to access this resource " + url + " due to: " + e.getMessage());
                throw new AuthorizationServiceException(e.getMessage());
            }
        }
        log.debug("Response = " + jsonString);
        return jsonString;
    }

    /**
     * Creates a HTTP request factory.
     * 
     * @return The HTTP factory.
     */
    private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {
        // Handle Token End point interaction
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties()
                .setDefaultRequestConfig(RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIMEOUT).build()).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return factory;
    }
}
