/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model;

/**
 * All the status.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public enum Status {
    /**
     * All the constants.
     */
    SUCCESS('S'), FAILURE('F');
    /**
     * The value.
     */
    private char value;

    /**
     * Enum with an specific status.
     * 
     * @param status
     *            The status.
     */
    Status(char value) {
        this.value = value;
    }

    /**
     * @return the value of the status.
     */
    public String getValue() {
        return String.valueOf(value);
    }
}
