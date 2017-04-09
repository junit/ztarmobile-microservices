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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ztarmobile.invoicing.model.CatalogProduct;
import com.ztarmobile.invoicing.model.LoggerRequest;
import com.ztarmobile.invoicing.model.ReportDetails;
import com.ztarmobile.invoicing.service.InvoicingService;

/**
 *
 * @author armandorivas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoicingServiceTest {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(InvoicingServiceTest.class);

    @Autowired
    private InvoicingService invoicingService;

    @Test
    public void performInvoicing() {
        LOG.debug("Starting the test...");
        String product1 = "GOOD2GOUS";
        String product2 = "GOOD2GOUS-CDMA";
        Calendar c1 = Calendar.getInstance();
        c1.set(2017, 0, 1);
        Calendar c2 = Calendar.getInstance();
        c2.set(2017, 0, 31);

        long init = System.currentTimeMillis();

        invoicingService.performInvoicing(c1, c2, product2, true);
        invoicingService.performInvoicing(c1, c2, product1, true);

        long end = System.currentTimeMillis();
        long total = end - init;
        System.out.println("Milliseconds: " + total);

        List<ReportDetails> list = invoicingService.generateReport(product1, c1, c2);
        for (ReportDetails vo : list) {
            System.out.println(vo);
        }
        System.out.println(list.size());

        for (CatalogProduct vo : invoicingService.getAllAvailableProducts()) {
            System.out.println(vo);
        }

        for (LoggerRequest vo : invoicingService.getAllAvailableRequests()) {
            System.out.println(vo);
        }
    }
}
