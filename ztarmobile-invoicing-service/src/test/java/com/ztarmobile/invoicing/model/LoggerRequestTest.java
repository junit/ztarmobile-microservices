/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, April 2017.
 */
package com.ztarmobile.invoicing.model;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test for {@link com.ztarmobile.invoicing.model.LoggerRequest}
 * 
 * @author armandorivas
 */
@RunWith(SpringRunner.class)
public class LoggerRequestTest {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(LoggerRequestTest.class);

    @Test
    public void setterAndGetters() {
        log.debug("Starting the test for..." + getClass());
        LoggerRequest loggerRequest = new LoggerRequest();
        loggerRequest.setRowId(4);
        Assert.assertEquals(4, loggerRequest.getRowId());

        loggerRequest.setProduct("DATAWIND");
        Assert.assertEquals("DATAWIND", loggerRequest.getProduct());
    }
}
