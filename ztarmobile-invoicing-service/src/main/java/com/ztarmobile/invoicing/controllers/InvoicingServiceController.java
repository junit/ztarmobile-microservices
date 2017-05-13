/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.controllers;

import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.MMDDYYYY;
import static com.ztarmobile.invoicing.common.ReportHelper.createHeader;
import static com.ztarmobile.invoicing.common.ReportHelper.createReportName;
import static com.ztarmobile.invoicing.common.ReportHelper.createRow;
import static com.ztarmobile.invoicing.jms.InvoicingReceiver.INVOICING_REQ_QUEUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.model.ReportDetails;
import com.ztarmobile.invoicing.model.Response;
import com.ztarmobile.invoicing.repository.CatalogProductRepository;
import com.ztarmobile.invoicing.service.InvoicingService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jms.core.JmsTemplate;
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

    /**
     * The catalog product repository.
     */
    @Autowired
    private CatalogProductRepository catalogProductRepository;

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

        // send the request to the queue
        jmsTemplate.convertAndSend(INVOICING_REQ_QUEUE, invoicingRequest);
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
        return new Response(invoicingService.getAllAvailableRequests());
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

        // validates the parameters...
        validateCommonInput(reportFrom, reportTo, product);

        Calendar calendarFrom = null;
        Calendar calendarTo = null;

        calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(reportFrom);

        calendarTo = Calendar.getInstance();
        calendarTo.setTime(reportTo);

        List<ReportDetails> list = invoicingService.generateReport(calendarFrom, calendarTo, product);
        OutputStream outputStream = null;
        String fileName = createReportName(product, calendarFrom, calendarTo);
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            int totalDownloaded = 0;
            outputStream = response.getOutputStream();
            outputStream.write(createHeader().getBytes());
            for (ReportDetails detail : list) {
                outputStream.write(createRow(detail).getBytes());
                totalDownloaded++;
            }
            outputStream.flush();
            outputStream.close();
            log.debug("Total records downloaded: " + totalDownloaded);
        } catch (Exception e) {
            log.debug("Error while downloading the report " + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.debug("Cannot close the connection...");
                }
            }
        }
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
