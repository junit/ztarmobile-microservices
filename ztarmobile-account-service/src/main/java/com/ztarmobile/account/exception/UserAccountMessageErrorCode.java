/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import com.ztarmobile.exception.HttpMessageErrorCode;

import javax.ws.rs.core.Response;

/**
 * This Enum contains all the errors related to the account creation.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public enum UserAccountMessageErrorCode implements HttpMessageErrorCode {

    // Generic message
    USER_ACCOUNT_ERROR (90000, "User Account Error", INTERNAL_SERVER_ERROR), 
    UNABLE_CREATE_ACCOUNT (90001, "Unable to create new user account", INTERNAL_SERVER_ERROR),
    DUPLICATE_ACCOUNT (90002, "An account with the same email has been already registered", INTERNAL_SERVER_ERROR),
    FIRST_NAME_EMPTY (90004, "The first name must be provided and must not be empty", BAD_REQUEST),
    FIRST_NAME_LENGTH (90005, "The first name is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    LAST_NAME_EMPTY (90006, "The last name must be provided and must not be empty", BAD_REQUEST),
    LAST_NAME_LENGTH (90007, "The last name is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    EMAIL_EMPTY (90008, "The email must be provided and must not be empty", BAD_REQUEST),
    EMAIL_LENGTH (90009, "The email entered is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    EMAIL_INVALID (90010, "The email provided is not valid", BAD_REQUEST),
    PASSWORD_EMPTY (90011, "A valid password must be provided", BAD_REQUEST),
    PASSWORD_LENGTH (90012, "A valid password must have at least ? characters and a maximum of ? characters", BAD_REQUEST),
    PASSWORD_INVALID (90013, "A valid password must have at least one number and a symbol (like !@#$%^)", BAD_REQUEST);

    private int code;
    private String message;
    private String evaluatedMessage;
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
    UserAccountMessageErrorCode(int code, String message, Response.Status httpStatus) {
        this.code = code;
        this.message = message;
        this.evaluatedMessage = this.message;
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
     * Returns the evaluated message.
     *
     * @return The evaluated message description.
     */
    @Override
    public String getEvaluatedMessage() {
        return this.evaluatedMessage;
    }

    /**
     * Sets the evaluated message.
     * 
     * @param evaluatedMessage
     *            The new message.
     */
    @Override
    public void setEvaluatedMessage(String evaluatedMessage) {
        this.evaluatedMessage = evaluatedMessage;
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
        return "[" + this.code + "] " + evaluatedMessage;
    }

}
