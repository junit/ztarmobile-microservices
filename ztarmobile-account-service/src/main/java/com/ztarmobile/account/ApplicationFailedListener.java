/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.account;

import com.ztarmobile.account.model.ApplicationEmailNotification;
import com.ztarmobile.account.notification.ApplicationStateMailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Application failed listener.
 *
 * @author armandorivas
 * @since 05/05/17
 */
@Component
public class ApplicationFailedListener implements ApplicationListener<ApplicationFailedEvent> {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationFailedListener.class);

    /**
     * Dependency for the application email sender.
     */
    @Autowired
    private ApplicationStateMailSender applicationStateMailSender;

    /**
     * Build info, contact dev.
     */
    @Value("${info.contact-dev}")
    private String contactDev;

    /**
     * Build info, notify dev.
     */
    @Value("${info.notify-dev}")
    private boolean notify;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        if (notify) {
            // we notify when there's an error at startup
            ApplicationEmailNotification notification = new ApplicationEmailNotification(false, event.getException());
            notification.setTo(contactDev);
            applicationStateMailSender.sendNotificationAtStartup(notification);
        } else {
            log.debug("Notification ApplicationFailedListener not available");
        }
    }
}
