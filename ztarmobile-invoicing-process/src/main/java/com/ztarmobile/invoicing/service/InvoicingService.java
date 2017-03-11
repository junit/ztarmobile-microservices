/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import java.util.Calendar;
import java.util.Date;

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
     * @param reloadCdrFiles
     *            If this flag is set to true, then the cdrs files are read from
     *            the source directory, if it's false the process assumes the
     *            cdrs files are already processed.
     * @see InvoicingService#performInvoicing(Calendar, Calendar, String,
     *      boolean)
     */
    void performInvoicing(Date start, Date end, String product, boolean reloadCdrFiles);

    /**
     * Perform the invoicing process based on a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @param reloadCdrFiles
     *            If this flag is set to true, then the cdrs files are read from
     *            the source directory, if it's false the process assumes the
     *            cdrs files are already processed.
     * @see InvoicingService#performInvoicing(Date, Date, String, boolean)
     */
    void performInvoicing(Calendar start, Calendar end, String product, boolean reloadCdrFiles);

    /**
     * Perform the invoicing process based on a specific month and a product.
     * 
     * @param month
     *            Value of the {@link java.util.Calendar} field indicating the
     *            third month of the year in the Gregorian and Julian calendars.
     * @param product
     *            The product.
     * @param reloadCdrFiles
     *            If this flag is set to true, then the cdrs files are read from
     *            the source directory, if it's false the process assumes the
     *            cdrs files are already processed.
     * @see InvoicingService#performInvoicing(int, int, String, boolean)
     */
    void performInvoicing(int month, String product, boolean reloadCdrFiles);

    /**
     * Perform the invoicing process based on a initial month and an end month.
     * 
     * @param fromMonth
     *            The initial month.
     * @param toMonth
     *            The end month.
     * @param product
     *            The product.
     * @param reloadCdrFiles
     *            If this flag is set to true, then the cdrs files are read from
     *            the source directory, if it's false the process assumes the
     *            cdrs files are already processed.
     * @see InvoicingService#performInvoicing(int, String, boolean)
     */
    void performInvoicing(int fromMonth, int toMonth, String product, boolean reloadCdrFiles);

    /**
     * Perform the invoicing process based on a specific product.
     * 
     * @param product
     *            The product.
     * @param reloadCdrFiles
     *            If this flag is set to true, then the cdrs files are read from
     *            the source directory, if it's false the process assumes the
     *            cdrs files are already processed.
     */
    void performInvoicing(String product, boolean reloadCdrFiles);

    /**
     * Perform the invoicing process based on a specific product. The cdrs files
     * are forzed to be read from the source folder.
     * 
     * @param product
     *            The product.
     */
    void performInvoicing(String product);

    void generateReport();
}