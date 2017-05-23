/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.openid.connect.security.authorization;

import static com.ztarmobile.exception.AuthorizationMessageErrorCode.AUTHORIZATION_ERROR;

import com.ztarmobile.exception.HttpMessageErrorCode;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;

/**
 * Thrown if an authorization request could not be processed due to a system
 * problem.
 * <p>
 * This might be thrown if a backend authentication repository is unavailable,
 * for example.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class AuthorizationServiceException extends AuthorizationException {
    /**
     * The serial number.
     */
    private static final long serialVersionUID = -2668956652376062805L;

    private int responseStatus = 401;
    private HttpMessageErrorCode httpMessageErrorCode;

    /**
     * Constructs an <code>AuthorizationServiceException</code> with the
     * specified message.
     *
     * @param msg
     *            the detail message
     */
    public AuthorizationServiceException(HttpMessageErrorCodeResolver resolver) {
        super(resolveMessage(resolver));
        this.httpMessageErrorCode = resolver.getHttpMessageErrorCode();
        this.httpMessageErrorCode.setMessage(resolveMessage(resolver));
    }

    /**
     * Constructs an <code>AuthorizationServiceException</code> with the
     * specified message.
     *
     * @param msg
     *            the detail message
     */
    public AuthorizationServiceException(HttpMessageErrorCode httpMessageErrorCode) {
        super(httpMessageErrorCode.getMessage());
        this.httpMessageErrorCode = httpMessageErrorCode;
    }

    /**
     * Constructs an <code>AuthorizationServiceException</code> with the
     * specified message.
     *
     * @param msg
     *            the detail message
     */
    public AuthorizationServiceException(String msg) {
        super(msg);
        this.httpMessageErrorCode = AUTHORIZATION_ERROR;
        this.httpMessageErrorCode.setMessage(msg);
    }

    /**
     * Constructs an <code>AuthorizationServiceException</code> with the
     * specified message.
     *
     * @param msg
     *            the detail message
     * @param responseStatus
     *            The response status.
     */
    public AuthorizationServiceException(String msg, int responseStatus) {
        super(msg);
        this.responseStatus = responseStatus;
    }

    /**
     * Constructs an <code>AuthorizationServiceException</code> with the
     * specified message and root cause.
     *
     * @param msg
     *            the detail message
     * @param t
     *            root cause
     */
    public AuthorizationServiceException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * @return the responseStatus
     */
    public int getResponseStatus() {
        return responseStatus;
    }

    /**
     * @return the httpMessageErrorCode
     */
    public HttpMessageErrorCode getHttpMessageErrorCode() {
        return httpMessageErrorCode;
    }

    /**
     * Resolves the message with parameters.
     * 
     * @param httpMessageErrorCode
     *            The httpMessageErrorCode.
     * @return The final message.
     */
    private static String resolveMessage(HttpMessageErrorCodeResolver httpMessageErrorCode) {
        String originalMessage = httpMessageErrorCode.getHttpMessageErrorCode().getMessage();
        StringBuilder finalMessage = new StringBuilder(originalMessage);

        int index = -1;
        for (String param : httpMessageErrorCode.getParams()) {
            index = finalMessage.indexOf("?");
            if (index == -1) {
                // no question marks were found
                break;
            }
            finalMessage.replace(index, index + 1, param);
        }
        return finalMessage.toString();
    }
}
