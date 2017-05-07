/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
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
     * The active profile.
     */
    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * The application name.
     */
    @Value("${spring.application.name}")
    private String applicationName;

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
     * We just print a reminder message to state that we are using a profile.
     * 
     * @return The spring bean.
     */
    @Bean
    CommandLineRunner values() {
        return args -> {
            log.debug("*** [" + applicationName + "] Running under profile: " + activeProfile + " ***");
            log.debug("Build Info:");
            log.debug("\tArtifact: " + artifact);
            log.debug("\tName: " + name);
            log.debug("\tDescription: " + description);
            log.debug("\tVersion: " + version);
        };
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the
        // message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
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
        app.run(args);
    }
}