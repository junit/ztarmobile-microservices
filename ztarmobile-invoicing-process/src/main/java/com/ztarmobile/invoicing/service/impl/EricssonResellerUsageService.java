/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service.impl;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddFormat;
import static java.util.Calendar.DAY_OF_MONTH;

import com.ztarmobile.invoicing.model.Usage;
import com.ztarmobile.invoicing.service.AbstractResellerUsageService;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Ericsson implementation to handle the files for the CDR's.
 *
 * @author armandorivas
 * @since 03/02/17
 */
@Service
public class EricssonResellerUsageService extends AbstractResellerUsageService {
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(EricssonResellerUsageService.class);

    /**
     * The file starts with...
     */
    private final String SUFIX_FILE_NAME = "_call_adj_data_dump";

    /**
     * Reference to the extracted directory of the Ericsson CDR's.
     */
    @Value("${invoicing.cdrs.extracted.ericsson.dir}")
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

        LOG.debug("Expected file name: " + sb);
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
    protected int getFileFrecuency() {
        return DAY_OF_MONTH; // files are dropped every day.
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
    protected Usage calculateIndividualUsage(String[] sln) {
        Usage usage = new Usage();

        // get usage values from specific locations
        String callType = sln[13];
        String kbs = sln[17];

        switch (callType) {
        case "SMS":
            usage.setSms(1);
            break;
        case "MMS":
            usage.setMms(1);
            break;
        case "VOICE":
            // *** NEW LOGIG
            // we need to exclude call forward to voice email from the
            // chargeable minutes, as it should not be chargeable…. We are not
            // charged by ATT either.
            if (sln.length == 29 && sln[10].equals("Forwarding") && sln[28].equals("VM_DEP")) {
                usage.setMou(0);
            } else {
                usage.setMou(Float.parseFloat(sln[14]));
            }
            break;
        case "GPRS":
            usage.setKbs(Float.parseFloat(kbs));
            break;
        default:
            break;
        }
        return usage;
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
