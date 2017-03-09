/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddFormat;
import static java.util.Calendar.DAY_OF_MONTH;

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
public class EricssonResellerUsageService extends AbstractResellerUsageService {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(EricssonResellerUsageService.class);

    /**
     * The file starts with...
     */
    final String SUFIX_FILE_NAME = "_call_adj_data_dump";

    /**
     * Reference to the extracted directory of the Ericsson cdrs.
     */
    @Value("${cdrs.extracted.ericsson.dir}")
    private String targetEricssonCdrs;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExpectedFileName(Calendar calendarNow) {
        StringBuilder sb = new StringBuilder();
        String initDateString = fromDateToYYYYmmddFormat(calendarNow.getTime());

        sb.append(initDateString);
        sb.append(SUFIX_FILE_NAME);
        sb.append(EXTRACTED_FILE_EXT);

        return sb.toString();
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
    protected boolean hasHeader() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processCurrentLine(String[] sln) {
        // get usage values from specific locations
        String type = sln[0];
        String callType = sln[13];
        String callDate = sln[getCallDateFieldPositionAt()];
        String mdn = sln[getMdnFieldPositionAt()];

        if (callType.equals("SMS")) {

        } else if (callType.equals("MMS")) {

        } else if (callType.equals("VOICE")) {

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getCallDateFieldPositionAt() {
        return 11;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMdnFieldPositionAt() {
        return 2;
    }
}
