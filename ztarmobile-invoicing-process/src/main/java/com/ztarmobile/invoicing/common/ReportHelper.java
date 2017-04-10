/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.common;

import static com.ztarmobile.invoicing.common.DateUtils.fromDateToYYYYmmddFormat;

import java.util.Calendar;

import com.ztarmobile.invoicing.model.ReportDetails;

/**
 * Utility to handle the reports.
 *
 * @author armandorivas
 * @since 04/09/17
 */
public class ReportHelper {
    /**
     * Private constructor.
     */
    private ReportHelper() {
        // no objects are allowed to be created.
    }

    /**
     * Break line and separator for the reports.
     */
    private static final String BL = "\n";
    private static final String COMMA = ",";

    /**
     * Creates the report name.
     * 
     * @param product
     *            The product.
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @return The report name.
     */
    public static String createReportName(String product, Calendar start, Calendar end) {
        StringBuilder sb = new StringBuilder();
        sb.append(product);
        sb.append("_");
        sb.append(fromDateToYYYYmmddFormat(start.getTime()));
        sb.append("_");
        sb.append(fromDateToYYYYmmddFormat(end.getTime()));
        sb.append(".csv");

        return sb.toString();
    }

    /**
     * Creates the header of the invoicing report.
     *
     * @return The header of the reports.
     */
    public static String createHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("Year").append(COMMA);
        sb.append("Month").append(COMMA);
        sb.append("MDN").append(COMMA);
        sb.append("Rate-Plan").append(COMMA);
        sb.append("Days-On-Plan").append(COMMA);
        sb.append("Actual-MOU").append(COMMA);
        sb.append("Actual-MBS").append(COMMA);
        sb.append("Actual-SMS").append(COMMA);
        sb.append("Actual-MMS").append(BL);
        return sb.toString();
    }

    /**
     * Based on an object it creates a single row in the report.
     *
     * @param vo
     *            The single object.
     * @return One row of the report.
     */
    public static String createRow(ReportDetails vo) {
        StringBuilder sb = new StringBuilder();
        sb.append(vo.getYear()).append(COMMA);
        sb.append(vo.getMonth()).append(COMMA);
        sb.append(vo.getMdn()).append(COMMA);
        sb.append(vo.getRatePlan()).append(COMMA);
        sb.append(vo.getDayOnPlans()).append(COMMA);
        sb.append(vo.getMou()).append(COMMA);
        sb.append(vo.getMbs()).append(COMMA);
        sb.append(vo.getSms()).append(COMMA);
        sb.append(vo.getMms()).append(BL);
        return sb.toString();
    }

}
