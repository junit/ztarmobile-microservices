/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.controllers;

import static com.ztarmobile.profile.common.CommonUtils.SLASH;
import static com.ztarmobile.profile.common.CommonUtils.createServiceUrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The controller class.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@RequestMapping(value = "${spring.data.rest.base-path}")
public abstract class CommonController {

    /**
     * Based path.
     */
    @Value("${spring.data.rest.base-path}")
    private String basePath;
    /**
     * The server address.
     */
    @Value("${server.address}")
    private String serverAddress;
    /**
     * The server port.
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * Creates the response header.
     * 
     * @param resourceLocation
     *            The resource location.
     * @return The header to be returned.
     */
    protected HttpHeaders createResponseHeader(String resourceLocation) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", resourceLocation);
        return responseHeaders;
    }

    /**
     * Creates the base URL.
     * 
     * @return The base URL for the service.
     */
    protected String createBaseUrl() {
        return createServiceUrl(serverAddress, serverPort, basePath);
    }

    /**
     * Creates the base URL.
     * 
     * @param userProfileMapping
     *            The user profile mapping.
     * @param userProfileId
     *            The user profileId.
     * @return The base URL.
     */
    protected String createBaseUrl(String userProfileMapping, long userProfileId) {
        return createBaseUrl() + SLASH + userProfileMapping + SLASH + userProfileId;
    }
}
