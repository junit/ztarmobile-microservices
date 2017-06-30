/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.service;

import java.util.Set;

/**
 * Service that handles the balance notification.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface BalanceNotificationService {
    /**
     * Gets all MDN with recent activity.
     * 
     * @return All the MDN's
     */
    Set<String> getAllAvailableActivity();

    /**
     * Performs the notification to the customer.
     * 
     * @param mdn
     *            The MDN to be notified.
     */
    void performNotification(String mdn);

}
