/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service.impl;

import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static java.util.Calendar.MONTH;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ztarmobile.invoicing.dao.CatalogProductDao;
import com.ztarmobile.invoicing.service.CdrFileService;
import com.ztarmobile.invoicing.service.InvoicingService;
import com.ztarmobile.invoicing.vo.CatalogProductVo;

/**
 * Direct service implementation that calculates and perform the invoicing
 * process.
 *
 * @author armandorivas
 * @since 03/09/17
 */
@Service
public class InvoicingServiceImpl implements InvoicingService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(InvoicingServiceImpl.class);
    /**
     * DAO dependency.
     */
    @Autowired
    private CatalogProductDao catalogProductDao;

    /**
     * Injection of the specific implementation for sprint.
     */
    @Autowired
    private SprintCdrFileService sprintCdrFileService;
    /**
     * Injection of the specific implementation for ericsson.
     */
    @Autowired
    private EricssonCdrFileService ericssonCdrFileService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(Date start, Date end, String product, boolean reloadCdrFiles) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(start);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(end);

        // delegates to a common method.
        this.performAllInvoicing(calendarStart, calendarEnd, product, reloadCdrFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(Calendar start, Calendar end, String product, boolean reloadCdrFiles) {
        // delegates to a common method.
        this.performAllInvoicing(start, end, product, reloadCdrFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(int month, String product, boolean reloadCdrFiles) {
        this.performInvoicing(month, month, product, reloadCdrFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(int fromMonth, int toMonth, String product, boolean reloadCdrFiles) {
        Calendar calendarStart = getMinimunDayOfMonth(fromMonth);
        Calendar calendarEnd = getMaximumDayOfMonth(toMonth);

        // delegates to a common method.
        this.performAllInvoicing(calendarStart, calendarEnd, product, reloadCdrFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(String product, boolean reloadCdrFiles) {
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        this.performInvoicing(previousMonth, product, reloadCdrFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(String product) {
        this.performInvoicing(product, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateReport() {
    }

    /**
     * Calculates the invoicing given a initial and final time including the
     * product.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The final calendar
     * @param product
     *            The product.
     * @param reloadCdrFiles
     *            If this flag is set to true, then the cdrs files are read from
     *            the source directory, if it's false the process assumes the
     *            cdrs files are already processed.
     */
    private void performAllInvoicing(Calendar calendarStart, Calendar calendarEnd, String product,
            boolean reloadCdrFiles) {
        log.debug("Starting the invoicing process...");

        CatalogProductVo catalogProductVo = catalogProductDao.getCatalogProduct(product);
        validateInput(catalogProductVo, "No product information was found for [" + product + "]");

        log.debug("CDR files to be processed => " + (catalogProductVo.isCdma() ? "SPRINT" : "ERICSSON"));
        log.debug("Reload CDR files from its source directory ? " + reloadCdrFiles);

        if (reloadCdrFiles) {
            CdrFileService cdrFileService = catalogProductVo.isCdma() ? sprintCdrFileService : ericssonCdrFileService;
            cdrFileService.extractCdrs(calendarStart, calendarEnd);
        }

    }

}
