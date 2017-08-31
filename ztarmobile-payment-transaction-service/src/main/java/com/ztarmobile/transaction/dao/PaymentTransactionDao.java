/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.dao;

import com.ztarmobile.transaction.model.SubscriberTransaction;

import java.util.Date;
import java.util.List;

/**
 * Performs operations over the subscriber CC transaction data.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface PaymentTransactionDao {

    /**
     * Gets a list of all the CC transactions by product.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     * @return List of transactions.
     */
    List<SubscriberTransaction> findCCTransactionsByProduct(Date start, Date end, String product);
}
