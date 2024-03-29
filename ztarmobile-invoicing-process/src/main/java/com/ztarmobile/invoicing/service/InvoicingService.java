/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import com.ztarmobile.invoicing.model.CatalogEmail;
import com.ztarmobile.invoicing.model.CatalogProduct;
import com.ztarmobile.invoicing.model.EmailProductNotification;
import com.ztarmobile.invoicing.model.LoggerRequest;
import com.ztarmobile.invoicing.model.ReportDetails;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service that calculates and perform the invoicing process.
 *
 * @author armandorivas
 * @since 03/09/17
 */
public interface InvoicingService {
    /**
     * Perform the invoicing process based on a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @param rerunInvoicing
     *            If this flag is set to true, then the invoice process is run
     *            from scratch processing the CDR's and calculating all, if it's
     *            false the process runs as usual.
     * @see InvoicingService#performInvoicing(Calendar, Calendar, String,
     *      boolean)
     */
    void performInvoicing(Date start, Date end, String product, boolean rerunInvoicing);

    /**
     * Perform the invoicing process based on a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @param rerunInvoicing
     *            If this flag is set to true, then the invoice process is run
     *            from scratch processing the CDR's and calculating all, if it's
     *            false the process runs as usual.
     * @see InvoicingService#performInvoicing(Date, Date, String, boolean)
     */
    void performInvoicing(Calendar start, Calendar end, String product, boolean rerunInvoicing);

    /**
     * Perform the invoicing process based on a specific month and a product.
     * 
     * @param month
     *            Value of the {@link java.util.Calendar} field indicating the
     *            third month of the year in the Gregorian and Julian calendars.
     * @param product
     *            The product.
     * @param rerunInvoicing
     *            If this flag is set to true, then the invoice process is run
     *            from scratch processing the CDR's and calculating all, if it's
     *            false the process runs as usual.
     * @see InvoicingService#performInvoicing(int, int, String, boolean)
     */
    void performInvoicing(int month, String product, boolean rerunInvoicing);

    /**
     * Perform the invoicing process based on a initial month and an end month.
     * 
     * @param fromMonth
     *            The initial month.
     * @param toMonth
     *            The end month.
     * @param product
     *            The product.
     * @param rerunInvoicing
     *            If this flag is set to true, then the invoice process is run
     *            from scratch processing the CDR's and calculating all, if it's
     *            false the process runs as usual.
     * @see InvoicingService#performInvoicing(int, String, boolean)
     */
    void performInvoicing(int fromMonth, int toMonth, String product, boolean rerunInvoicing);

    /**
     * Perform the invoicing process based on a specific product. The process
     * will consider the previous month.
     * 
     * @param product
     *            The product.
     * @param rerunInvoicing
     *            If this flag is set to true, then the invoice process is run
     *            from scratch processing the CDRS's and calculating all, if
     *            it's false the process runs as usual.
     */
    void performInvoicing(String product, boolean rerunInvoicing);

    /**
     * Perform the invoicing process based on a specific product. The invoicing
     * process is started from scratch.
     * 
     * @param product
     *            The product.
     */
    void performInvoicing(String product);

    /**
     * Return a list of objects for the invoicing report.
     * 
     * @param start
     *            Initial date.
     * @param end
     *            End date.
     * @param product
     *            The product.
     * @return List of objects or null if no information was found.
     * @see InvoicingService#generateReport(Date, Date,String)
     */
    List<ReportDetails> generateReport(Calendar start, Calendar end, String product);

    /**
     * Return a list of objects for the invoicing report.
     * 
     * 
     * @param start
     *            Initial date.
     * @param end
     *            End date.
     * @param product
     *            The product.
     * @return List of objects or null if no information was found.
     * @see InvoicingService#generateReport(Calendar, Calendar,String)
     */
    List<ReportDetails> generateReport(Date start, Date end, String product);

    /**
     * Gets all the existing products.
     * 
     * @return The list of all the products or an empty list where no products
     *         were found.
     */
    List<CatalogProduct> getAllAvailableProducts();

    /**
     * Gets all the existing email's.
     * 
     * @return The list of all the email's or an empty list where no email's
     *         were found.
     */
    List<CatalogEmail> getAllAvailableEmails();

    /**
     * Gets all the products by email.
     * 
     * @param email
     *            The email.
     * @return The list of all the products by email or an empty list where no
     *         products were found.
     */
    List<EmailProductNotification> getAllProductsByEmail(CatalogEmail email);

    /**
     * Gets all the existing invoicing requests.
     * 
     * @return The list of all the requests or an empty list where no request
     *         were found.
     */
    List<LoggerRequest> getAllAvailableRequests();
}
