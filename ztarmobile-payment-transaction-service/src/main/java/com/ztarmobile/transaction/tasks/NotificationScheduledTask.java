/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.tasks;

import com.ztarmobile.transaction.model.SubscriberTransaction;
import com.ztarmobile.transaction.service.PaymentTransactionService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Performs operations to generate the transaction report.
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
    private PaymentTransactionService paymentTransactionService;

    /**
     * This method processes the payment transaction notification.
     */
    @Scheduled(cron = "${notification.cron.activity}")
    public void scheduleUsageNotification() {
        log.debug(">> Requesting payment transaction notification...");

        try {
            List<SubscriberTransaction> list = paymentTransactionService.getAllPaymentActivity();
            paymentTransactionService.performNotification(list);
        } catch (Throwable ex) {
            log.error("An error occured while sending the notification due to: " + ex.toString());
            ex.printStackTrace();
        }
        log.debug("<< Ending payment transactions notification...");
    }
}
