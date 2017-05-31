/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.security.authorization;

/**
 * Abstract superclass for all exceptions related to Authentication being
 * invalid for whatever reason.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public abstract class AuthorizationException extends RuntimeException {
    /**
     * The serial number.
     */
    private static final long serialVersionUID = -3056264215973397357L;
    /**
     * The message description.
     */
    protected String msg;

    /**
     * Constructs an {@code AuthorizationException} with the specified message
     * and root cause.
     *
     * @param msg
     *            the detail message
     * @param t
     *            the root cause
     */
    public AuthorizationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message
     * and no root cause.
     *
     * @param msg
     *            the detail message
     */
    public AuthorizationException(String msg) {
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
