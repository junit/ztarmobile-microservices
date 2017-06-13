/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ztarmobile.profile.model.UserProfile;
import com.ztarmobile.profile.service.UserProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
public class UserProfileServiceController extends CommonController {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceController.class);

    /**
     * All the mappings.
     */
    private static final String USER_PROF_MAPPING = "userProfiles";
    private static final String USER_PROFILE_MAPPING = USER_PROF_MAPPING + "/{userProfile}";

    /**
     * The service associated with the user profile.
     */
    @Autowired
    private UserProfileService userProfileService;

    /**
     * This endpoint creates a new profile.
     * 
     * @param userProfile
     *            The user profile to be created.
     * @return The representation of the use profile.
     */
    @RequestMapping(value = USER_PROF_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<UserProfile> createNewUserProfile(@RequestBody UserProfile userProfile) {
        // delegates the work to the service layer.
        long userProfileId = userProfileService.createNewUserProfile(userProfile);

        // the service has been created, we add some info in the header.
        String resourceLocation = createBaseUrl(USER_PROF_MAPPING, userProfileId);
        return new ResponseEntity<UserProfile>(userProfile, createResponseHeader(resourceLocation), CREATED);
    }

    @RequestMapping(value = USER_PROFILE_MAPPING, method = GET)
    public HttpEntity<UserProfile> readUserProfile(@PathVariable("userProfile") long userProfileId) {
        log.debug("Getting user profile details");

        // delegates the work to the service layer.
        UserProfile userProfile = userProfileService.readUserProfile(userProfileId);
        return new ResponseEntity<UserProfile>(userProfile, OK);
    }
}
