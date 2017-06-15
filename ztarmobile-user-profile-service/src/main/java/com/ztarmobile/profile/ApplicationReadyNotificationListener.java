/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.profile;

import static com.ztarmobile.profile.common.CommonUtils.createServiceUrl;

import com.ztarmobile.profile.model.ApplicationEmailNotification;
import com.ztarmobile.profile.notification.ApplicationStateMailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ApplicationReadyNotificationListener implements ApplicationListener<ApplicationReadyEvent> {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationReadyNotificationListener.class);

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
     * Based path.
     */
    @Value("${spring.data.rest.base-path}")
    private String basePath;
    /**
     * The server address, by default is: localhost.
     */
    @Value("${server.address:localhost}")
    private String serverAddress;
    /**
     * The server port.
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * The active profile.
     */
    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (notify) {
            // we notify when everything was OK
            ApplicationEmailNotification applicationNotif = new ApplicationEmailNotification(activeProfile);
            applicationNotif.setTo(contactDev);
            applicationNotif.setArtifact(artifact);
            applicationNotif.setDescription(description);
            applicationNotif.setVersion(version);
            applicationNotif.setName(name);
            applicationNotif.setUrl(createServiceUrl(serverAddress, serverPort, basePath));

            applicationStateMailSender.sendNotificationAtStartup(applicationNotif);
        } else {
            log.debug("Notification ApplicationReadyListener not available");
        }
    }
}
