/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.controllers;

import static com.ztarmobile.invoicing.common.DateUtils.fromStringToMMddYYYYFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ztarmobile.invoicing.model.Invoicing;
import com.ztarmobile.invoicing.model.LoggerRequest;
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
    public Invoicing processInvoicing(@PathVariable("reportFrom") String reportFrom,
            @PathVariable("reportTo") String reportTo, @PathVariable("product") String product,
            @PathVariable("rerunInvoicing") boolean rerunInvoicing) {

        Date dateFrom = fromStringToMMddYYYYFormat(reportFrom);
        Date dateTo = fromStringToMMddYYYYFormat(reportTo);

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(dateFrom);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateTo);

        invoicingService.performInvoicing(calendarFrom, calendarTo, product, rerunInvoicing);

        return new Invoicing();
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
