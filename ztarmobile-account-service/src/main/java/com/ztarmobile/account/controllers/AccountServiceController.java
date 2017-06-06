/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.controllers;

import static com.ztarmobile.account.common.CommonUtils.createServiceUrl;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import com.ztarmobile.account.annotation.EnableBasicAuthentication;
import com.ztarmobile.account.annotation.IgnoreSecurity;
import com.ztarmobile.account.model.Echo;
import com.ztarmobile.account.model.UserAccount;
import com.ztarmobile.account.service.UserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller class.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@RestController
@RequestMapping(value = "${spring.data.rest.base-path}")
public class AccountServiceController {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AccountServiceController.class);
    /**
     * All the mappings.
     */
    private static final String ECHO_MAPPING = "/echo";
    private static final String USER_ACCOUNT_MAPPING = "/users";

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
     * The service associated with the account management.
     */
    @Autowired
    private UserAccountService userAccountService;

    /**
     * This endPoint creates a new account so that the user can be
     * authenticated.
     * 
     * @param userAccount
     *            The user account to be created.
     * @return The user created.
     */
    @EnableBasicAuthentication
    @RequestMapping(value = USER_ACCOUNT_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<UserAccount> createNewAccount(@RequestBody UserAccount userAccount) {
        // delegates the work to the service layer.
        userAccountService.createNewUserAccount(userAccount);
        // the service has been created, we add some info in the header.
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", createLocation(userAccount.getUserId()));
        return new ResponseEntity<UserAccount>(userAccount, responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Creates the location of the new account created.
     * 
     * @param userId
     *            The userId generated.
     * @return The location.
     */
    private String createLocation(String userId) {
        return createServiceUrl(serverAddress, serverPort, basePath) + USER_ACCOUNT_MAPPING + "/" + userId;
    }

    /**
     * This is just a ping endPoint.
     * 
     * @return Just a 'OK' message to indicate that the service is alive.
     */
    @RequestMapping(value = ECHO_MAPPING, method = GET)
    public HttpEntity<Echo> echo() {
        log.debug("Requesting GET echo...");

        // a simple message...
        return new ResponseEntity<Echo>(new Echo(), HttpStatus.OK);
    }

    /**
     * This is just a ping endPoint.
     * 
     * @return Just a 'OK' message to indicate that the service is alive.
     */
    @RequestMapping(value = ECHO_MAPPING, method = POST)
    public HttpEntity<Echo> echoPost() {
        log.debug("Requesting POST echo...");

        // a simple message...
        return new ResponseEntity<Echo>(new Echo(), HttpStatus.OK);
    }

    /**
     * This is just a ping endPoint.
     * 
     * @return Just a 'OK' message to indicate that the service is alive.
     */
    @RequestMapping(value = ECHO_MAPPING, method = PUT)
    public HttpEntity<Echo> echoPut() {
        log.debug("Requesting PUT echo...");

        // a simple message...
        return new ResponseEntity<Echo>(new Echo(), HttpStatus.OK);
    }

}
