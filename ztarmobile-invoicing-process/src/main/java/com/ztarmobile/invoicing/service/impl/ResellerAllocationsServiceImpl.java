/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service.impl;

import static com.ztarmobile.invoicing.common.DateUtils.createCalendarFrom;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.setMaximumCalendarDay;
import static com.ztarmobile.invoicing.common.DateUtils.setMinimumCalendarDay;
import static com.ztarmobile.invoicing.model.Phase.ALLOCATIONS;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import com.ztarmobile.invoicing.dao.LoggerDao;
import com.ztarmobile.invoicing.dao.ResellerAllocationsDao;
import com.ztarmobile.invoicing.model.LoggerReportFile;
import com.ztarmobile.invoicing.model.ResellerSubsUsage;
import com.ztarmobile.invoicing.service.AbstractDefaultService;
import com.ztarmobile.invoicing.service.ResellerAllocationsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle the operations for the allocations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
@Service
public class ResellerAllocationsServiceImpl extends AbstractDefaultService implements ResellerAllocationsService {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(ResellerAllocationsServiceImpl.class);

    /**
     * DAO dependency.
     */
    @Autowired
    private ResellerAllocationsDao resellerAllocationsDao;

    /**
     * DAO dependency for the logger process.
     */
    @Autowired
    private LoggerDao loggerDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAllocations(Date start, Date end, String product) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(start);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(end);

        // delegates to a common method.
        this.createAllAllocations(calendarStart, calendarEnd, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAllocations(Calendar start, Calendar end, String product) {
        // delegates to a common method.
        this.createAllAllocations(start, end, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAllocations(int month, String product) {
        this.createAllocations(month, month, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAllocations(int fromMonth, int toMonth, String product) {
        Calendar calendarStart = getMinimunDayOfMonth(fromMonth);
        Calendar calendarEnd = getMaximumDayOfMonth(toMonth);

        // delegates to a common method.
        this.createAllAllocations(calendarStart, calendarEnd, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAllocations(String product) {
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        this.createAllocations(previousMonth, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResellerSubsUsage> getResellerSubsUsage(Calendar start, Calendar end, String product) {
        this.validateEntries(start, end, product);

        List<ResellerSubsUsage> list;
        list = resellerAllocationsDao.getResellerSubsUsage(start.getTime(), end.getTime(), product);
        LOG.debug("Reseller subsUsage found: " + list.size());
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateResellerSubsUsage(List<ResellerSubsUsage> subscribers) {
        List<ResellerSubsUsage> alist = new ArrayList<>();
        for (ResellerSubsUsage sub : subscribers) {
            if (!sub.isUpdated()) {
                continue;
            }
            alist.add(sub);
        }
        // updates the database
        resellerAllocationsDao.updateResellerSubsUsage(alist);
    }

    /**
     * Calculates the allocations given a initial time and final time. The
     * product.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     * @param product
     *            The product.
     */
    private void createAllAllocations(Calendar calendarStart, Calendar calendarEnd, String product) {
        // validates the input.
        this.validateEntries(calendarStart, calendarEnd, product);

        // Standardize the dates.
        setMinimumCalendarDay(calendarStart);
        setMaximumCalendarDay(calendarEnd);

        Calendar calendarCurr = createCalendarFrom(calendarStart);

        Date durationStart;
        Date durationEnd;
        LOG.debug("===> Starting Allocations... ===>");
        long startTime = System.currentTimeMillis();
        while (calendarCurr.compareTo(calendarEnd) <= 0) {
            durationStart = calendarCurr.getTime();
            durationEnd = calculateDurationEnd(Calendar.getInstance(), calendarCurr);

            try {
                // test whether the file is going to be processed or not.
                boolean processed = isAllocationProcessed(product, calendarCurr.getTime());
                if (!processed) {
                    LOG.debug("Creating allocations from: " + durationStart + " - " + durationEnd);
                    resellerAllocationsDao.createAllocations(calendarCurr.getTime(), durationStart, durationEnd,
                            product);

                    // saves the file processed
                    loggerDao.saveOrUpdateReportFileProcessed(product, calendarCurr.getTime(), ALLOCATIONS, false);
                } else {
                    LOG.info("==> Allocation already processed... " + calendarCurr.getTime());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LOG.error(ex);
                // we save the error and continue with the next file.
                loggerDao.saveOrUpdateReportFileProcessed(product, calendarCurr.getTime(), ALLOCATIONS, false,
                        ex.toString());
            }
            calendarCurr.add(DAY_OF_MONTH, 1);
        }
        try {
            resellerAllocationsDao.updateAllocationIndicators();
        } catch (Exception ex) {
            calendarCurr.add(DAY_OF_MONTH, -1);
            ex.printStackTrace();
            LOG.error(ex);
            // we save the error and continue the normal flow.
            loggerDao.saveOrUpdateReportFileProcessed(product, calendarCurr.getTime(), ALLOCATIONS, false,
                    ex.toString());
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        LOG.debug("<=== Ending Allocations... <===");

        long min = MILLISECONDS.toMinutes(totalTime);
        long sec = MILLISECONDS.toSeconds(totalTime) - MINUTES.toSeconds(MILLISECONDS.toMinutes(totalTime));
        String timeMsg = String.format("%02d min, %02d sec", min, sec);
        LOG.debug("Allocations executed in: " + timeMsg);
    }

    /**
     * This method validates whether the current file is already processed or
     * not. If the file was already processed, then uncompress it so that it can
     * be used later.
     * 
     * @param product
     *            The product.
     * @param currentDate
     *            The current date.
     * @return true, it was processed, false it's not.
     */
    private boolean isAllocationProcessed(String product, Date currentDate) {
        boolean processed = false;
        if (this.isReProcess()) {
            // the process will be rerun
            return processed;
        }

        LoggerReportFile loggerReportFileVo = loggerDao.getReportFileProcessed(product, currentDate);
        if (loggerReportFileVo != null && loggerReportFileVo.getStatusAllocations() == 'C') {
            // the record was found and it was completed.
            processed = true;
        }
        return processed;
    }

    /**
     * Calculates the duration end based on the year, month and day. If
     * calendarNow and the current calendar have the same month, year and day,
     * then the calendar time is returned, otherwise the whole day is returned.
     * 
     * @param calendarNow
     *            The calendar now.
     * @param calendarCurr
     *            The current calendar.
     * @return The date.
     */
    private Date calculateDurationEnd(Calendar calendarNow, Calendar calendarCurr) {
        if (calendarNow.get(YEAR) == calendarCurr.get(YEAR) && calendarNow.get(MONTH) == calendarCurr.get(MONTH)
                && calendarNow.get(DAY_OF_MONTH) == calendarCurr.get(DAY_OF_MONTH)) {
            return calendarNow.getTime();
        } else {
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.set(calendarCurr.get(YEAR), calendarCurr.get(MONTH), calendarCurr.get(DAY_OF_MONTH),
                    calendarEnd.getActualMaximum(HOUR_OF_DAY), calendarEnd.getActualMaximum(MINUTE),
                    calendarEnd.getActualMaximum(SECOND));
            return calendarEnd.getTime();
        }
    }

}
