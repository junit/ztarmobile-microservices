/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.core.Response;

/**
 * This Enum contains all the errors related to the authorization.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public enum GlobalMessageErrorCode implements HttpMessageErrorCode {
    
    // Generic message
    UNABLE_COMPLETE_TRANSACTION (90000, "There was an error while running this transaction, try again later", INTERNAL_SERVER_ERROR),
    
    USER_PROFILE_ERROR (90001, "User Profile Error", INTERNAL_SERVER_ERROR), 
    USER_PROFILE_DUPLICATE_PROFILE (90002, "A profile with the same email has been already registered", INTERNAL_SERVER_ERROR),
    USER_PROFILE_FIRST_NAME_EMPTY (90004, "The first name must be provided and must not be empty", BAD_REQUEST),
    USER_PROFILE_FIRST_NAME_LENGTH (90005, "The first name is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    USER_PROFILE_LAST_NAME_EMPTY (90006, "The last name must be provided and must not be empty", BAD_REQUEST),
    USER_PROFILE_LAST_NAME_LENGTH (90007, "The last name is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    USER_PROFILE_EMAIL_EMPTY (90008, "The email must be provided and must not be empty", BAD_REQUEST),
    USER_PROFILE_EMAIL_LENGTH (90009, "The email entered is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    USER_PROFILE_EMAIL_INVALID (90010, "The email provided is not valid", BAD_REQUEST),
    USER_PROFILE_PASSWORD_EMPTY (90011, "A valid password must be provided", BAD_REQUEST),
    USER_PROFILE_PASSWORD_LENGTH (90012, "A valid password must have at least ? characters and a maximum of ? characters", BAD_REQUEST),
    USER_PROFILE_PASSWORD_INVALID (90013, "A valid password must have at least one number and a symbol (like !@#$%^)", BAD_REQUEST),
    USER_PROFILE_NOT_FOUND (90014, "User profile with identifier '?' was not found", BAD_REQUEST),
    USER_PROFILE_NOT_FOUND_BY_PAYMENT_PROFILE (90015, "The User profile associated with this payment profile '?' was not found", INTERNAL_SERVER_ERROR),
    USER_PROFILE_NOT_FOUND_BY_ADDRESS (90015, "The User profile associated with this address '?' was not found", INTERNAL_SERVER_ERROR),
    
    ADDRESS_NOT_FOUND (90016, "Address with identifier '?' was not found", BAD_REQUEST),
    ADDRESS_NAME_EMPTY (90017, "The name of the address must be provided and must not be empty", BAD_REQUEST),
    ADDRESS_NAME_MAX_LEN (90018, "The name of the address is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_LINE1_EMPTY (90019, "The address line 1 must be provided and must not be empty", BAD_REQUEST),
    ADDRESS_LINE1_MAX_LEN (90020, "The address line 1 is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_LINE2_MAX_LEN (90021, "The address line 2 is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_LINE3_MAX_LEN (90022, "The address line 3 is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_CITY_EMPTY (90023, "The city must be provided and must not be empty", BAD_REQUEST),
    ADDRESS_CITY_MAX_LEN (90024, "The city is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_STATE_EMPTY (90025, "The state must be provided and must not be empty", BAD_REQUEST),
    ADDRESS_STATE_MAX_LEN (90026, "The state is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_ZIP_EMPTY (90027, "The zip code must be provided and must not be empty", BAD_REQUEST),
    ADDRESS_ZIP_MAX_LEN (90028, "The zip code is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    ADDRESS_COUNTRY_MAX_LEN (90029, "The country is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    
    MDN_NOT_FOUND (90030, "Mdn with identifier '?' was not found", BAD_REQUEST),
    MDN_PHONE_EMPTY (90031, "The phone number must be provided and must not be empty", BAD_REQUEST),
    MDN_PHONE_MAX_LEN (90032, "The phone number is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    
    PAYMENT_PROFILE_NOT_FOUND (90033, "The Payment Profile with identifier '?' was not found", BAD_REQUEST),
    PAYMENT_PROFILE_ALIAS_EMPTY (90034, "The alias must be provided and must not be empty", BAD_REQUEST),
    PAYMENT_PROFILE_ALIAS_MAX_LEN (90035, "The alias is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    PAYMENT_PROFILE_EXP_EMPTY (90036, "The expiration date must be provided and must not be empty", BAD_REQUEST),
    PAYMENT_PROFILE_EXP_MAX_LEN (90037, "The expiration date is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST),
    PAYMENT_PROFILE_KEY_EMPTY (90038, "The profile key must be provided and must not be empty", BAD_REQUEST),
    PAYMENT_PROFILE_KEY_MAX_LEN (90039, "The profile key is too long, the maximum length must be less or equals than ? characters", BAD_REQUEST);
    
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
    GlobalMessageErrorCode(int code, String message, Response.Status httpStatus) {
        this(code, message, httpStatus.getStatusCode());
    }

    /**
     * Creates a header message error with code, message and HTTP status.
     *
     * @param code
     *            The code.
     * @param message
     *            The message.
     * @param httpCode
     *            The HTTP code.
     */
    GlobalMessageErrorCode(int code, String message, int httpCode) {
        this.code = code;
        this.message = message;
        this.evaluatedMessage = this.message;
        this.httpCode = httpCode;
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
