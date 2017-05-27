/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.client.service.impl;

import com.ztarmobile.oauth2.model.RegisteredClient;
import com.ztarmobile.openid.connect.config.service.RegisteredClientService;

import java.util.HashMap;
import java.util.Map;

/**
 * The client holds its data in memory.
 * 
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class InMemoryRegisteredClientService implements RegisteredClientService {

    private Map<String, RegisteredClient> clients = new HashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see org.mitre.openid.connect.client.service.RegisteredClientService#
     * getByIssuer(java.lang.String)
     */
    @Override
    public RegisteredClient getByIssuer(String issuer) {
        return clients.get(issuer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mitre.openid.connect.client.service.RegisteredClientService#save(org.
     * mitre.oauth2.model.RegisteredClient)
     */
    @Override
    public void save(String issuer, RegisteredClient client) {
        clients.put(issuer, client);
    }
}
