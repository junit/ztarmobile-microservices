/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.common;

import com.ztarmobile.transaction.model.SubscriberTransaction;

import java.util.List;

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
        StringBuilder sb = new StringBuilder("report_payment_transaction");

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
        sb.append("Amount").append(COMMA);
        sb.append("Payment Date");

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
    public static String createRow(SubscriberTransaction vo) {
        StringBuilder sb = new StringBuilder();

        sb.append(fromNullToEmpty(vo.getMdn())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getTotal())).append(COMMA);
        sb.append(fromNullToEmpty(vo.getPaymentDate()));

        sb.append(BL);
        return sb.toString();
    }

    /**
     * Creates the total amount for each MDN.
     * 
     * @param list
     *            The list of subscribers.
     * @return The row.
     */
    public static String createTotalRow(List<SubscriberTransaction> list) {
        StringBuilder sb = new StringBuilder();

        double total = 0;
        for (SubscriberTransaction st : list) {
            total += Double.parseDouble(st.getTotal());
        }

        sb.append(COMMA);
        sb.append(total).append(COMMA);

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
