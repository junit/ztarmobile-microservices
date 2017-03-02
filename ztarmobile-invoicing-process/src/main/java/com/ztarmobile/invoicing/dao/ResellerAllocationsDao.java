/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.Date;

/**
 * DAO to handle the operations for the allocations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public interface ResellerAllocationsDao {

    /**
     * Creates the allocations.
     * 
     * @param callDate
     *            The call date.
     * @param durationStart
     *            The duration start.
     * @param durationEnd
     *            The duration end.
     * @param product
     *            The product.
     */
    void createAllocations(Date callDate, Date durationStart, Date durationEnd, String product);

    /**
     * Updates the indicators after the allocation process is done.
     */
    void updateAllocationIndicators();
}
