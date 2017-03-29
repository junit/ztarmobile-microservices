/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.controllers;

import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.MMDDYYYY;
import static com.ztarmobile.invoicing.model.Response.SUCCESS;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ztarmobile.invoicing.model.LoggerRequest;
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
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(InvoicingServiceController.class);

    /**
     * Dependency of the invoicing service.
     */
    @Autowired
    private InvoicingService invoicingService;

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public Response processInvoicing(@RequestParam("reportFrom") @DateTimeFormat(pattern = MMDDYYYY) Date reportFrom,
            @RequestParam("reportTo") @DateTimeFormat(pattern = MMDDYYYY) Date reportTo,
            @RequestParam("product") String product, @RequestParam("rerunInvoicing") boolean rerunInvoicing) {

        validateInput(reportFrom, "Please provide a 'reportFrom' parameter using this format: " + MMDDYYYY);
        validateInput(reportTo, "Please provide a 'reportTo' parameter using this format: " + MMDDYYYY);
        validateInput(product, "The 'product' cannot be empty");

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(reportFrom);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(reportTo);

        invoicingService.performInvoicing(calendarFrom, calendarTo, product, rerunInvoicing);
        Response response = new Response();
        response.setStatus(SUCCESS);
        response.setDetail("More json goes here...");
        return response;
    }

    /**
     * Get all the requests available.
     * 
     * @return The list of requests available.
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public List<LoggerRequest> processdInvoicing() {
        LOG.debug("Requesting all the requests available...");

        return invoicingService.getAllAvailableRequests();
    }
}
