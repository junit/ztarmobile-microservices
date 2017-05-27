/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.client.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ztarmobile.oauth2.model.RegisteredClient;
import com.ztarmobile.openid.connect.ClientDetailsEntityJsonProcessor;
import com.ztarmobile.openid.connect.config.service.ClientConfigurationService;
import com.ztarmobile.openid.connect.config.service.RegisteredClientService;
import com.ztarmobile.openid.connect.config.service.ServerConfiguration;

import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Dynamically register the client.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class DynamicRegistrationClientConfigurationService implements ClientConfigurationService {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(DynamicRegistrationClientConfigurationService.class);
    private LoadingCache<ServerConfiguration, RegisteredClient> clients;

    @Autowired
    private RegisteredClientService registeredClientService;

    private RegisteredClient template;

    public DynamicRegistrationClientConfigurationService() {
        this(HttpClientBuilder.create().useSystemProperties().build());
    }

    public DynamicRegistrationClientConfigurationService(HttpClient httpClient) {
        clients = CacheBuilder.newBuilder().build(new DynamicClientRegistrationLoader(httpClient));
    }

    @Override
    public RegisteredClient getClientConfiguration(ServerConfiguration issuer) {
        try {
            return clients.get(issuer);
        } catch (UncheckedExecutionException | ExecutionException e) {
            log.warn("Unable to get client configuration", e);
            return null;
        }
    }

    /**
     * @return the template
     */
    public RegisteredClient getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(RegisteredClient template) {
        // make sure the template doesn't have unwanted fields set on it
        if (template != null) {
            template.setClientId(null);
            template.setClientSecret(null);
            template.setRegistrationClientUri(null);
            template.setRegistrationAccessToken(null);
        }
        this.template = template;
    }

    /**
     * Loader class that fetches the client information.
     *
     * If a client has been registered (ie, it's known to the
     * RegisteredClientService), then this will fetch the client's configuration
     * from the server.
     *
     * @author jricher
     *
     */
    public class DynamicClientRegistrationLoader extends CacheLoader<ServerConfiguration, RegisteredClient> {

        private HttpComponentsClientHttpRequestFactory httpFactory;
        private Gson gson = new Gson(); // note that this doesn't serialize
                                        // nulls by default

        public DynamicClientRegistrationLoader() {
            this(HttpClientBuilder.create().useSystemProperties().build());
        }

        public DynamicClientRegistrationLoader(HttpClient httpClient) {
            this.httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        }

        @Override
        public RegisteredClient load(ServerConfiguration serverConfig) throws Exception {
            RestTemplate restTemplate = new RestTemplate(httpFactory);

            RegisteredClient knownClient = registeredClientService.getByIssuer(serverConfig.getIssuer());
            if (knownClient == null) {

                // dynamically register this client
                JsonObject jsonRequest = ClientDetailsEntityJsonProcessor.serialize(template);
                String serializedClient = gson.toJson(jsonRequest);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));

                HttpEntity<String> entity = new HttpEntity<>(serializedClient, headers);

                try {
                    String registered = restTemplate.postForObject(serverConfig.getRegistrationEndpointUri(), entity,
                            String.class);

                    RegisteredClient client = ClientDetailsEntityJsonProcessor.parseRegistered(registered);

                    // save this client for later
                    registeredClientService.save(serverConfig.getIssuer(), client);

                    return client;
                } catch (RestClientException rce) {
                    throw new IllegalStateException("Error registering client with server");
                }
            } else {

                if (knownClient.getClientId() == null) {

                    // load this client's information from the server
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization",
                            String.format("%s %s", "Bearer", knownClient.getRegistrationAccessToken()));
                    headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));

                    HttpEntity<String> entity = new HttpEntity<>(headers);

                    try {
                        String registered = restTemplate
                                .exchange(knownClient.getRegistrationClientUri(), HttpMethod.GET, entity, String.class)
                                .getBody();
                        // TODO: handle HTTP errors

                        RegisteredClient client = ClientDetailsEntityJsonProcessor.parseRegistered(registered);

                        return client;
                    } catch (RestClientException rce) {
                        throw new IllegalStateException(
                                "Error loading previously registered client information from server");
                    }
                } else {
                    // it's got a client ID from the store, don't bother trying
                    // to load it
                    return knownClient;
                }
            }
        }

    }

}
