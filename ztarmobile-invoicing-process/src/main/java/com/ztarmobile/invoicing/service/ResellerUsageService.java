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
 * Service to handle the usage files for the CDR's.
 *
 * @author armandorivas
 * @since 03/06/17
 */
public interface ResellerUsageService {
    /**
     * This method creates the usage given a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     */
    void createUsage(Date start, Date end, String product);

    /**
     * This method creates the usage given a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     */
    void createUsage(Calendar start, Calendar end, String product);

    /**
     * This method creates the usage given an initial and end month. This
     * process works only in the same year.
     * 
     * @param fromMonth
     *            The initial month.
     * @param toMonth
     *            The final month.
     * @param product
     *            The product.
     */
    void createUsage(int fromMonth, int toMonth, String product);

    /**
     * This method creates the usage for a particular month. This process works
     * only in the same year.
     * 
     * @param month
     *            An specific month.
     * @param product
     *            The product.
     */
    void createUsage(int month, String product);

    /**
     * This method creates the usage. This process works only in the same year
     * and the previous month.
     * 
     * @param product
     *            The product.
     */
    void createUsage(String product);
}
