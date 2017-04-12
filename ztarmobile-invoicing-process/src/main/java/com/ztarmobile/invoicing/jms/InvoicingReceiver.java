/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.jms;

import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.service.InvoicingService;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Receives all the messages from the JMS.
 *
 * @author armandorivas
 * @since 03/29/17
 */
@Component
public class InvoicingReceiver {
    /**
     * The queue name.
     */
    public static final String INVOICING_REQ_QUEUE = "invoicing.requests";

    /**
     * Logger for this class.
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
    @JmsListener(destination = INVOICING_REQ_QUEUE, containerFactory = "myFactory")
    public void receiveMessage(InvoicingRequest request) {
        LOG.debug("Receiving request: " + request);

        Calendar calendarFrom = null;
        Calendar calendarTo = null;
        String product = null;

        product = request.getProduct();
        if (!request.isRunPreviousMonth()) {
            calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(request.getReportFrom());

            calendarTo = Calendar.getInstance();
            calendarTo.setTime(request.getReportTo());
        }

        try {
            // performs the invoicing stuff.
            if (!request.isRunPreviousMonth()) {
                invoicingService.performInvoicing(calendarFrom, calendarTo, product, request.isRerunInvoicing());
            } else {
                // process the previous month.
                invoicingService.performInvoicing(product, request.isRerunInvoicing());
            }
        } catch (Throwable ex) {
            LOG.debug("Request did not finish correctly :( ");
            // we log the error...
            LOG.error(ex);
        }
    }

}
