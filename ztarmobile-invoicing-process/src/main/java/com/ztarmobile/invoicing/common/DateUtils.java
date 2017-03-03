/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility to handle the date format conversions.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public class DateUtils {
    /**
     * Last hour.
     */
    public static final int LAST_HOUR = 23;
    /**
     * Last min/sec.
     */
    public static final int LAST_MIN_SEC = 59;
    /**
     * Format used commonly by the db.
     */
    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat(YYYYMMDD_HHMMSS);

    /**
     * Format used in cdrs files yyyyMMdd.
     */
    public static final String YYYYMMDD = "yyyyMMdd";
    private static final SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat(YYYYMMDD);

    /**
     * Format used in cdrs files yyyyMM.
     */
    public static final String YYYYMM = "yyyyMM";
    private static final SimpleDateFormat yyyyMMDateFormat = new SimpleDateFormat(YYYYMM);

    /**
     * Private constructor.
     */
    private DateUtils() {
        // no objects are allowed to be created.
    }

    /**
     * Converts from a date object to a data base format.
     * 
     * @param date
     *            The date to convert to a database format.
     * @return The String representation of the date.
     */
    public static String fromDateToDbFormat(Date date) {
        return dbDateFormat.format(date);
    }

    /**
     * Converts from a date object to a yyyyMMdd format.
     * 
     * @param date
     *            The date to convert to a yyyyMMdd format.
     * @return The String representation of the date.
     */
    public static String fromDateToYYYYmmddFormat(Date date) {
        return yyyyMMddDateFormat.format(date);
    }

    /**
     * Converts from a date object to a yyyyMM format.
     * 
     * @param date
     *            The date to convert to a yyyyMM format.
     * @return The String representation of the date.
     */
    public static String fromDateToYYYYmmFormat(Date date) {
        return yyyyMMDateFormat.format(date);
    }

    /**
     * This method returns the first day of a month passed in the arguments.
     * 
     * @param month
     *            A month.
     * @return The first day of a month.
     * @see DateUtils#getMaximumDayOfMonth(int)
     */
    public static Calendar getMinimunDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(MONTH, month);
        calendar.set(DAY_OF_MONTH, calendar.getActualMinimum(DAY_OF_MONTH));
        return calendar;
    }

    /**
     * This method returns the last day of a month passed in the arguments.
     * 
     * @param month
     *            A month.
     * @return The last day of a month.
     * @see DateUtils#getMinimunDayOfMonth(int)
     */
    public static Calendar getMaximumDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(MONTH, month);
        calendar.set(DAY_OF_MONTH, calendar.getActualMaximum(DAY_OF_MONTH));
        return calendar;
    }
}
