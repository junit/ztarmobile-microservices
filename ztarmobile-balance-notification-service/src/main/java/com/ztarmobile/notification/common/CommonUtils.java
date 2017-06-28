/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.notification.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to handle some common operations.
 *
 * @author armandorivas
 * @since 03/01/17
 */
public class CommonUtils {

    public static final int PROFILE_FIRST_NAME_LEN = 50;
    public static final int PROFILE_LAST_NAME_LEN = 50;
    public static final int PROFILE_EMAIL_LEN = 50;
    public static final int PROFILE_PASS_MIN = 8;
    public static final int PROFILE_PASS_MAX = 20;

    public static final int ADDRESS_NAME_LEN = 100;
    public static final int ADDRESS_LINE1_LEN = 100;
    public static final int ADDRESS_LINE2_LEN = 100;
    public static final int ADDRESS_LINE3_LEN = 100;
    public static final int ADDRESS_CITY_LEN = 100;
    public static final int ADDRESS_STATE_LEN = 2;
    public static final int ADDRESS_ZIP_LEN = 15;
    public static final int ADDRESS_COUNTRY_LEN = 100;

    public static final int MDN_PHONE_LEN = 10;

    public static final int PAYMENT_PROFILE_ALIAS_LEN = 50;
    public static final int PAYMENT_PROFILE_EXP_LEN = 4;
    public static final int PAYMENT_PROFILE_KEY_LEN = 50;

    public static final String SLASH = "/";
    /**
     * The email pattern.
     */
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * The password pattern.
     */
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[!@#$%]).{" + PROFILE_PASS_MIN + ","
            + PROFILE_PASS_MAX + "})";

    /**
     * The expiration date pattern.
     */
    private static final String EXP_DATE_PATTERN = "(0[1-9]|1[0-2])[0-9]{2}";

    private static Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);
    private static Pattern patternPass = Pattern.compile(PASSWORD_PATTERN);
    private static Pattern patternExpDate = Pattern.compile(EXP_DATE_PATTERN);

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

    /**
     * Validate hex with regular expression.
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validateEmail(final String hex) {
        Matcher matcher = patternEmail.matcher(hex);
        return matcher.matches();
    }

    /**
     * Validates the password by having at least one digit and a special
     * character.
     * 
     * @param password
     *            The password.
     * @return true, the password matches, false it does not match.
     */
    public static boolean validatePassword(final String password) {
        Matcher matcher = patternPass.matcher(password);
        return matcher.matches();
    }

    /**
     * Validates that the expiration date has at least 2 digits as part of the
     * month and another 2 digits for the year.
     * 
     * @param expirationDate
     *            The expiration date.
     * @return true, the expiration date matches, false it does not match.
     */
    public static boolean validateExpDate(final String expirationDate) {
        Matcher matcher = patternExpDate.matcher(expirationDate);
        return matcher.matches();
    }
}
