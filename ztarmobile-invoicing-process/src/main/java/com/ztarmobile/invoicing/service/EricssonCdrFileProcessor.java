/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddFormat;
import static java.util.Calendar.DAY_OF_MONTH;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Ericsson implementation to handle the files for the cdrs.
 *
 * @author armandorivas
 * @since 03/02/17
 */
@Service
public class EricssonCdrFileProcessor extends AbstractCdrFileProcessor {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(EricssonCdrFileProcessor.class);
    /**
     * The file extension.
     */
    public static final String FILE_EXT = ".txt.gz";
    /**
     * The file starts with...
     */
    final String PREFIX_FILE_NAME = "ztar-711_data_dump_";
    /**
     * Reference to the directory of the Ericsson cdrs.
     */
    @Value("${cdrs.source.ericsson.dir}")
    private String sourceEricssonCdrs;
    /**
     * Reference to the extracted directory of the Ericsson cdrs.
     */
    @Value("${cdrs.extracted.ericsson.dir}")
    private String targetEricssonCdrs;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFileExtension() {
        return FILE_EXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExpectedFileName(Calendar calendarNow) {
        StringBuilder sb = new StringBuilder();
        String initDateString = fromDateToYYYYmmddFormat(calendarNow.getTime());

        Calendar calendarNext = Calendar.getInstance();
        calendarNext.setTimeInMillis(calendarNow.getTimeInMillis());
        calendarNext.add(DAY_OF_MONTH, 2);

        String endDateString = fromDateToYYYYmmddFormat(calendarNext.getTime());

        sb.append(PREFIX_FILE_NAME);
        sb.append(initDateString);
        sb.append("_");
        sb.append(endDateString);
        sb.append(FILE_EXT);

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSourceDirectoryCdrFile() {
        return sourceEricssonCdrs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTargetDirectoryCdrFile() {
        return targetEricssonCdrs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void incrementFrecuency(Calendar calendarNow) {
        calendarNow.add(DAY_OF_MONTH, 1); // files are dropped every day.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileCompressed() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSortedFileName(String originalFileName) {
        String[] parts = originalFileName.split("_");
        String callDataFileName = parts[3] + "_call_adj_data_dump.txt.sorted";
        return callDataFileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSortShellExpression(File fileTobeSorted) {
        return "egrep '^C' " + fileTobeSorted + " | LC_ALL=C sort -t \"|\" -k 3,3n -k 12,12n";
    }
}
