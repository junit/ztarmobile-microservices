/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.service;

import com.ztarmobile.transaction.model.SubscriberTransaction;

import java.util.List;

/**
 * Service that handles the payment transaction notification.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface PaymentTransactionService {
    /**
     * Gets all CC transaction activity for subscribers.
     * 
     * @return All subscriber activity.
     */
    List<SubscriberTransaction> getAllPaymentActivity();

    /**
     * Performs the notification.
     * 
     * @param list
     *            The list of subscribers.
     */
    void performNotification(List<SubscriberTransaction> list);

}
