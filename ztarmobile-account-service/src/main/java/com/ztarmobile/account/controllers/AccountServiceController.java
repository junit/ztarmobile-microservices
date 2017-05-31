/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.ztarmobile.account.model.Echo;
import com.ztarmobile.account.repository.UserAccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserAccountRepository userAccountRepository;

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
    /*
     * @RequestMapping(value = ECHO_MAPPING, method = POST) public
     * HttpEntity<Echo> echoPost() { log.debug("Requesting POST echo...");
     * 
     * // a simple message... return new ResponseEntity<Echo>(new Echo(),
     * HttpStatus.OK); }
     */
}
