/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.controllers;

import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.MMDDYYYY;
import static com.ztarmobile.invoicing.jms.InvoicingReceiver.INVOICING_REQ_QUEUE;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.model.Response;
import com.ztarmobile.invoicing.service.InvoicingService;

/**
 * The controller class.
 *
 * @author armandorivas
 * @since 03/27/17
 */
@RestController
@RequestMapping(value = "v1/invoice/report/")
public class InvoicingServiceController {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(InvoicingServiceController.class);

    /**
     * Dependency of the invoicing service.
     */
    @Autowired
    private InvoicingService invoicingService;
    /**
     * The JSM template.
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public Response processInvoicing(@RequestParam("reportFrom") @DateTimeFormat(pattern = MMDDYYYY) Date reportFrom,
            @RequestParam("reportTo") @DateTimeFormat(pattern = MMDDYYYY) Date reportTo,
            @RequestParam("product") String product, @RequestParam("rerunInvoicing") boolean rerunInvoicing) {

        // we validate some inputs before sending the request to the queue
        validateInput(reportFrom, "Please provide a 'reportFrom' parameter using this format: " + MMDDYYYY);
        validateInput(reportTo, "Please provide a 'reportTo' parameter using this format: " + MMDDYYYY);
        validateInput(product, "The 'product' cannot be empty");

        InvoicingRequest invoicingRequest = new InvoicingRequest();
        invoicingRequest.setReportFrom(reportFrom);
        invoicingRequest.setReportTo(reportTo);
        invoicingRequest.setProduct(product);
        invoicingRequest.setRerunInvoicing(rerunInvoicing);

        // send the request to the queue
        jmsTemplate.convertAndSend(INVOICING_REQ_QUEUE, invoicingRequest);
        return new Response();
    }

    /**
     * Get all the requests.
     * 
     * @return The list of requests available.
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public Response getAllAvailableRequests() {
        LOG.debug("Requesting all the requests...");

        // we just send the response to the client.
        return new Response(invoicingService.getAllAvailableRequests());
    }

    /**
     * Get all the products.
     * 
     * @return The list of products.
     */
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public Response getAllAvailableProducts() {
        LOG.debug("Requesting all the products...");

        // we just send the response to the client.
        return new Response(invoicingService.getAllAvailableProducts());
    }
}
