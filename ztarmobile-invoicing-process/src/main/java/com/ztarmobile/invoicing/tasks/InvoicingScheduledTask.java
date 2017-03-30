/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.tasks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ztarmobile.invoicing.model.CatalogProduct;
import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.service.InvoicingService;

/**
 * Performs an schedule task to create the montly reports.
 *
 * @author armandorivas
 * @since 03/30/17
 */
@Component
public class InvoicingScheduledTask {
    /**
     * The queue name.
     */
    public static final String INVOICING_REQ_QUEUE = "invoicing.requests";

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(InvoicingScheduledTask.class);

    /**
     * The JSM template.
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Dependency of the invoicing service.
     */
    @Autowired
    private InvoicingService invoicingService;

    /**
     * This method receives the request from the queue and process it.
     * 
     * @param request
     *            The request.
     */
    @Scheduled(cron = "${invoice.report.cron}")
    public void scheduleMonthlyInvoice(InvoicingRequest request) {
        LOG.debug("Requesting montly invoicing report...");

        for (CatalogProduct catalogProduct : invoicingService.getAllAvailableProducts()) {
            LOG.debug("Processing: " + catalogProduct);
            InvoicingRequest invoicingRequest = new InvoicingRequest();
            invoicingRequest.setProduct(catalogProduct.getProduct());
            // we make sure the we run the previous month
            invoicingRequest.setRunPreviousMonth(true);

            // send the request to the queue
            jmsTemplate.convertAndSend(INVOICING_REQ_QUEUE, invoicingRequest);
        }
    }
}
