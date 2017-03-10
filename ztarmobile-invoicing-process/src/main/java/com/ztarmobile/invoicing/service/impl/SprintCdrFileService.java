/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service.impl;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmFormat;
import static java.util.Calendar.MONTH;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ztarmobile.invoicing.service.AbstractCdrFileService;

/**
 * Sprint implementation to handle the files for the cdrs.
 *
 * @author armandorivas
 * @since 03/02/17
 */
@Service
public class SprintCdrFileService extends AbstractCdrFileService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(SprintCdrFileService.class);
    /**
     * The file extension.
     */
    private static final String FILE_EXT = ".txt";
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
        StringBuilder sb = new StringBuilder("dwh_cdr_");
        sb.append(fromDateToYYYYmmFormat(calendarNow.getTime()));
        sb.append(FILE_EXT);

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSourceDirectoryCdrFile() {
        return sourceSprintCdrs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTargetDirectoryCdrFile() {
        return targetSprintCdrs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void incrementFrecuency(Calendar calendarNow) {
        calendarNow.add(MONTH, 1); // files are dropped every month.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileCompressed() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSortedFileName(String originalFileName) {
        return originalFileName + ".sorted";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getSortShellExpression(File fileTobeSorted) {
        // sort the file by phn_num, and call date. (index starts at 1)
        return "sort -t \"|\" -k 5,5n -k 6,6n " + fileTobeSorted;
    }

}
