/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.exception;

import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.USER_ACCOUNT_ERROR;

import com.ztarmobile.exception.HttpMessageErrorCode;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.exception.ServiceException;

/**
 * Thrown if there's any validation or error that needs to be reported in the
 * final response.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class AccountServiceException extends ServiceException {
    /**
     * The serial number.
     */
    private static final long serialVersionUID = -2668956652376062805L;

    private HttpMessageErrorCode httpMessageErrorCode;

    /**
     * Constructs an <code>AccountServiceException</code> with the
     * specified message.
     *
     * @param resolver
     *            The HTTP message error code resolver.
     */
    public AccountServiceException(HttpMessageErrorCodeResolver resolver) {
        super(resolver.getResolvedMessage());
        this.httpMessageErrorCode = resolver.getHttpMessageErrorCode();
        // the original message is replaced.
        this.httpMessageErrorCode.setEvaluatedMessage(super.getMsg());
    }

    /**
     * Constructs an <code>AccountServiceException</code> with the
     * specified message.
     *
     * @param httpMessageErrorCode
     *            The HTTP message error code.
     */
    public AccountServiceException(HttpMessageErrorCode httpMessageErrorCode) {
        super(httpMessageErrorCode.getMessage());
        this.httpMessageErrorCode = httpMessageErrorCode;
    }

    /**
     * Constructs an <code>AccountServiceException</code> with the
     * specified message.
     *
     * @param msg
     *            the detail message
     */
    public AccountServiceException(String msg) {
        super(msg);
        this.httpMessageErrorCode = USER_ACCOUNT_ERROR;
        this.httpMessageErrorCode.setEvaluatedMessage(msg);
    }

    /**
     * Constructs an <code>AccountServiceException</code> with the
     * specified message and root cause.
     *
     * @param msg
     *            the detail message
     * @param t
     *            root cause
     */
    public AccountServiceException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * @return the httpMessageErrorCode
     */
    public HttpMessageErrorCode getHttpMessageErrorCode() {
        return httpMessageErrorCode;
    }
}
