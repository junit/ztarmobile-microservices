/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;

import java.util.Calendar;

/**
 * Utility to handle some common operations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public class CommonUtils {
    /**
     * Private constructor.
     */
    private CommonUtils() {
        // no objects are allowed to be created.
    }

    /**
     * Validates the input value.
     * 
     * @param value
     *            The actual value.
     */
    public static void validateInput(String value) {
        validateInput(value, "Please provide a valid value...");
    }

    /**
     * Validates the input value.
     * 
     * @param value
     *            The actual value.
     * @param msg
     *            The message to be displayed.
     */
    public static void validateInput(String value, String msg) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Validates a generic object to test if this object is null or not.
     * 
     * @param object
     *            The object.
     * @param msg
     *            The exception message.
     */
    public static void validateInput(Object object, String msg) {
        if (object == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Validates a generic array to test if this array is null or empty.
     * 
     * @param arr
     *            The array of elements.
     * @param msg
     *            The exception message.
     */
    public static void validateInput(Object[] arr, String msg) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Throws an exception with a given message.
     * 
     * @param msg
     *            Exception message.
     */
    public static void invalidInput(String msg) {
        throw new IllegalArgumentException(msg);
    }

    /**
     * This method converts the elements of the array into a single string
     * separated by comma (,). Also, every element is modified by adding leading
     * and trailing single quotes.<code>E.g. [a,b,c] =&#62; 'a','b','c'</code>
     * 
     * @param array
     *            The array to be converted. It does nothing when the array is
     *            null or empty.
     * @return The list of elements separated by ','.
     */
    public static String toInFormatString(String[] array) {
        String elements = null;
        if (array != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append("'");
                sb.append(s);
                sb.append("'");
                sb.append(",");
            }
            if (sb.length() > 0) {
                if (sb.toString().endsWith(",")) {
                    sb.delete(sb.length() - 1, sb.length());
                }
            }
            elements = sb.toString();
        }
        return elements;
    }

    /**
     * This method returns the first day of a month passed in the arguments.
     * 
     * @param month
     *            A month.
     * @return The first day of a month.
     * @see CommonUtils#getMaximumDayOfMonth(int)
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
     * @see CommonUtils#getMinimunDayOfMonth(int)
     */
    public static Calendar getMaximumDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(MONTH, month);
        calendar.set(DAY_OF_MONTH, calendar.getActualMaximum(DAY_OF_MONTH));
        return calendar;
    }
}
