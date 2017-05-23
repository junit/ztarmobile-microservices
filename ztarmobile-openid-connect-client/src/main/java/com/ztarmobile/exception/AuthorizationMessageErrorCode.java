/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import javax.ws.rs.core.Response;

/**
 * This Enum contains all the errors related to the authorization.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public enum AuthorizationMessageErrorCode implements HttpMessageErrorCode {

    // Generic message
    AUTHORIZATION_ERROR(80000, "Authorization Error", INTERNAL_SERVER_ERROR), 
    NO_ACCESS_TOKEN_FOUND(80001, "Access token was not found in the header or payload of the request", BAD_REQUEST),
    UNAUTHORIZED_ACCESS(80002, "Full authentication is required to access this resource", UNAUTHORIZED),
    NO_ACCESS_EXTERNAL_RESOURCE(80003, "Unable to get access to external resource [?]", INTERNAL_SERVER_ERROR);

    private int code;
    private String message;
    private int httpCode;

    /**
     * Creates a header message error with code, message and HTTP status.
     *
     * @param code
     *            The code.
     * @param message
     *            The message.
     * @param httpStatus
     *            The HTTP status.
     */
    AuthorizationMessageErrorCode(int code, String message, Response.Status httpStatus) {
        this.code = code;
        this.message = message;
        this.httpCode = httpStatus.getStatusCode();
    }

    /**
     * Returns the error code.
     *
     * @return The number of the code.
     */
    @Override
    public int getNumber() {
        return this.code;
    }

    /**
     * Returns the message.
     *
     * @return The message description.
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * Updates the message.
     * 
     * @param message
     *            The new message.
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return The HTTP status code.
     */
    @Override
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * The message of the exception.
     *
     * @return The message description.
     */
    @Override
    public String toString() {
        return "[" + this.code + "] " + message;
    }

}
