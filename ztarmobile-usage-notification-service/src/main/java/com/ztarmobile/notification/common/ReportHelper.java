/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.notification.common;

import com.ztarmobile.notification.model.SubscriberUsage;

/**
 * Utility to handle the reports.
 *
 * @author armandorivas
 * @since 04/09/17
 */
public class ReportHelper {
    /**
     * Break line and separator for the reports.
     */
    private static final String BL = "\n";
    private static final String COMMA = ",";

    /**
     * Private constructor.
     */
    private ReportHelper() {
        // no objects are allowed to be created.
    }

    /**
     * Creates the report name.
     * 
     * @return The report name.
     */
    public static String createReportName() {
        StringBuilder sb = new StringBuilder("report_subscriber_usage");

        sb.append(".csv");
        return sb.toString();
    }

    /**
     * Creates the header of the usage report.
     *
     * @return The header of the reports.
     */
    public static String createHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("MDN").append(COMMA);
        sb.append("MDN2").append(BL);
        return sb.toString();
    }

    /**
     * Based on an object it creates a single row in the report.
     *
     * @param vo
     *            The single object.
     * @return One row of the report.
     */
    public static String createRow(SubscriberUsage vo) {
        StringBuilder sb = new StringBuilder();

        sb.append(vo.getMdn()).append(COMMA);
        sb.append(vo.getMdn()).append(BL);
        return sb.toString();
    }

}
