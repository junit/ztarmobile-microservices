/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.service;

import com.ztarmobile.notification.model.NotificationActity;

import java.util.List;

/**
 * Service that handles the user profile management.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface BalanceNotificationService {

    List<NotificationActity> getAllAvailableActivity();

}
