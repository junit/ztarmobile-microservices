/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jul 2017.
 */
package com.ztarmobile.notification.service;

import com.ztarmobile.notification.model.SubscriberUsage;

import java.util.List;

/**
 * Service that handles the usage notification.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface UsageNotificationService {
    /**
     * Gets all usage subscribers associated to a specific bundle.
     * 
     * @param bundleId
     * 
     * @return All subscriber activity.
     */
    List<SubscriberUsage> getAllSubscriberActivityByBundle(String bundleId);

    /**
     * Performs the notification to the customer.
     * 
     * @param list
     *            The list of subscribers.
     */
    void performNotification(List<SubscriberUsage> list);

}
