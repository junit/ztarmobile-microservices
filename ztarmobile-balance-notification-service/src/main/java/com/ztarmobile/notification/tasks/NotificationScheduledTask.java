/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ztarmobile.notification.tasks;

import com.ztarmobile.notification.service.BalanceNotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author armandorivasarzaluz
 */
@Component
public class NotificationScheduledTask {
    /**
     * The queue name.
     */
    public static final String NOTIFICATION_REQ_QUEUE = "notification.requests";

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduledTask.class);

    /**
     * The JSM template.
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Dependency of the balance notification service.
     */
    @Autowired
    private BalanceNotificationService balanceNotificationService;

    /**
     * This method receives the request from the queue and process it.
     */
    @Scheduled(cron = "${notification.cron.activity}")
    public void scheduleBalanceNotification() {
        log.debug("Requesting balance notification...");

        for (String mdn : balanceNotificationService.getAllAvailableActivity()) {
            log.debug("Processing: " + mdn);

            // send the request to the queue
            jmsTemplate.convertAndSend(NOTIFICATION_REQ_QUEUE, mdn);
        }
        log.debug("Ending notifications...");
    }
}
