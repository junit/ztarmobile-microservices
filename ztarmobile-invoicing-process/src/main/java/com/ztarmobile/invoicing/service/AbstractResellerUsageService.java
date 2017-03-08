/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.DateUtils.createCalendarFrom;
import static com.ztarmobile.invoicing.common.DateUtils.fromStringToYYmmddHHmmssFormat;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static java.util.Calendar.MONTH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
public abstract class AbstractResellerUsageService extends AbstractService implements ResellerUsageService {
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
        File file = new File(getTargetDirectoryCdrFile());
        this.validateEntries(calendarStart, calendarEnd, file);

        log.debug("Calculating usage from: " + calendarStart.getTime() + " - " + calendarEnd.getTime());

        Calendar calendarNow = createCalendarFrom(calendarStart);
        String expectedFileName = null;
        File[] files = file.listFiles(createFileNameFilter(EXTRACTED_FILE_EXT));
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
                parseCurrentFile(currentFile, calendarStart.getTime(), calendarNow.getTime(), calendarEnd.getTime());
                incrementFrecuency(calendarNow);
                if (calendarNow.after(calendarEnd)) {
                    // no more files to process, the process is done
                    break;
                }
            }
        }
    }

    private void parseCurrentFile(File currentFile, Date startDate, Date nowDate, Date endDate) {
        log.info("==> The following file will be read... " + currentFile);
        log.info("start. " + startDate);
        log.info("now. " + nowDate);
        log.info("end. " + endDate);
        log.info("==> The following file will be read... " + currentFile);
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
            if (hasHeader()) {
                reader.readLine();// ignores the first line
            }
            // read the rest of the files
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    // some how we got a blank row... skip it.
                    continue;
                }
                // tokenize the line
                String[] sln = line.split("\\|");
                String callDate = sln[getCallDateFieldPositionAt()];
                Date cdt = fromStringToYYmmddHHmmssFormat(callDate);

                if (cdt.before(startDate) || cdt.after(endDate)) {
                    // call date is either before the start-date or
                    // is after the end date.
                    // either way, skip it.
                    continue;
                }
                processCurrentLine(sln);
            }
        } catch (IOException ex) {
            invalidInput("There was a problem while reading: " + currentFile + " due to: " + ex);
        }
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

    /**
     * The file contains a header as part of its content?
     * 
     * @return true, has header, otherwise false.
     */
    protected abstract boolean hasHeader();

    /**
     * Process the current line.
     * 
     * @param sln
     *            The current line.
     */
    protected abstract void processCurrentLine(String[] sln);

    /**
     * Gets the position of the call date in a cdr file.
     * 
     * @return the position in a row.
     */
    protected abstract int getCallDateFieldPositionAt();

    /**
     * Gets the position of the mdn field in a cdr file.
     * 
     * @return the position in a row.
     */
    protected abstract int getMdnFieldPositionAt();

}
