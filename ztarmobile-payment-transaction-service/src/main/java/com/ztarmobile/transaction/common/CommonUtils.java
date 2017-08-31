/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility to handle some common operations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public class CommonUtils {
    /**
     * The format.
     */
    private static DateFormat DF = new SimpleDateFormat("MM/dd/yyyy");

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
     * Validates that the value being passed is true or not, if it's true, it
     * throws the exception, otherwise everything is ignored.
     * 
     * @param value
     *            The boolean value.
     * @param msg
     *            The exception message.
     */
    public static void validateInput(boolean value, String msg) {
        if (value) {
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

    public static String getFormattedDate(Date date) {
        String dateString = "";
        if (date != null) {
            dateString = DF.format(date);
        }
        return dateString;
    }

    /**
     * Gets the amount of days between now and the endDate.
     * 
     * @param endDate
     *            The end date.
     * @return Total of days.
     */
    public static String getDaysBetween(Date endDate) {
        String days = "";
        if (endDate == null) {
            // for some reason Ericsson returned as null
            days = "-1";
        }
        Calendar calendarToday = Calendar.getInstance();
        Calendar calendarEndDate = Calendar.getInstance();
        calendarEndDate.setTime(endDate);

        int year = calendarEndDate.get(Calendar.YEAR);
        if (year == 9999) {
            // for some reason E/ returned 9999, under this scenario we don't
            // need to calculate anything.
            days = "N/A";
        } else {
            long timeElapsed = calendarEndDate.getTimeInMillis() - calendarToday.getTimeInMillis();
            BigDecimal bd = new BigDecimal(timeElapsed);
            bd = bd.divide(new BigDecimal(1000 * 60 * 60 * 24), 2, RoundingMode.HALF_UP);

            days = String.valueOf(Math.round(bd.doubleValue()));
        }

        return days;
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
     * Returns a representation of the object in JSON format.
     * 
     * @param object
     *            The object.
     * @return The string representation.
     */
    public static String toJson(Object object) {
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            invalidInput("Error while converting object into json format: " + e);
        }
        return json;
    }

    /**
     * This method just returns the service URL of this application.
     * 
     * @param serverAddress
     *            The server address.
     * @param serverPort
     *            The server port.
     * @return The URL of this service.
     */
    public static String createServiceUrl(String serverAddress, String serverPort) {
        return "http://" + serverAddress + ":" + serverPort;
    }
}
