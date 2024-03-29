/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.profile;

import static com.ztarmobile.profile.common.CommonUtils.createServiceUrl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.context.request.RequestAttributes;

/**
 * Main Application.
 *
 * @author armandorivas
 * @since 03/27/17
 */
@SpringBootApplication
@ImportResource("classpath*:**/*-dao.xml")
public class Application {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(Application.class);

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
     * Based path.
     */
    @Value("${spring.data.rest.base-path}")
    private String basePath;
    /**
     * The server address.
     */
    @Value("${server.address}")
    private String serverAddress;
    /**
     * The server port.
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * We just print a reminder message to state that we are using a profile.
     * 
     * @return The spring bean.
     */
    @Bean
    CommandLineRunner values() {
        return args -> {
            String url = createServiceUrl(serverAddress, serverPort, basePath);

            log.debug("*** [" + applicationName + "] Running under profile: " + activeProfile + " ***");
            log.debug("Build Info:");
            log.debug("\tArtifact: " + artifact);
            log.debug("\tName: " + name);
            log.debug("\tDescription: " + description);
            log.debug("\tVersion: " + version);
            log.debug("URL: " + url);
        };
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                    boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                // Removing those properties that we don't need.
                errorAttributes.remove("exception");
                errorAttributes.remove("path");
                return errorAttributes;
            }
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
        app.run(args);
    }
}
