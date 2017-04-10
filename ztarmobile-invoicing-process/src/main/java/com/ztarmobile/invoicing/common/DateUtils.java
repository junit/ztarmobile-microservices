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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Utility to handle the date format conversions.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public class DateUtils {
    /**
     * Format used commonly to deserialize dates. e.g. Jun 2, 2016 9:46:15 AM
     */
    public static final String MEDIUM = "MMM d, yyyy hh:mm:ss a";
    /**
     * Default timezone of the JVM
     */
    public static final String DEFAULT_TIMEZONE = "America/Chicago";
    /**
     * Format used commonly by the db.
     */
    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat(YYYYMMDD_HHMMSS);

    /**
     * Format used commonly in the CDR files.
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private static final SimpleDateFormat CDR_DATE_FORMAT = new SimpleDateFormat(YYYYMMDDHHMMSS);

    /**
     * Format used in CDR files yyyyMMdd.
     */
    public static final String YYYYMMDD = "yyyyMMdd";
    private static final SimpleDateFormat YYYYMMDD_DATE_FORMAT = new SimpleDateFormat(YYYYMMDD);

    /**
     * Format used in date format yyyy-MM-dd.
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final SimpleDateFormat YYYYMMDD_DASH_DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD);

    /**
     * Format used in CDR files yyyyMM.
     */
    public static final String YYYYMM = "yyyyMM";
    private static final SimpleDateFormat YYYYMM_DATE_FORMAT = new SimpleDateFormat(YYYYMM);

    /**
     * Format used in dates MM/dd/yyyy.
     */
    public static final String MMDDYYYY = "MM/dd/yyyy";
    private static final SimpleDateFormat MMDDYYYY_DATE_FORMAT = new SimpleDateFormat(MMDDYYYY);

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
        return DB_DATE_FORMAT.format(date);
    }

    /**
     * Converts from a date object to a cdrs file format.
     * 
     * @param date
     *            The date to convert to a cdrs file format.
     * @return The String representation of the date.
     */
    public static String fromDateToYYmmddHHmmssFormat(Date date) {
        return CDR_DATE_FORMAT.format(date);
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
            return CDR_DATE_FORMAT.parse(dateString);
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
        return YYYYMMDD_DATE_FORMAT.format(date);
    }

    /**
     * Converts from a date object to a yyyy-MM-dd format.
     * 
     * @param date
     *            The date to convert to a yyyy-MM-dd format.
     * @return The String representation of the date.
     */
    public static String fromDateToYYYYmmddDashFormat(Date date) {
        return YYYYMMDD_DASH_DATE_FORMAT.format(date);
    }

    /**
     * Converts from a date object to a yyyyMM format.
     * 
     * @param date
     *            The date to convert to a yyyyMM format.
     * @return The String representation of the date.
     */
    public static String fromDateToYYYYmmFormat(Date date) {
        return YYYYMM_DATE_FORMAT.format(date);
    }

    /**
     * Converts from a String object to a date format.
     * 
     * @param dateString
     *            The date to convert to a date format.
     * @return The Date representation of the string.
     */
    public static Date fromStringToMMddYYYYFormat(String dateString) {
        try {
            return MMDDYYYY_DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            CommonUtils.invalidInput("Unable to convert string: " + dateString + ", to date " + e);
            return null;
        }
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
        // we first set the calendar to the first day of the month so that when
        // we set the month, the calculation of the last day works on the same
        // month.
        calendar.set(DAY_OF_MONTH, calendar.getActualMinimum(DAY_OF_MONTH));
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
     * Given a calendar set the minimum value for the hour, minutes and seconds.
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
     * Given a calendar set the maximum value for the hour, minutes and seconds.
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

    /**
     * Given a calendar, this one is set to the last day of the month.
     * 
     * @param calendar
     *            The calendar to be returned as the last one of the month.
     * @return The last day of the month.
     */
    public static Calendar getMaximumCalendarMonth(Calendar calendar) {
        Calendar calendarLast = createCalendarFrom(calendar);

        calendarLast.set(DAY_OF_MONTH, calendarLast.getActualMaximum(DAY_OF_MONTH));

        calendarLast.set(HOUR_OF_DAY, calendarLast.getActualMaximum(HOUR_OF_DAY));
        calendarLast.set(MINUTE, calendarLast.getActualMaximum(MINUTE));
        calendarLast.set(SECOND, calendarLast.getActualMaximum(SECOND));
        return calendarLast;
    }

    /**
     * Given a calendar, this one is set to the first day of the month.
     * 
     * @param calendar
     *            The calendar to be returned as the first one of the month.
     * @return The first day of the month.
     */
    public static Calendar getMinimumCalendarMonth(Calendar calendar) {
        Calendar calendarFirst = createCalendarFrom(calendar);

        calendarFirst.set(DAY_OF_MONTH, calendarFirst.getActualMinimum(DAY_OF_MONTH));

        calendarFirst.set(HOUR_OF_DAY, calendarFirst.getActualMinimum(HOUR_OF_DAY));
        calendarFirst.set(MINUTE, calendarFirst.getActualMinimum(MINUTE));
        calendarFirst.set(SECOND, calendarFirst.getActualMinimum(SECOND));
        return calendarFirst;
    }

    /**
     * Given a start and end calendar, this method returns a list of months
     * within that interval. e.g. The start date is: 01/23/17 and the end date
     * is: 03/14/17. The result would be [01/23/17 - 01/31-17, 02/01/17 -
     * 02/28/17, 03/01/17 - 03/14/17].
     * 
     * @param start
     *            The start calendar.
     * @param end
     *            The end calendar.
     * @return List of intervals by month.
     */
    public static List<MontlyTime> splitTimeByMonth(Calendar start, Calendar end) {
        List<MontlyTime> list = null;
        if (!(start == null || end == null)) {
            if (!start.after(end)) {
                // the end date is greater than start date and those are not
                // null
                list = new ArrayList<>();

                Calendar startTmp = createCalendarFrom(start);
                for (;;) {
                    Calendar calendarNow = getMaximumCalendarMonth(startTmp);

                    if (calendarNow.after(end) || calendarNow.equals(end)) {
                        list.add(createMontlyTime(startTmp, end));
                        break;
                    } else {
                        list.add(createMontlyTime(startTmp, calendarNow));
                        // we prepare for the next interval
                        calendarNow.add(MONTH, 1);
                        calendarNow = getMinimumCalendarMonth(calendarNow);
                        startTmp = getMinimumCalendarMonth(calendarNow);
                    }

                }
            }
        }
        return list;
    }

    /**
     * This method test whether the date passed in the argument is in the future
     * or not.
     * 
     * @param calendar
     *            The input calendar.
     * @return true, if this date is in the future, false it is not.
     */
    public static boolean isFutureDate(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        return calendar.after(now);
    }

    /**
     * Creates a monthly time object.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     * @return The monthly time.
     */
    private static MontlyTime createMontlyTime(Calendar start, Calendar end) {
        MontlyTime montlyTime = new MontlyTime();
        montlyTime.setStart(createCalendarFrom(start));
        montlyTime.setEnd(createCalendarFrom(end));
        return montlyTime;
    }
}
