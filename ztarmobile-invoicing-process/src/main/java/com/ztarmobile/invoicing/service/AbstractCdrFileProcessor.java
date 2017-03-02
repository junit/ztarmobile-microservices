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
import static java.util.Calendar.MONTH;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Parent abstract class to handle the files for the cdrs.
 *
 * @author armandorivas
 * @since 03/02/17
 */
public abstract class AbstractCdrFileProcessor implements CdrFileProcessorService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(AbstractCdrFileProcessor.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void extractCdrs(Date start, Date end) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(start);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(end);

        // delegates to a common method.
        this.extractAllCdrs(calendarStart, calendarEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extractCdrs(Calendar start, Calendar end) {
        // delegates to a common method.
        this.extractAllCdrs(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extractCdrs(int fromMonth, int toMonth) {
        Calendar calendarStart = getMinimunDayOfMonth(fromMonth);
        Calendar calendarEnd = getMaximumDayOfMonth(toMonth);

        // delegates to a common method.
        this.extractAllCdrs(calendarStart, calendarEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extractCdrs(int month) {
        this.extractCdrs(month, month);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extractCdrs() {
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        this.extractCdrs(previousMonth);
    }

    /**
     * Extracts all the files and move them into the target directory.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     */
    private void extractAllCdrs(Calendar calendarStart, Calendar calendarEnd) {
        validateInput(calendarStart, "calendarStart must be not null");
        validateInput(calendarEnd, "calendarStart must be not null");

        if (calendarStart.after(calendarEnd)) {
            // making sure the start date is not greater than the end date.
            invalidInput("The Start date cannot be greater than the end date: startDate -> " + calendarStart.getTime()
                    + ", endDate -> " + calendarEnd.getTime());
        }
        File file = new File(getSourceDirectoryCdrFile());
        if (!(file.exists() && file.isDirectory())) {
            invalidInput("Cannot proceed further..., the ericsson directory cannot be read: " + file);
        }

        Calendar calendarNow = calendarStart;
        String expectedFileName = null;
        for (File currentFile : file.listFiles(createFileNameFilter())) {
            log.debug(currentFile);
            expectedFileName = getExpectedFileName(calendarNow);
            log.debug(expectedFileName);
            // ztar-711_data_dump_20170221_20170223.txt
        }
    }

    /**
     * Gets the file extension of the cdr file.
     * 
     * @return The file extension.
     */
    protected abstract String getFileExtension();

    /**
     * Gets the expected file name.
     * 
     * @param calendarNow
     *            The calendar.
     * @return The full file name.
     */
    protected abstract String getExpectedFileName(Calendar calendarNow);

    /**
     * Creates the file name filter.
     * 
     * @return The file name filter.
     */
    protected abstract FilenameFilter createFileNameFilter();

    /**
     * Gets the source directory where the cdrs files are located.
     * 
     * @return The directory of the cdrs source files.
     */
    protected abstract String getSourceDirectoryCdrFile();

    /**
     * Gets the target directory where the cdrs files are located.
     * 
     * @return The directory of the cdrs target files.
     */
    protected abstract String getTargetDirectoryCdrFile();
}
