/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.jms;

import com.ztarmobile.notification.service.BalanceNotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Receives all the messages from the JMS.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Component
public class NotificationReceiver {
    /**
     * The queue name.
     */
    public static final String NOTIFICATION_REQ_QUEUE = "notification.requests";

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(NotificationReceiver.class);

    /**
     * Dependency of the balance notification service.
     */
    @Autowired
    private BalanceNotificationService balanceNotificationService;

    /**
     * This method receives the request from the queue and process it.
     * 
     * @param mdn
     *            The MDN.
     */
    @JmsListener(destination = NOTIFICATION_REQ_QUEUE, containerFactory = "myFactory")
    public void receiveMessage(String mdn) {
        log.debug("=====| Receiving request: " + mdn);

        try {
            // performs the notification stuff.
            balanceNotificationService.performNotification(mdn);
        } catch (Throwable ex) {
            log.debug("Request did not finish correctly :( ");
            // we log the error...
            ex.printStackTrace();
        }
    }
}
