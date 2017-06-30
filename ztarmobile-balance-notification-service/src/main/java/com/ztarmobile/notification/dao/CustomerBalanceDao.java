/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.model.CustomerBalance;

/**
 * Performs operations over the customer balance table.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface CustomerBalanceDao {
    /**
     * Updates the balances for the customer.
     * 
     * @param customerBalance
     *            The customer balance.
     */
    void updateBalances(CustomerBalance customerBalance);

    /**
     * Counts the total of records given an MDN and the customer balance info.
     * 
     * @param mdn
     *            The MDN.
     * @param customerBalance
     *            The customer balance.
     * @return Total of records found.
     */
    int countCustomerBalance(String mdn, CustomerBalance customerBalance);
}
