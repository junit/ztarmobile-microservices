/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jul 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.model.SubscriberUsage;

import java.util.List;

/**
 * Performs operations over the subscriber usage data.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface SubscriberUsageDao {
    /**
     * Gets a list of all the subscribers associated to a specific bundle.
     * 
     * @param bundleId
     *            The bundle id.
     * @return List of subscribers.
     */
    List<SubscriberUsage> findUsageByBundle(String bundleId);
}
