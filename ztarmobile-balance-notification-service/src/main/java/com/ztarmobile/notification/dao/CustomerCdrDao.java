/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.dao;

import com.ztarmobile.notification.model.NotificationActity;

import java.util.List;

/**
 * Performs operations over the CDR tables.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface CustomerCdrDao {
    /**
     * Retrieves all user activity.
     * 
     * @return The user activity,
     */
    List<NotificationActity> fetchAllMdnActivityAir();

    /**
     * Retrieves all user activity.
     * 
     * @return The user activity,
     */
    List<NotificationActity> fetchAllMdnActivityData();
}
