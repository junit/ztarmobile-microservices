/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.jms;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.service.InvoicingService;

/**
 * Receives all the messages from the JMS.
 *
 * @author armandorivas
 * @since 03/29/17
 */
@Component
public class InvoicingReceiver {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(InvoicingReceiver.class);

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
    @JmsListener(destination = "invoicing.requests", containerFactory = "myFactory")
    public void receiveMessage(InvoicingRequest request) {
        LOG.debug("Receiving request: " + request);

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(request.getReportFrom());

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(request.getReportTo());

        // performs the invoicing stuff.
        invoicingService.performInvoicing(calendarFrom, calendarTo, request.getProduct(), request.isRerunInvoicing());
    }

}
