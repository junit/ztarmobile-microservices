/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ztarmobile.invoicing.service.InvoicingService;
import java.util.Calendar;

/**
 *
 * @author armandorivas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/ztarmobile/invoicing/springframework/applicationContext.xml",
        "classpath*:com/ztarmobile/invoicing/springframework/**/*-config.xml",
        "classpath*:com/ztarmobile/invoicing/**/*-dao.xml" })
public class InvoicingServiceTest {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(InvoicingServiceTest.class);

    @Autowired
    private InvoicingService invoicingService;

    @Test
    public void performInvoicing() {
        Calendar c1 = Calendar.getInstance();
        c1.set(2016, 9, 3);
        Calendar c2 = Calendar.getInstance();
        c2.set(2016, 9, 3);
        
        
        invoicingService.performInvoicing(c1, c2, "GOOD2GOUS", true);
        //invoicingService.performInvoicing(c1, c2, "GOOD2GOUS-CDMA", true);
        
    }
}