/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ztarmobile.account.service.ResourceSetScopeService;

/**
 * Application ready listener.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class ApplicationReadyCustomScopeListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationReadyCustomScopeListener.class);

    @Autowired
    private ResourceSetScopeService resourceSetScopeService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.debug("Synchronizing the roles to scopes");

        resourceSetScopeService.syncFromRolesToScope();
        log.debug("Synch completed");
    }
}
