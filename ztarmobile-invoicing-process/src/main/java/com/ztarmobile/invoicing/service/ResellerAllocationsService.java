/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ztarmobile.invoicing.vo.ResellerSubsUsageVo;

/**
 * Service to handle the operations for the allocations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public interface ResellerAllocationsService {
    /**
     * Creates the allocations based on a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @see ResellerAllocationsService#createAllocations(Calendar, Calendar,
     *      String)
     */
    void createAllocations(Date start, Date end, String product);

    /**
     * Creates the allocations based on a start and end date.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @see ResellerAllocationsService#createAllocations(Date, Date, String)
     */
    void createAllocations(Calendar start, Calendar end, String product);

    /**
     * Creates the allocations based on a specific month.
     * 
     * @param month
     *            Value of the {@link java.util.Calendar} field indicating the
     *            third month of the year in the Gregorian and Julian calendars.
     * @param product
     *            The product.
     * @see ResellerAllocationsService#createAllocations(int, int, String)
     */
    void createAllocations(int month, String product);

    /**
     * Creates the allocations based on a initial month and an end month.
     * 
     * @param fromMonth
     *            The initial month.
     * @param toMonth
     *            The end month.
     * @param product
     *            The product.
     * @see ResellerAllocationsService#createAllocations(int, String)
     */
    void createAllocations(int fromMonth, int toMonth, String product);

    /**
     * Creates the allocations based on the previous month depending on when
     * this process is run.
     * 
     * @param product
     *            The product.
     * @see ResellerAllocationsService#createAllocations(int, String)
     */
    void createAllocations(String product);

    /**
     * Gets a list of reseller subscribers.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @return List of subscribers.
     */
    List<ResellerSubsUsageVo> getResellerSubsUsage(Calendar start, Calendar end, String product);
}
