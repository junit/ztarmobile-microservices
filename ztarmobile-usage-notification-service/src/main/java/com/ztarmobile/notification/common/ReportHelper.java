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
     * @param includeError
     *            Include error?
     * @return The header of the reports.
     */
    public static String createHeader(boolean includeError) {
        StringBuilder sb = new StringBuilder();

        sb.append("MDN").append(COMMA);
        sb.append("Activation Date").append(COMMA);
        sb.append("Balance Account").append(COMMA);
        sb.append("DA1").append(COMMA);
        sb.append("DA2").append(COMMA);
        sb.append("DA4").append(COMMA);
        sb.append("Service Class");

        if (includeError) {
            sb.append(COMMA);
            sb.append("Error");
        }
        sb.append(BL);
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

        sb.append(fromNullToEmpty(vo.getMdn())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getActivationDate())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getAccountBalance())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getDedicatedAccount1())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getDedicatedAccount2())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getDedicatedAccount4())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getServiceClass()));

        if (vo.isError()) {
            sb.append(COMMA);
            sb.append(vo.getErrorDescription());
        }
        sb.append(BL);
        return sb.toString();
    }

    /**
     * Utility used to convert from a null value into an empty value. If the
     * input is not null, just return the original value.
     * 
     * @param value
     *            The value.
     * @return The converted value.
     */
    private static String fromNullToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

}
