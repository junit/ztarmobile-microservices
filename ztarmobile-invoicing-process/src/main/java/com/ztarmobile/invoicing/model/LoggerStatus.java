/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

/**
 * Value object with the constants available for the logger process.
 *
 * @author armandorivas
 * @since 03/23/17
 */
public enum LoggerStatus {
    /**
     * Status to indicate that the record is in progress.
     */
    PROGRESS('P'),
    /**
     * Status to indicate that the record was loaded successfully.
     */
    PENDING('P'),
    /**
     * Status to indicate that the record was loaded successfully.
     */
    COMPLETED('C'),
    /**
     * Status to indicate that there was an error.
     */
    ERROR('E');

    /**
     * The value of the enum.
     */
    private char statusVal;

    /**
     * The constructor for this enum.
     * 
     * @param statusVal
     *            Value of the status.
     */
    private LoggerStatus(char statusVal) {
        this.statusVal = statusVal;
    }

    /**
     * Gets the value of this enum.
     * 
     * @return The value of the enum.
     */
    public char getStatusVal() {
        return this.statusVal;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.valueOf(getStatusVal());
    }
}
