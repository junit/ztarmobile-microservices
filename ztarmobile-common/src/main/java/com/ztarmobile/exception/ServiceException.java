/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.exception;

/**
 * Abstract superclass for all exceptions related to the services.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class ServiceException extends RuntimeException {
    /**
     * The serial number.
     */
    private static final long serialVersionUID = -3056264215973397357L;
    /**
     * The message description.
     */
    protected String msg;

    /**
     * Constructs an {@code ServiceException} with the specified message and
     * root cause.
     *
     * @param msg
     *            the detail message
     * @param t
     *            the root cause
     */
    public ServiceException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs an {@code ServiceException} with the specified message and no
     * root cause.
     *
     * @param msg
     *            the detail message
     */
    public ServiceException(String msg) {
        super(msg);
        this.msg = msg;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }
}
