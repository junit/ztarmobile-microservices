/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.DateUtils.createCalendarFrom;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.setMaximumCalendarDay;
import static com.ztarmobile.invoicing.common.DateUtils.setMinimumCalendarDay;
import static com.ztarmobile.invoicing.common.FileUtils.GZIP_EXT;
import static com.ztarmobile.invoicing.common.FileUtils.copy;
import static com.ztarmobile.invoicing.common.FileUtils.executeShellCommand;
import static com.ztarmobile.invoicing.common.FileUtils.gunzipIt;
import static java.util.Calendar.MONTH;

import com.ztarmobile.invoicing.dao.LoggerDao;
import com.ztarmobile.invoicing.model.LoggerCdrFile;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Parent abstract class to handle the files for the CDR's.
 *
 * @author armandorivas
 * @since 03/02/17
 */
public abstract class AbstractCdrFileService extends AbstractDefaultService implements CdrFileService {
    /**
     * The standard file extension.
     */
    public static final String STANDARD_FILE_EXT = ".txt";
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(AbstractCdrFileService.class);

    /**
     * DAO dependency for the logger process.
     */
    @Autowired
    private LoggerDao loggerDao;

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

        setMinimumCalendarDay(calendarStart);
        setMaximumCalendarDay(calendarEnd);
        LOG.debug("Extracting files from: " + calendarStart.getTime() + " - " + calendarEnd.getTime());

        Calendar calendarNow = createCalendarFrom(calendarStart);
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
        // none of the expected files were found, we report it.
        if (!foundFileInRange) {
            String fileNotFound = getSourceDirectoryCdrFile() + File.separator + expectedFileName;
            invalidInput("The following file could not be found: " + fileNotFound);
        }
    }

    /**
     * Now, we proceed to extract the file...
     * 
     * @param currentFile
     *            The current file.
     */
    private void extractCurrentFile(File currentFile) {
        try {
            // test whether the file is going to be processed or not.
            if (isFileProcessed(currentFile)) {
                LOG.info("==> File already processed... " + currentFile);
                return;
            }

            LOG.info("==> The following file will be extracted... " + currentFile);
            // copy the current file into the extracted directory
            File targetFile = new File(getTargetDirectoryCdrFile(), currentFile.getName());
            copy(currentFile, targetFile);
            if (isFileCompressed()) {
                // ungzip it
                targetFile = gunzipIt(targetFile);
            }
            // targetFile is the name of the file after it has been uncompressed
            File processedFile = sortFile(targetFile);
            LOG.debug("Extracted file: " + processedFile);

            // we cleanup the files we don't need.
            if (isFileCompressed()) {
                File compressedFile = new File(getTargetDirectoryCdrFile(), currentFile.getName());
                if (compressedFile.exists()) {
                    if (!compressedFile.delete()) {
                        LOG.warn("This file: " + targetFile + " could not be deleted");
                    }
                }
            }
            // we make sure the file gets deleted.
            if (targetFile.exists()) {
                if (!targetFile.delete()) {
                    LOG.warn("This file: " + targetFile + " could not be deleted");
                }
            }

            // make sure that the resulting file has a valid extension like .txt
            String processedFileName = processedFile.getName();
            if (!processedFileName.endsWith(STANDARD_FILE_EXT)) {
                if (processedFileName.contains(STANDARD_FILE_EXT)) {
                    int index = processedFileName.indexOf(STANDARD_FILE_EXT);
                    String finalFileName = processedFileName.substring(0, index + STANDARD_FILE_EXT.length());
                    File finalName = new File(processedFile.getParentFile(), finalFileName);
                    if (!processedFile.renameTo(finalName)) {
                        LOG.warn("This file: " + processedFile + " could not be renamed");
                    }
                    LOG.debug("Final file: " + finalName);

                    // saves the file processed
                    loggerDao.saveOrUpdateCdrFileProcessed(currentFile.getName(), finalName.getName() + GZIP_EXT,
                            getFileType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(ex);
            // we save the error and continue with the next file.
            loggerDao.saveOrUpdateCdrFileProcessed(currentFile.getName(), "unknown", getFileType(), ex.toString());
        }
    }

    /**
     * This method validates whether the current file is already processed or
     * not. If the file was already processed, then uncompress it so that it can
     * be used later.
     * 
     * @param fileName
     *            The file name.
     * @return true, it was processed, false it's not.
     */
    private boolean isFileProcessed(File fileName) {
        boolean processed = false;
        if (this.isReProcess()) {
            // the process will be rerun
            return processed;
        }

        LoggerCdrFile loggerCdrFileVo = loggerDao.getCdrFileProcessed(fileName.getName());
        if (loggerCdrFileVo != null && loggerCdrFileVo.getStatus() == 'C') {
            // the record was found and it was completed.
            // make sure the target file is there...
            String targetFile = loggerCdrFileVo.getTargetFileName();
            File file = new File(getTargetDirectoryCdrFile(), targetFile);
            if (file.isFile() && file.exists()) {
                processed = true;
            }
            if (processed) {
                gunzipIt(file);
                if (!file.delete()) {
                    LOG.warn("This file: " + file + " could not be deleted");
                }
            }
        }
        return processed;
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

        LOG.debug("Executing command: " + sb.toString());
        // executes the shell expression to sort the file
        executeShellCommand(sb.toString());
        return expectedFileName;
    }

    /**
     * Increments the frequency of the calendar based on the type of file.
     * 
     * @param calendarNow
     *            The calendar.
     */
    private void incrementFrecuency(Calendar calendarNow) {
        calendarNow.add(getFileFrecuency(), 1);
    }

    /**
     * Gets the file extension of the CDR file.
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
     * Get the file frequency, that means, how after the cdr file is dropped in
     * the servers.
     * 
     * @return The frequency constant that must match with the Calendar
     *         constant. It could be, every month or every day or every year
     *         depending on the Calendar constant.
     */
    protected abstract int getFileFrecuency();

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

    /**
     * Get the type of the file.
     * 
     * @return The type of the file.
     */
    protected abstract char getFileType();
}
