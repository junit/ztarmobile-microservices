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
import static com.ztarmobile.invoicing.common.DateUtils.splitTimeByMonth;
import static com.ztarmobile.invoicing.model.LoggerStatus.COMPLETED;
import static com.ztarmobile.invoicing.model.LoggerStatus.ERROR;
import static com.ztarmobile.invoicing.model.LoggerStatus.PROGRESS;
import static java.util.Calendar.MONTH;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ztarmobile.invoicing.common.MontlyTime;
import com.ztarmobile.invoicing.dao.CatalogProductDao;
import com.ztarmobile.invoicing.dao.InvoicingDao;
import com.ztarmobile.invoicing.dao.LoggerDao;
import com.ztarmobile.invoicing.model.CatalogProduct;
import com.ztarmobile.invoicing.model.LoggerRequest;
import com.ztarmobile.invoicing.model.ReportDetails;
import com.ztarmobile.invoicing.service.AbstractDefaultService;
import com.ztarmobile.invoicing.service.CdrFileService;
import com.ztarmobile.invoicing.service.InvoicingService;
import com.ztarmobile.invoicing.service.ResellerAllocationsService;
import com.ztarmobile.invoicing.service.ResellerUsageService;

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
    private static final Logger LOG = Logger.getLogger(InvoicingServiceImpl.class);

    /**
     * DAO dependency for the invoicing process.
     */
    @Autowired
    private InvoicingDao invoicingDao;

    /**
     * DAO dependency for the product.
     */
    @Autowired
    private CatalogProductDao catalogProductDao;

    /**
     * DAO dependency for the logger process.
     */
    @Autowired
    private LoggerDao loggerDao;

    /**
     * Injection of the specific implementation for sprint.
     */
    @Autowired
    private SprintCdrFileService sprintCdrFileService;
    /**
     * Injection of the specific implementation for Ericsson.
     */
    @Autowired
    private EricssonCdrFileService ericssonCdrFileService;

    /**
     * Injection of the service to create the allocations.
     */
    @Autowired
    private ResellerAllocationsService allocationsService;

    /**
     * Injection of the specific implementation for sprint.
     */
    @Autowired
    private SprintResellerUsageService sprintUsageService;
    /**
     * Injection of the specific implementation for Ericsson.
     */
    @Autowired
    private EricssonResellerUsageService ericssonUsageService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(Date start, Date end, String product, boolean rerunInvoicing) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(start);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(end);

        // delegates to a common method.
        this.performAllInvoicing(calendarStart, calendarEnd, product, rerunInvoicing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(Calendar start, Calendar end, String product, boolean rerunInvoicing) {
        // delegates to a common method.
        this.performAllInvoicing(start, end, product, rerunInvoicing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(int month, String product, boolean rerunInvoicing) {
        this.performInvoicing(month, month, product, rerunInvoicing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(int fromMonth, int toMonth, String product, boolean rerunInvoicing) {
        Calendar calendarStart = getMinimunDayOfMonth(fromMonth);
        Calendar calendarEnd = getMaximumDayOfMonth(toMonth);

        // delegates to a common method.
        this.performAllInvoicing(calendarStart, calendarEnd, product, rerunInvoicing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInvoicing(String product, boolean rerunInvoicing) {
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        this.performInvoicing(previousMonth, product, rerunInvoicing);
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
    public List<ReportDetails> generateReport(String product, Calendar start, Calendar end) {
        validateInput(start, "calendarStart must be not null");
        validateInput(end, "calendarEnd must be not null");
        // delegates to the overloaded method.
        return this.generateReport(product, start.getTime(), end.getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportDetails> generateReport(String product, Date start, Date end) {
        return invoicingDao.generateReport(product, start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CatalogProduct> getAllAvailableProducts() {
        // gets all the available products.
        return catalogProductDao.getCatalogProduct();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LoggerRequest> getAllAvailableRequests() {
        // gets all the available requests.
        return loggerDao.getInvoiceProcessed(500);
    }

    /**
     * Calculates the invoicing given a initial and final time including the
     * product.
     * 
     * @param start
     *            The initial calendar.
     * @param end
     *            The final calendar
     * @param product
     *            The product.
     * @param rerunInvoicing
     *            If this flag is set to true, then the invoice process is run
     *            from scratch processing the CDR's and calculating all, if it's
     *            false the process runs as usual.
     */
    private void performAllInvoicing(Calendar start, Calendar end, String product, boolean rerunInvoicing) {
        LOG.debug("Starting the invoicing process...rerun process? " + rerunInvoicing);

        long id = 0;
        try {
            if (!loggerDao.isInvoiceInStatus(PROGRESS)) {
                // save the record before staring the process.
                id = loggerDao.saveOrUpdateInvoiceProcessed(id, product, start.getTime(), end.getTime(), 0, PROGRESS);

                long startTime = System.currentTimeMillis();
                CatalogProduct catalogProductVo = catalogProductDao.getCatalogProduct(product);
                validateInput(catalogProductVo, "No product information was found for [" + product + "]");
                LOG.debug("CDR files to be processed => " + (catalogProductVo.isCdma() ? "SPRINT" : "ERICSSON"));

                LOG.debug("==================> 0. preparing data <==================================");
                CdrFileService cdrFileService = catalogProductVo.isCdma() ? sprintCdrFileService
                        : ericssonCdrFileService;
                ((AbstractDefaultService) cdrFileService).setReProcess(rerunInvoicing);
                cdrFileService.extractCdrs(start, end);

                LOG.debug("==================> 1. create_reseller_allocations <=====================");
                ((AbstractDefaultService) allocationsService).setReProcess(rerunInvoicing);
                allocationsService.createAllocations(start, end, product);

                LOG.debug("==================> 2. create_reseller_usage <=====================");
                ResellerUsageService usageService = catalogProductVo.isCdma() ? sprintUsageService
                        : ericssonUsageService;
                ((AbstractDefaultService) usageService).setReProcess(rerunInvoicing);
                usageService.createUsage(start, end, product);

                LOG.debug("==================> 3. create_invoicing_details <=====================");
                // finally, create the data so that we can use later
                this.createInvoicingDetails(start, end, product);
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;

                // save the record after it has been completed.
                loggerDao.saveOrUpdateInvoiceProcessed(id, product, start.getTime(), end.getTime(), totalTime,
                        COMPLETED);
            } else {
                LOG.warn("There's one or more invocie requests in " + PROGRESS + " status...");
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            LOG.fatal(ex);
            // we save the error...
            loggerDao.saveOrUpdateInvoiceProcessed(id, product, start.getTime(), end.getTime(), 0, ERROR,
                    ex.toString());
            // we re throw the exception
            throw ex;
        }
    }

    /**
     * Creates the necessary information so that is can be used later.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     */
    private void createInvoicingDetails(Calendar start, Calendar end, String product) {
        // we remove old data so that we can override with new info.
        invoicingDao.cleanUpInvoicing(start.getTime(), end.getTime(), product);
        // by default, if more than one month is requested, the information is
        // grouped monthly
        boolean splitByMonth = true;
        LOG.debug("Split by month: " + splitByMonth);
        if (splitByMonth) {
            // calculates the intervals.
            List<MontlyTime> intervals = splitTimeByMonth(start, end);
            if (intervals != null) {
                for (MontlyTime montlyTime : intervals) {
                    // month by month
                    invoicingDao.saveInvoicing(montlyTime.getStart().getTime(), montlyTime.getEnd().getTime(), product);
                }
            } else {
                LOG.warn("There's no invoicing to be processed");
            }
        } else {
            invoicingDao.saveInvoicing(start.getTime(), end.getTime(), product);
        }
    }

}
