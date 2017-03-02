/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.CommonUtils.getMinimunDayOfMonth;
import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.LAST_HOUR;
import static com.ztarmobile.invoicing.common.DateUtils.LAST_MIN_SEC;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ztarmobile.invoicing.dao.ResellerAllocationsDao;

/**
 * Service to handle the operations for the allocations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
@Service
public class ResellerAllocationsServiceImpl implements ResellerAllocationsService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(ResellerAllocationsServiceImpl.class);

    /**
     * DAO dependency.
     */
    @Autowired
    private ResellerAllocationsDao resellerAllocationsDao;

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
        validateInput(calendarStart, "calendarStart must be not null");
        validateInput(calendarEnd, "calendarStart must be not null");

        if (calendarStart.after(calendarEnd)) {
            // making sure the start date is not greater than the end date.
            invalidInput("The Start date cannot be greater than the end date: startDate -> " + calendarStart.getTime()
                    + ", endDate -> " + calendarEnd.getTime());
        }
        // at least one product needs to be passed.
        validateInput(product, "At least one product must be provided");

        // the start date must start at 00:00:00
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);

        // we get the current time.
        Calendar calendarNow = Calendar.getInstance();

        // Based on the current time, we calculate the hour, min and sec to set
        // the end date.
        if (calendarEnd.compareTo(calendarNow) >= 0) {
            calendarEnd = calendarNow;
        } else {
            calendarEnd.set(Calendar.HOUR_OF_DAY, LAST_HOUR);
            calendarEnd.set(Calendar.MINUTE, LAST_MIN_SEC);
            calendarEnd.set(Calendar.SECOND, LAST_MIN_SEC);
        }

        Calendar calendarCurr = calendarStart;

        Date durationStart;
        Date durationEnd;
        log.debug("===> Starting Allocations... ===>");
        long startTime = System.currentTimeMillis();
        while (calendarCurr.compareTo(calendarEnd) <= 0) {
            durationStart = calendarCurr.getTime();
            durationEnd = calculateDurationEnd(calendarNow, calendarCurr);

            log.debug("Collecting mdn usage from: " + durationStart + " - " + durationEnd);

            resellerAllocationsDao.createAllocations(calendarCurr.getTime(), durationStart, durationEnd, product);
            calendarCurr.add(DAY_OF_MONTH, 1);
        }
        resellerAllocationsDao.updateAllocationIndicators();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.debug("<=== Ending Allocations... <===");

        long min = MILLISECONDS.toMinutes(totalTime);
        long sec = MILLISECONDS.toSeconds(totalTime) - MINUTES.toSeconds(MILLISECONDS.toMinutes(totalTime));
        String timeMsg = String.format("%02d min, %02d sec", min, sec);
        log.debug("Allocations executed in: " + timeMsg);
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
            calendarEnd.set(calendarCurr.get(YEAR), calendarCurr.get(MONTH), calendarCurr.get(DAY_OF_MONTH), LAST_HOUR,
                    LAST_MIN_SEC, LAST_MIN_SEC);
            return calendarEnd.getTime();
        }
    }

}
