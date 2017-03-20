/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * Parent abstract class to handle common operations related to the invoicing.
 *
 * @author armandorivas
 * @since 03/07/17
 */
public abstract class AbstractDefaultService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(AbstractDefaultService.class);
    /**
     * Flag to reprocess the current task.
     */
    private boolean reProcess;

    /**
     * Validate the common input.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     * @param sourceDirectory
     *            The source directory.
     * @param product
     *            The product.
     */
    protected void validateEntries(Calendar calendarStart, Calendar calendarEnd, File sourceDirectory, String product) {
        log.debug("Validating reseller input start, end date with source and product");
        validateEntries(calendarStart, calendarEnd, sourceDirectory);
        validateInput(product, "At least one product must be provided");
    }

    /**
     * Validate the common input.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     * @param product
     *            The product.
     */
    protected void validateEntries(Calendar calendarStart, Calendar calendarEnd, String product) {
        log.debug("Validating reseller input start, end date with product");
        validateCalendar(calendarStart, calendarEnd);
        validateInput(product, "At least one product must be provided");
    }

    /**
     * Validate the common input.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     * @param sourceDirectory
     *            The source directory.
     */
    protected void validateEntries(Calendar calendarStart, Calendar calendarEnd, File sourceDirectory) {
        log.debug("Validating reseller input start, end date with source");
        validateCalendar(calendarStart, calendarEnd);
        validateInput(sourceDirectory, "The source direcotry cannot be null");

        if (!(sourceDirectory.exists() && sourceDirectory.isDirectory())) {
            invalidInput("Cannot proceed further..., the source directory cannot be read: " + sourceDirectory);
        }
    }

    /**
     * Validate the common input.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     * 
     */
    private void validateCalendar(Calendar calendarStart, Calendar calendarEnd) {
        validateInput(calendarStart, "calendarStart must be not null");
        validateInput(calendarEnd, "calendarEnd must be not null");

        if (calendarStart.after(calendarEnd)) {
            // making sure the start date is not greater than the end date.
            invalidInput("The Start date cannot be greater than the end date: startDate -> " + calendarStart.getTime()
                    + ", endDate -> " + calendarEnd.getTime());
        }
    }

    /**
     * Creates the file name filter based on a specific file extension.
     * 
     * @param fileExtension
     *            The file extension.
     * @return The file name filter.
     */
    protected FilenameFilter createFileNameFilter(String fileExtension) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(fileExtension);
            }
        };
    }

    /**
     * @return the reProcess
     */
    public boolean isReProcess() {
        return reProcess;
    }

    /**
     * @param reProcess
     *            the reProcess to set
     */
    public void setReProcess(boolean reProcess) {
        this.reProcess = reProcess;
    }
}
