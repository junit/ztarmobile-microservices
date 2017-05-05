/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing;

import com.ztarmobile.invoicing.notification.ApplicationStateMailSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Application.
 *
 * @author armandorivas
 * @since 03/27/17
 */
@SpringBootApplication
@EnableAsync
@EnableJms
@EnableScheduling
public class Application {
    /**
     * Logger for this class.
     */
    private final Logger log = LoggerFactory.getLogger(Application.class);

    /**
     * Dependency to send notifications.
     */
    @Autowired
    private ApplicationStateMailSender applicationStateMailSender;

    /**
     * We just print a reminder message to state that we are using a profile.
     * 
     * @return The spring bean.
     */
    @Bean
    CommandLineRunner values() {
        return args -> {
            log.debug("*** [**");
            System.out.println("sdsdsdsddsdsdsds");
        };
    }

    /**
     * Starts the service.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        // Launch the application
        SpringApplication app = new SpringApplication(Application.class);
        app.addListeners(new ApplicationListener<ApplicationReadyEvent>() {

            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                System.out.println("I'm ready");

            }

        });
        app.run(args);
    }
}
