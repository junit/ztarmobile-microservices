/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmFormat;
import static java.util.Calendar.MONTH;

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
public class SprintResellerUsageService extends AbstractResellerUsageService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(SprintResellerUsageService.class);

    /**
     * Reference to the extracted directory of the Sprint cdrs.
     */
    @Value("${cdrs.extracted.sprint.dir}")
    private String targetSprintCdrs;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExpectedFileName(Calendar calendarNow) {
        StringBuilder sb = new StringBuilder();
        String dateString = fromDateToYYYYmmFormat(calendarNow.getTime());

        sb.append("dwh_cdr_");
        sb.append(dateString);
        sb.append(EXTRACTED_FILE_EXT);

        return sb.toString();
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
}
