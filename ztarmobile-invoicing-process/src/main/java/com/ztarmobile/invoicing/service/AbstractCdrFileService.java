/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static com.ztarmobile.invoicing.common.FileUtils.copy;
import static com.ztarmobile.invoicing.common.FileUtils.executeShellCommand;
import static com.ztarmobile.invoicing.common.FileUtils.gunzipIt;
import static java.util.Calendar.MONTH;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Parent abstract class to handle the files for the cdrs.
 *
 * @author armandorivas
 * @since 03/02/17
 */
public abstract class AbstractCdrFileService extends AbstractService implements CdrFileService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(AbstractCdrFileService.class);
    /**
     * The standard file extension.
     */
    public static final String STANDARD_FILE_EXT = ".txt";

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
        File file = new File(getSourceDirectoryCdrFile());
        this.validateEntries(calendarStart, calendarEnd, file);

        log.debug("Extracting files from: " + calendarStart.getTime() + " - " + calendarEnd.getTime());

        Calendar calendarNow = calendarStart;
        String expectedFileName = null;
        File[] files = file.listFiles(createFileNameFilter(getFileExtension()));
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
                    String fileNotFound = getSourceDirectoryCdrFile() + File.separator + expectedFileName;
                    invalidInput("This file could not be found: " + fileNotFound);
                }
            }

            if (foundFileInRange) {
                // process currentFile
                extractCurrentFile(currentFile);
                incrementFrecuency(calendarNow);
                if (calendarNow.after(calendarEnd)) {
                    // no more files to process, the process is done
                    break;
                }
            }
        }
    }

    /**
     * Now, we proceed to extract the file...
     * 
     * @param currentFile
     *            The current file.
     */
    private void extractCurrentFile(File currentFile) {
        log.info("==> The following file will be extracted... " + currentFile);
        // copy the current file into the extracted directory
        File targetFile = new File(getTargetDirectoryCdrFile(), currentFile.getName());
        copy(currentFile, targetFile);
        if (isFileCompressed()) {
            // ungzip it
            targetFile = gunzipIt(targetFile);
        }
        // targetFile is the name of the file after it has been decompressed
        File processedFile = sortFile(targetFile);
        log.debug("Extracted file: " + processedFile);

        // we cleanup the files we dont need.
        if (isFileCompressed()) {
            File compressedFile = new File(getTargetDirectoryCdrFile(), currentFile.getName());
            if (compressedFile.exists()) {
                compressedFile.delete();
            }
        }
        if (targetFile.exists()) {
            targetFile.delete();
        }

        // make sure that the resulting file has a valid extension like .txt
        String processedFileName = processedFile.getName();
        if (!processedFileName.endsWith(STANDARD_FILE_EXT)) {
            if (processedFileName.contains(STANDARD_FILE_EXT)) {
                int index = processedFileName.indexOf(STANDARD_FILE_EXT);
                String finalFileName = processedFileName.substring(0, index + STANDARD_FILE_EXT.length());
                File finalName = new File(processedFile.getParentFile(), finalFileName);
                processedFile.renameTo(finalName);
                log.debug("Final file: " + finalName);
            }
        }
    }

    /**
     * Sort the existing content of the file depending on business rules.
     * 
     * @param file
     *            The file to be sorted.
     * @return The resulting location of the file.
     */
    private File sortFile(File file) {
        // the file after it has been sorted.
        File expectedFileName = new File(file.getParentFile(), getSortedFileName(file.getName()));

        StringBuilder sb = new StringBuilder(getSortShellExpression(file));
        sb.append(" ");
        sb.append(">");
        sb.append(" ");
        sb.append(expectedFileName);

        log.debug("Executing command: " + sb.toString());
        // executes the shell expression to sort the file
        executeShellCommand(sb.toString());
        return expectedFileName;
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

    /**
     * Increments the frecuency of the calendar based on the type of file.
     * 
     * @param calendarNow
     *            The calendar.
     */
    protected abstract void incrementFrecuency(Calendar calendarNow);

    /**
     * Indicates whether the source file is compressed or not.
     * 
     * @return true, it's compressed, false it's not.
     */
    protected abstract boolean isFileCompressed();

    /**
     * Gets the name of the sorted file name.
     * 
     * @param originalFileName
     *            The original name.
     * @return The sorted file name.
     */
    protected abstract String getSortedFileName(String originalFileName);

    /**
     * Gets the shell expression based on the file to be sorted.
     * 
     * @param fileTobeSorted
     *            The sorted file.
     * @return The shell expression.
     */
    protected abstract String getSortShellExpression(File fileTobeSorted);

}
