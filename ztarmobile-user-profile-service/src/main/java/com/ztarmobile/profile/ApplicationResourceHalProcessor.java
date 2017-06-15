/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile;

import static com.ztarmobile.profile.common.CommonUtils.evaluateHateoasLink;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.ztarmobile.profile.controllers.UserProfileServiceController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Adds custom controller links to the index resource.
 * 
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class ApplicationResourceHalProcessor implements ResourceProcessor<RepositoryLinksResource> {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationResourceHalProcessor.class);

    /**
     * Based path.
     */
    @Value("${spring.data.rest.base-path}")
    private String basePath;

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        log.debug("Listing all initial resources.");

        // adds new links to the index resource.

        resource.add(evaluateHateoasLink(linkTo(methodOn(UserProfileServiceController.class).createNewUserProfile(null))
                .withRel("createNewUserProfile"), basePath));
        resource.add(evaluateHateoasLink(
                linkTo(methodOn(UserProfileServiceController.class).readUserProfile(0)).withRel("readUserProfile"),
                basePath));
        resource.add(evaluateHateoasLink(
                linkTo(methodOn(UserProfileServiceController.class).readUserProfileWithPaymentProfile(0))
                        .withRel("readUserProfileWithPaymentProfile"),
                basePath));
        resource.add(
                evaluateHateoasLink(linkTo(methodOn(UserProfileServiceController.class).readUserProfileWithAddress(0))
                        .withRel("readUserProfileWithAddress"), basePath));
        resource.add(evaluateHateoasLink(linkTo(methodOn(UserProfileServiceController.class).readUserProfileWithMdn(0))
                .withRel("readUserProfileWithMdn"), basePath));

        return resource;
    }

}
