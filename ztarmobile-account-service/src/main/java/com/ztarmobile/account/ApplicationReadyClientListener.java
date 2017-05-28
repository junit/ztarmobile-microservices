/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.account;

import com.ztarmobile.oauth2.model.RegisteredClient;
import com.ztarmobile.openid.connect.client.OIDCAuthenticationToken;
import com.ztarmobile.openid.connect.client.service.impl.DynamicRegistrationClientConfigurationService;
import com.ztarmobile.openid.connect.config.service.ClientConfigurationService;
import com.ztarmobile.openid.connect.config.service.ServerConfiguration;
import com.ztarmobile.openid.connect.config.service.ServerConfigurationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Application ready listener.
 *
 * @author armandorivas
 * @since 05/05/17
 */
@Component
public class ApplicationReadyClientListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationReadyClientListener.class);

    /**
     * The identity provider URL.
     */
    @Value("${account.openid.issuer}")
    private String identityProviderUrl;

    /**
     * The client name.
     */
    @Value("${account.openid.client.name}")
    private String clientName;

    // holds server information (auth URI, token URI, etc.), indexed by issuer
    @Autowired
    private ServerConfigurationService servers;
    // holds client information (client ID, redirect URI, etc.), indexed by
    // issuer of the server
    @Autowired
    private ClientConfigurationService clients;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.debug("Registering this client with the IdP: " + identityProviderUrl);
        ServerConfiguration serverConfig = servers.getServerConfiguration(identityProviderUrl);
        if (serverConfig == null) {
            log.error("Server configuration not found for issuer: " + identityProviderUrl);
            throw new IllegalStateException("No server configuration found for issuer: " + identityProviderUrl);
        }

        DynamicRegistrationClientConfigurationService dynamicClient = null;
        if (clients instanceof DynamicRegistrationClientConfigurationService) {
            RegisteredClient registeredClient = new RegisteredClient();
            registeredClient.setClientName(clientName);

            dynamicClient = (DynamicRegistrationClientConfigurationService) clients;
            dynamicClient.setTemplate(registeredClient);
        } else {
            log.error("Only dynamic clients are allowed");
            throw new IllegalStateException("Dynamic clients are allowed...");
        }

        RegisteredClient clientConfig = clients.getClientConfiguration(serverConfig);
        if (clientConfig == null) {
            // for some reason the client could not be registered.
            throw new IllegalStateException(
                    "There was a problem while trying to register this client with the server.");
        }
        String clientId = clientConfig.getClient().getClientId();
        String clientSecret = clientConfig.getClient().getClientSecret();
        String introspectionEndpointUri = serverConfig.getIntrospectionEndpointUri();

        // we set these properties into the actual bean (manually injected)
        OIDCAuthenticationToken authToken = event.getApplicationContext().getBean(OIDCAuthenticationToken.class);
        authToken.setClientId(clientId);
        authToken.setClientSecret(clientSecret);
        authToken.setIntrospectionEndpointUri(introspectionEndpointUri);
        log.debug("ClientId: " + clientId);
    }
}
