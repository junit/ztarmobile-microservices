/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmFormat;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Sprint implementation to handle the files for the cdrs.
 *
 * @author armandorivas
 * @since 03/02/17
 */
@Service
public class SprintCdrFileProcessor extends AbstractCdrFileProcessor {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(SprintCdrFileProcessor.class);
    /**
     * The file extension.
     */
    public static final String FILE_EXT = ".txt";
    /**
     * Reference to the directory of the Sprint cdrs.
     */
    @Value("${cdrs.source.sprint.dir}")
    private String sourceSprintCdrs;
    /**
     * Reference to the extracted directory of the Sprint cdrs.
     */
    @Value("${cdrs.extracted.sprint.dir}")
    private String targetSprintCdrs;

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
        String dateString = fromDateToYYYYmmFormat(calendarNow.getTime());

        sb.append("dwh_cdr_");
        sb.append(dateString);
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
                // valid sprint files.
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
        return sourceSprintCdrs;
    }

    @Override
    protected String getTargetDirectoryCdrFile() {
        return targetSprintCdrs;
    }

}
