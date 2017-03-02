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
import java.io.FilenameFilter;
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

        sb.append("ztar-711_data_dump_");
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
    protected FilenameFilter createFileNameFilter() {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // valid ericsson files.
                if (name.endsWith(FILE_EXT)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
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
}
