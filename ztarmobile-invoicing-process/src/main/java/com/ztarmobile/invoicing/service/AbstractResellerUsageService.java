/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;
import static com.ztarmobile.invoicing.common.CommonUtils.validateInput;
import static com.ztarmobile.invoicing.common.DateUtils.createCalendarFrom;
import static com.ztarmobile.invoicing.common.DateUtils.fromStringToYYmmddHHmmssFormat;
import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.setMaximumCalendarDay;
import static com.ztarmobile.invoicing.common.DateUtils.setMinimumCalendarDay;
import static com.ztarmobile.invoicing.common.FileUtils.zipIt;
import static com.ztarmobile.invoicing.model.Phase.USAGE;
import static java.util.Calendar.MONTH;

import com.ztarmobile.invoicing.dao.LoggerDao;
import com.ztarmobile.invoicing.model.LoggerReportFile;
import com.ztarmobile.invoicing.model.ResellerSubsUsage;
import com.ztarmobile.invoicing.model.Usage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Parent abstract class to handle the usage for the CDR's.
 *
 * @author armandorivas
 * @since 03/06/17
 */
public abstract class AbstractResellerUsageService extends AbstractDefaultService implements ResellerUsageService {
    /**
     * The file extension.
     */
    public static final String EXTRACTED_FILE_EXT = ".txt";
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(AbstractResellerUsageService.class);

    /**
     * Service dependency.
     */
    @Autowired
    private ResellerAllocationsService allocationsService;

    /**
     * DAO dependency for the logger process.
     */
    @Autowired
    private LoggerDao loggerDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(Date start, Date end, String product) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(start);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(end);

        // delegates to a common method.
        this.createAllUsageCdrs(calendarStart, calendarEnd, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(Calendar start, Calendar end, String product) {
        // delegates to a common method.
        this.createAllUsageCdrs(start, end, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(int fromMonth, int toMonth, String product) {
        Calendar calendarStart = getMinimunDayOfMonth(fromMonth);
        Calendar calendarEnd = getMaximumDayOfMonth(toMonth);

        // delegates to a common method.
        this.createAllUsageCdrs(calendarStart, calendarEnd, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(int month, String product) {
        this.createUsage(month, month, product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUsage(String product) {
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        this.createUsage(previousMonth, product);
    }

    /**
     * Extracts all the files and move them into the target directory.
     * 
     * @param calendarStart
     *            The initial calendar.
     * @param calendarEnd
     *            The end calendar.
     * @param product
     *            The product.
     * @param subscribers
     *            The list of subscribers.
     */
    private void createAllUsageCdrs(Calendar calendarStart, Calendar calendarEnd, String product) {
        // finds all the subscribers for this product
        List<ResellerSubsUsage> subs = allocationsService.getResellerSubsUsage(calendarStart, calendarEnd, product);
        validateInput(subs.isEmpty(), "No subcribers were found, the usage cannot be calculated for [" + product + "]");

        File file = new File(getTargetDirectoryCdrFile());
        this.validateEntries(calendarStart, calendarEnd, file, product);

        setMinimumCalendarDay(calendarStart);
        setMaximumCalendarDay(calendarEnd);
        LOG.debug("Calculating usage from: " + calendarStart.getTime() + " - " + calendarEnd.getTime());

        Calendar calendarNow = createCalendarFrom(calendarStart);
        String expectedFileName;
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
                    String fileNotFound = getTargetDirectoryCdrFile() + File.separator + expectedFileName;
                    invalidInput("This file could not be found: " + fileNotFound);
                }
            }

            if (foundFileInRange) {
                try {
                    // test whether the file is going to be processed or not.
                    boolean processed = isUsageProcessed(product, calendarNow.getTime());
                    if (!processed) {
                        // 1. calculate the usage in the current file.
                        calculateUsagePerFile(currentFile, calendarStart.getTime(), calendarEnd.getTime(), subs);

                        // 2. updates the usage.
                        allocationsService.updateResellerSubsUsage(subs);

                        // 3. finally, we compress the file (close it out)
                        zipIt(currentFile);

                        // saves the file processed
                        loggerDao.saveOrUpdateReportFileProcessed(product, calendarNow.getTime(), USAGE,
                                getFileFrecuency() == MONTH);
                    } else {
                        LOG.info("==> Usage already processed... " + currentFile);
                        // 3. finally, we compress the file (close it out)
                        // this is necessary to finish the process.
                        zipIt(currentFile);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOG.error(ex);
                    // we save the error and continue with the next file.
                    loggerDao.saveOrUpdateReportFileProcessed(product, calendarNow.getTime(), USAGE,
                            getFileFrecuency() == MONTH, ex.toString());
                }
                incrementFrecuency(calendarNow);
                if (calendarNow.after(calendarEnd)) {
                    // no more files to process, the process is done
                    break;
                }
            }
        }
    }

    private boolean isUsageProcessed(String product, Date currentDate) {
        boolean processed = false;
        if (this.isReProcess()) {
            // the process will be rerun
            return processed;
        }

        LoggerReportFile loggerReportFileVo = loggerDao.getReportFileProcessed(product, currentDate);
        if (loggerReportFileVo != null && loggerReportFileVo.getStatusUsage() == 'C') {
            // the record was found and it was completed.
            processed = true;
        }
        return processed;
    }

    private void calculateUsagePerFile(File currentFile, Date startDate, Date endDate, List<ResellerSubsUsage> subs) {
        LOG.debug("==> The following file will be read... " + currentFile);

        // resetting the list of subscribers
        for (ResellerSubsUsage vo : subs) {
            if (vo.isUpdated()) {
                vo.setUpdated(false);
            }
        }

        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
            if (hasHeader()) {
                // ignores the first line
                reader.readLine();
            }

            String lastMdn = null;
            String lastCallDate = null;
            List<ResellerSubsUsage> usgList = new ArrayList<>();
            long linecnt = 0, mdnUpdCnt = 0;

            // read the rest of the files
            while ((line = reader.readLine()) != null) {
                linecnt++;
                if (line.isEmpty()) {
                    // some how we got a blank row... skip it.
                    continue;
                }
                // tokenize the line
                String[] sln = line.split("\\|");

                // we get the info from each line
                String callDate = sln[getCallDateFieldPositionAt()];
                String mdn = sln[getMdnFieldPositionAt()];

                Date cdt = fromStringToYYmmddHHmmssFormat(callDate);
                if (cdt.before(startDate) || cdt.after(endDate)) {
                    // call date is either before the start-date or
                    // is after the end date.
                    // either way, skip it.
                    continue;
                }

                // calculate the usage
                Usage usage = calculateIndividualUsage(sln);

                // the usage list is null. That means we reached this MDN
                // for the first time. This row is a new MDN.
                if (lastMdn == null || lastCallDate == null || !lastMdn.equals(mdn)
                        || !lastCallDate.substring(0, 8).equals(callDate.substring(0, 8))) {
                    /*
                     * We have the MDN and call date. Let's look in the
                     * subscribers list, if this MDN shows up for this
                     * call-date. There can be more than one entries (if the
                     * rate plan was changed on that day). So, get all the
                     * entries for the subscriber i.e. MDN for the call-date.
                     */
                    usgList = getUsageByMdnAndCallDate(subs, mdn, callDate.substring(0, 8));
                }
                // check for the specific subscriber and plan when
                // record was created and add usage
                for (ResellerSubsUsage rms : usgList) {
                    // we know the usage list is for the MDN, and the call date.
                    // match the call date and time to fit in the rate plan
                    // duration start and end.

                    if (rms.isTimeInRange(callDate)) {
                        rms.setActualMou(rms.getActualMou() + usage.getMou());
                        rms.setActualKbs(rms.getActualKbs() + usage.getKbs());
                        rms.setActualSms(rms.getActualSms() + usage.getSms());
                        rms.setActualMms(rms.getActualMms() + usage.getMms());
                        rms.setUpdated(true); // dirty flag
                        break;
                    }
                }
                // capture this MDN for next iteration.
                lastMdn = mdn;
                lastCallDate = callDate;
                mdnUpdCnt++;
            }
            LOG.info("Lines read #: " + linecnt + ", processed mdns #:" + mdnUpdCnt);
        } catch (IOException ex) {
            invalidInput("There was a problem while reading: " + currentFile + " due to: " + ex);
        }
    }

    /**
     * Get all usage rows that match MDN, and the call date.
     * 
     * @param subs
     *            The list of subscribers.
     * @param mdn
     *            The MDN.
     * @param callDate
     *            The callDate in format YYYYmmdd
     * @return The list containing matching objects.
     */
    private List<ResellerSubsUsage> getUsageByMdnAndCallDate(List<ResellerSubsUsage> subs, String mdn,
            String callDate) {
        List<ResellerSubsUsage> subList = new ArrayList<>();

        for (ResellerSubsUsage sub : subs) {
            if (!sub.isEqualsByMdnAndCallDate(mdn, callDate)) {
                // skip all list entries where MDN is does not match
                continue;
            }
            // found the matching MDN and callDate
            // Initialize all actual values. This is to avoid doubling up in
            // case of accidental duplicate runs..
            sub.setActualKbs(0);
            sub.setActualMms(0);
            sub.setActualMou(0);
            sub.setActualSms(0);

            // add the same object from the subscribers list to the sublist.
            subList.add(sub);
        }
        return subList;
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
     * Gets the expected file name.
     * 
     * @param calendarNow
     *            The calendar.
     * @return The full file name.
     */
    protected abstract String getExpectedFileName(Calendar calendarNow);

    /**
     * Gets the target directory where the CDR's files are located.
     * 
     * @return The directory of the CDR's target files.
     */
    protected abstract String getTargetDirectoryCdrFile();

    /**
     * Get the file frequency, that means, how after the CDR file is dropped in
     * the servers.
     * 
     * @return The frequency constant that must match with the Calendar
     *         constant. It could be, every month or every day or every year
     *         depending on the Calendar constant.
     */
    protected abstract int getFileFrecuency();

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
     * @return The usage for each line.
     */
    protected abstract Usage calculateIndividualUsage(String[] sln);

    /**
     * Gets the position of the call date in a CDR file.
     * 
     * @return the position in a row.
     */
    protected abstract int getCallDateFieldPositionAt();

    /**
     * Gets the position of the MDN field in a CDR file.
     * 
     * @return the position in a row.
     */
    protected abstract int getMdnFieldPositionAt();

}
