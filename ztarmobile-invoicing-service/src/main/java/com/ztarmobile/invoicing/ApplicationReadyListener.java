/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing;

import com.ztarmobile.invoicing.model.ApplicationEmailNotification;
import com.ztarmobile.invoicing.notification.ApplicationStateMailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Application ready listener.
 *
 * @author armandorivas
 * @since 05/05/17
 */
@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    /**
     * Dependency for the application email sender.
     */
    @Autowired
    private ApplicationStateMailSender applicationStateMailSender;

    /**
     * Build info, artifact.
     */
    @Value("${info.build.artifact}")
    private String artifact;
    /**
     * Build info, name.
     */
    @Value("${info.build.name}")
    private String name;
    /**
     * Build info, description.
     */
    @Value("${info.build.description}")
    private String description;
    /**
     * Build info, version.
     */
    @Value("${info.build.version}")
    private String version;

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
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (notify) {
            // we notify when everything was OK
            ApplicationEmailNotification applicationNotif = new ApplicationEmailNotification();
            applicationNotif.setTo(contactDev);
            applicationNotif.setArtifact(artifact);
            applicationNotif.setDescription(description);
            applicationNotif.setVersion(version);
            applicationNotif.setName(name);

            applicationStateMailSender.sendNotificationAtStartup(applicationNotif);
        }
    }
}
