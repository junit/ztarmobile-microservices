/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.controllers;

import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.MMDDYYYY;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.model.Response;
import com.ztarmobile.invoicing.repository.CatalogProductRepository;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller class.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 2.0
 */
@RestController
@RequestMapping(value = "${spring.data.rest.base-path}")
public class InvoicingServiceController {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(InvoicingServiceController.class);

    /**
     * All the mappings.
     */
    private static final String REPORT_REQUEST_MAPPING = "/report/request";
    private static final String REPORT_LOG_MAPPING = "/report/log";
    private static final String REPORT_DOWNLOAD_MAPPING = "/report/download";
    private static final String PRODUCTS_MAPPING = "/products";
    private static final String ECHO_MAPPING = "/echo";

    @Autowired
    private CatalogProductRepository catalogProductRepository;

    @RequestMapping(value = REPORT_REQUEST_MAPPING, method = POST)
    public Response processInvoicing(@RequestParam("reportFrom") @DateTimeFormat(pattern = MMDDYYYY) Date reportFrom,
            @RequestParam("reportTo") @DateTimeFormat(pattern = MMDDYYYY) Date reportTo,
            @RequestParam("product") String product, @RequestParam("rerunInvoicing") boolean rerunInvoicing) {

        // validates the parameters...
        validateCommonInput(reportFrom, reportTo, product);

        InvoicingRequest invoicingRequest = new InvoicingRequest();
        invoicingRequest.setReportFrom(reportFrom);
        invoicingRequest.setReportTo(reportTo);
        invoicingRequest.setProduct(product);
        invoicingRequest.setRerunInvoicing(rerunInvoicing);

        return new Response();
    }

    /**
     * Get all the requests.
     * 
     * @return The list of requests available.
     */
    @RequestMapping(value = REPORT_LOG_MAPPING, method = GET)
    public Response getAllAvailableRequests() {
        log.debug("Requesting all the requests...");

        // we just send the response to the client.
        return new Response();
    }

    /**
     * Gets an stream to download a requested invoice file.
     * 
     * @param response
     *            The HTTP response.
     * @param reportFrom
     *            The report from.
     * @param reportTo
     *            The report to.
     * @param product
     *            The product description.
     * @return The empty response.
     */
    @RequestMapping(value = REPORT_DOWNLOAD_MAPPING, method = GET, produces = { "text/csv" })
    public Response getFileStreamingOutput(HttpServletResponse response,
            @RequestParam("reportFrom") @DateTimeFormat(pattern = MMDDYYYY) Date reportFrom,
            @RequestParam("reportTo") @DateTimeFormat(pattern = MMDDYYYY) Date reportTo,
            @RequestParam("product") String product) {
        return new Response();
    }

    /**
     * Get all the products.
     * 
     * @return The list of products.
     */
    @RequestMapping(value = PRODUCTS_MAPPING, method = GET)
    public Response getAllAvailableProducts() {
        log.debug("Requesting all the products...");

        // we just send the response to the client.
        return new Response(catalogProductRepository.findAll());
    }

    /**
     * This is just a ping endPoint.
     * 
     * @return Just a 'ok' message to indicate that the service is alive.
     */
    @RequestMapping(value = ECHO_MAPPING, method = GET)
    public Response echo() {
        log.debug("Requesting echo...");

        // a simple message...
        return new Response("I'm alive :)");
    }

    /**
     * Validate the common input parameters.
     * 
     * @param reportFrom
     *            The report from.
     * @param reportTo
     *            The report to.
     * @param product
     *            The product.
     */
    private void validateCommonInput(Date reportFrom, Date reportTo, String product) {
        // we validate some inputs before sending the request to the queue
        validateInput(reportFrom, "Please provide a 'reportFrom' parameter using this format: " + MMDDYYYY);
        validateInput(reportTo, "Please provide a 'reportTo' parameter using this format: " + MMDDYYYY);
        validateInput(product, "The 'product' cannot be empty");
    }
}
