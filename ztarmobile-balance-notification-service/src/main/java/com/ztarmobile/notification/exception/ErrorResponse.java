/*
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.notification.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

/**
 * Contains all the possible properties to be returned when there's an failure
 * while executing a REST service.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class ErrorResponse {
    // when the error occurred.
    long timestamp = new Date().getTime();
    // status of the transaction.
    int status;
    // the error description if applicable.
    String error;
    // the actual and mandatory error message.
    String message;

    /**
     * Creates a response with a predefined properties.
     * 
     * @param message
     *            The message.
     */
    public ErrorResponse(String message) {
        this(message, Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
    }

    /**
     * Creates a response with a predefined properties.
     * 
     * @param message
     *            The message.
     * @param status
     *            The status.
     */
    public ErrorResponse(String message, int status) {
        this.message = (message == null || message.trim().isEmpty()) ? "Unknown Error" : message;
        this.status = status;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ErrorResponse [timestamp=" + timestamp + ", status=" + status + ", error=" + error + ", message="
                + message + "]";
    }
}
