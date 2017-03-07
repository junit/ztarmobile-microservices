/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static java.util.Calendar.MONTH;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Parent abstract class to handle the usage for the cdrs.
 *
 * @author armandorivas
 * @since 03/06/17
 */
public abstract class AbstractResellerUsageService implements ResellerUsageService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(AbstractResellerUsageService.class);
    /**
     * The file extension.
     */
    public static final String EXTRACTED_FILE_EXT = ".txt";

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(Date start, Date end) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(start);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(end);

        // delegates to a common method.
        this.createAllUsageCdrs(calendarStart, calendarEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(Calendar start, Calendar end) {
        // delegates to a common method.
        this.createAllUsageCdrs(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(int fromMonth, int toMonth) {
        Calendar calendarStart = getMinimunDayOfMonth(fromMonth);
        Calendar calendarEnd = getMaximumDayOfMonth(toMonth);

        // delegates to a common method.
        this.createAllUsageCdrs(calendarStart, calendarEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(int month) {
        this.createUsage(month, month);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage() {
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        this.createUsage(previousMonth);
    }

    /**
     * Extracts all the files and move them into the target directory.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     */
    private void createAllUsageCdrs(Calendar calendarStart, Calendar calendarEnd) {
        validateInput(calendarStart, "calendarStart must be not null");
        validateInput(calendarEnd, "calendarStart must be not null");

        if (calendarStart.after(calendarEnd)) {
            // making sure the start date is not greater than the end date.
            invalidInput("The Start date cannot be greater than the end date: startDate -> " + calendarStart.getTime()
                    + ", endDate -> " + calendarEnd.getTime());
        }
        File file = new File(getTargetDirectoryCdrFile());
        if (!(file.exists() && file.isDirectory())) {
            invalidInput("Cannot proceed further..., the target directory cannot be read: " + file);
        }
        log.debug("Calculating usage from: " + calendarStart.getTime() + " - " + calendarEnd.getTime());

        Calendar calendarNow = calendarStart;
        String expectedFileName = null;
        File[] files = file.listFiles(createFileNameFilter());
        Arrays.sort(files); // make sure the files are ordered lexicographically

        boolean foundFileInRange = false;
        for (File currentFile : files) {
            // the expected file name ...
            expectedFileName = getExpectedFileName(calendarNow);

            if (expectedFileName.equals(currentFile.getName())) {
                foundFileInRange = true;
            } else {
                if (foundFileInRange) {
                    // the process can't continue because a file is missing...
                    String fileNotFound = null;
                    invalidInput("This file could not be found: " + fileNotFound);
                }
            }

            if (foundFileInRange) {
                // process currentFile
                // extractCurrentFile(currentFile);
                log.debug(currentFile);
                incrementFrecuency(calendarNow);
                if (calendarNow.after(calendarEnd)) {
                    // no more files to process, the process is done
                    break;
                }
            }
        }
    }

    /**
     * Creates the file name filter.
     * 
     * @return The file name filter.
     */
    private FilenameFilter createFileNameFilter() {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(EXTRACTED_FILE_EXT);
            }
        };
    }

    /**
     * Gets the expected file name.
     * 
     * @param calendarNow
     *            The calendar.
     * @return The full file name.
     */
    protected abstract String getExpectedFileName(Calendar calendarNow);

    /**
     * Gets the target directory where the cdrs files are located.
     * 
     * @return The directory of the cdrs target files.
     */
    protected abstract String getTargetDirectoryCdrFile();

    /**
     * Increments the frecuency of the calendar based on the type of file.
     * 
     * @param calendarNow
     *            The calendar.
     */
    protected abstract void incrementFrecuency(Calendar calendarNow);

}
