/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ztarmobile.invoicing.service.CdrFileService;
import com.ztarmobile.invoicing.service.ResellerAllocationsService;
import com.ztarmobile.invoicing.service.ResellerUsageService;
import com.ztarmobile.invoicing.vo.ResellerSubsUsageVo;

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

    @Qualifier(value = "ericssonCdrFileService")
    // @Qualifier(value = "sprintCdrFileService")
    @Autowired
    private CdrFileService cdrFileService;

    @Qualifier(value = "ericssonResellerUsageService")
    // @Qualifier(value = "sprintResellerUsageService")
    @Autowired
    private ResellerUsageService resellerUsageService;

    @Test
    public void some() {
        Calendar c1 = Calendar.getInstance();
        c1.set(2016, 9, 3);
        Calendar c2 = Calendar.getInstance();
        c2.set(2016, 9, 3);
        // resellerAllocationsService.createAllocations(c1.getTime(), c2.getTime(), "REALMOBILE");
        // resellerAllocationsService.createAllocations(Calendar.FEBRUARY, "REALMOBILE");
        // resellerAllocationsService.createAllocations(Calendar.JANUARY, Calendar.MARCH, "REALMOBILE");
        // resellerAllocationsService.createAllocations("REALMOBILE");
        // resellerAllocationsService.createAllocations("");
        // cdrFileServiceService.extractCdrs();
        // cdrFileService.extractCdrs(c1, c2);
        // resellerUsageService.createUsage();
        // resellerUsageService.createUsage(c1, c2);
        List<ResellerSubsUsageVo> s = resellerAllocationsService.getResellerSubsUsage(c1, c2, "REALMOBILE");
        log.debug(s);
    }
}
