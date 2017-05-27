/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.config.service;

import com.ztarmobile.oauth2.model.RegisteredClient;

/**
 * The Registered client service.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface RegisteredClientService {

    /**
     * Get a remembered client (if one exists) to talk to the given issuer. This
     * client likely doesn't have its full configuration information but
     * contains the information needed to fetch it.
     *
     * @param issuer
     * @return
     */
    RegisteredClient getByIssuer(String issuer);

    /**
     * Save this client's information for talking to the given issuer. This will
     * save only enough information to fetch the client's full configuration
     * from the server.
     *
     * @param client
     */
    void save(String issuer, RegisteredClient client);
}
