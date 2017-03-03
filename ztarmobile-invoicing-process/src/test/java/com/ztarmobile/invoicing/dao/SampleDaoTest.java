/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import com.ztarmobile.invoicing.service.CdrFileProcessorService;
import com.ztarmobile.invoicing.service.ResellerAllocationsService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

/**
 *
 * @author armandorivas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/ztarmobile/invoicing/springframework/applicationContext.xml",
        "classpath*:com/ztarmobile/invoicing/springframework/**/*-config.xml",
        "classpath*:com/ztarmobile/invoicing/**/*-dao.xml" })
public class SampleDaoTest {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(SampleDaoTest.class);

    @Autowired
    private ResellerAllocationsService resellerAllocationsService;

    @Qualifier(value = "ericssonCdrFileProcessor")
    //@Qualifier(value = "sprintCdrFileProcessor")
    @Autowired
    private CdrFileProcessorService cdrFileProcessorService;
    
    @Test
    public void some() {
        Calendar c1 = Calendar.getInstance();
        c1.set(2016, 10, 1);
        Calendar c2 = Calendar.getInstance();
        c2.set(2017, 1, 1);
        //resellerAllocationsService.createAllocations(c1.getTime(), c2.getTime(), "REALMOBILE");
        //resellerAllocationsService.createAllocations(Calendar.FEBRUARY, "REALMOBILE");
        //resellerAllocationsService.createAllocations(Calendar.JANUARY, Calendar.MARCH, "REALMOBILE");
        //resellerAllocationsService.createAllocations("REALMOBILE");
        //resellerAllocationsService.createAllocations("");
        //cdrFileProcessorService.extractCdrs();
        cdrFileProcessorService.extractCdrs(c1, c2);
    }
}
