/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jul 2017.
 */
package com.ztarmobile.notification.tasks;

import com.ztarmobile.notification.model.SubscriberUsage;
import com.ztarmobile.notification.service.UsageNotificationService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Performs operations to generate the usage report.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Component
public class NotificationScheduledTask {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduledTask.class);

    /**
     * Dependency of the usage notification service.
     */
    @Autowired
    private UsageNotificationService usageNotificationService;

    /**
     * The bundle Id.
     */
    @Value("${notification.bundle-id}")
    private String bundleId;

    /**
     * This method processes the usage notification.
     */
    @Scheduled(cron = "${notification.cron.activity}")
    public void scheduleUsageNotification() {
        log.debug(">> Requesting usage notification...");

        List<SubscriberUsage> list = usageNotificationService.getAllSubscriberActivityByBundle(bundleId);
        if (!list.isEmpty()) {
            usageNotificationService.performNotification(list);
        } else {
            log.warn("Subscriber Usage was not found");
        }
        log.debug("<< Ending notifications...");
    }
}
