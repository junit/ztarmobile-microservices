/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * Main Application.
 *
 * @author armandorivas
 * @since 03/27/17
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ImportResource({ "classpath:com/ztarmobile/invoicing/springframework/applicationContext.xml",
        "classpath*:com/ztarmobile/invoicing/springframework/**/*-config.xml",
        "classpath*:com/ztarmobile/invoicing/**/*-dao.xml" })

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
