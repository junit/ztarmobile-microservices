/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.ztarmobile.invoicing.controllers.InvoicingServiceController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Adds custom controller links to the index resource.
 * 
 * @author armandorivas
 * @version %I%, %G%
 * @since 2.0
 */
@Component
public class ApplicationResourceProcessor implements ResourceProcessor<RepositoryLinksResource> {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationResourceProcessor.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        log.debug("Listing all initial resources.");

        // adds new links to the index resource.
        resource.add(linkTo(methodOn(InvoicingServiceController.class).processInvoicing(null, null, null, false))
                .withRel("invoicing:requests"));
        resource.add(
                linkTo(methodOn(InvoicingServiceController.class).getAllAvailableRequests()).withRel("invoicing:logs"));
        resource.add(linkTo(methodOn(InvoicingServiceController.class).getFileStreamingOutput(null, null, null, null))
                .withRel("invoicing:downloads"));
        resource.add(linkTo(methodOn(InvoicingServiceController.class).getAllAvailableProducts())
                .withRel("invoicing:products"));
        resource.add(linkTo(methodOn(InvoicingServiceController.class).echo()).withRel("invoicing:echo"));

        return resource;
    }
}
