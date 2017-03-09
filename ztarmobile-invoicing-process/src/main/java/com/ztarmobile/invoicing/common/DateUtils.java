/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;

import java.text.ParseException;
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
     * Format used commonly by the db.
     */
    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat(YYYYMMDD_HHMMSS);

    /**
     * Format used commonly in the cdr files.
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private static final SimpleDateFormat cdrDateFormat = new SimpleDateFormat(YYYYMMDDHHMMSS);

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
     * Converts from a date object to a cdrs file format.
     * 
     * @param date
     *            The date to convert to a cdrs file format.
     * @return The String representation of the date.
     */
    public static String fromDateToYYmmddHHmmssFormat(Date date) {
        return cdrDateFormat.format(date);
    }

    /**
     * Converts from a String object to a cdrs file format.
     * 
     * @param dateString
     *            The date to convert to a cdrs file format.
     * @return The String representation of the date.
     */
    public static Date fromStringToYYmmddHHmmssFormat(String dateString) {
        try {
            return cdrDateFormat.parse(dateString);
        } catch (ParseException e) {
            CommonUtils.invalidInput("Unable to convert string: " + dateString + ", to date " + e);
            return null;
        }
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

    /**
     * This method returns a copy of a calendar based on another calendar
     * object.
     * 
     * @param calendar
     *            The initial calendar.
     * @return A new calendar.
     */
    public static Calendar createCalendarFrom(Calendar calendar) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(calendar.getTimeInMillis());
        return newCalendar;
    }

    /**
     * Given a calendar set the minimun value for the hour, minutes and secodns.
     * (e.g. 0 min, 0 sec and 0 hours =&gt; 00:00:00).
     * 
     * @param calendarStart
     *            The calendar.
     */
    public static void setMinimumCalendarDay(Calendar calendarStart) {
        calendarStart.set(HOUR_OF_DAY, calendarStart.getActualMinimum(HOUR_OF_DAY));
        calendarStart.set(MINUTE, calendarStart.getActualMinimum(MINUTE));
        calendarStart.set(SECOND, calendarStart.getActualMinimum(SECOND));
    }

    /**
     * Given a calendar set the maximum value for the hour, minutes and secodns.
     * If the calendar end is greater than 'now', then the calendar is set to
     * 'now'. This calendar can never be greater than 'now'.
     * 
     * @param calendarEnd
     *            The calendar.
     */
    public static void setMaximumCalendarDay(Calendar calendarEnd) {
        // we get the current time.
        Calendar calendarNow = Calendar.getInstance();

        // Based on the current time, we calculate the hour, min and sec to set
        // the date.
        if (calendarEnd.compareTo(calendarNow) >= 0) {
            calendarEnd = calendarNow;
        } else {
            calendarEnd.set(HOUR_OF_DAY, calendarEnd.getActualMaximum(HOUR_OF_DAY));
            calendarEnd.set(MINUTE, calendarEnd.getActualMaximum(MINUTE));
            calendarEnd.set(SECOND, calendarEnd.getActualMaximum(SECOND));
        }
    }
}
