/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service.impl;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmFormat;
import static java.util.Calendar.MONTH;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ztarmobile.invoicing.service.AbstractResellerUsageService;
import com.ztarmobile.invoicing.vo.UsageVo;

/**
 * Sprint implementation to handle the files for the CDR's.
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
     * Reference to the extracted directory of the Sprint CDR's.
     */
    @Value("${cdrs.extracted.sprint.dir}")
    private String targetSprintCdrs;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExpectedFileName(Calendar calendarNow) {
        StringBuilder sb = new StringBuilder("dwh_cdr_");
        sb.append(fromDateToYYYYmmFormat(calendarNow.getTime()));
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
    protected int getFileFrecuency() {
        return MONTH; // files are dropped every month.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasHeader() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UsageVo calculateIndividualUsage(String[] sln) {
        UsageVo usage = new UsageVo();

        // check if it's prepaid as there's a difference on how
        // sms is handled
        String ppd = sln[0];

        usage.setKbs(Float.parseFloat(sln[9]));
        if (ppd.equalsIgnoreCase("prepaid")) {
            if (sln.length > 14) {
                String tp = sln[14];
                if (tp != null && tp.equalsIgnoreCase("TEXT MESG")) {
                    usage.setSms(Float.parseFloat(sln[8]));
                } else {
                    usage.setMou(Float.parseFloat(sln[8]));
                }
            } else {
                usage.setMou(Float.parseFloat(sln[8]));
            }
        } else {
            usage.setSms(Float.parseFloat(sln[10]));
            usage.setMou(Float.parseFloat(sln[8]));
        }
        return usage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getCallDateFieldPositionAt() {
        return 5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMdnFieldPositionAt() {
        return 4;
    }
}
