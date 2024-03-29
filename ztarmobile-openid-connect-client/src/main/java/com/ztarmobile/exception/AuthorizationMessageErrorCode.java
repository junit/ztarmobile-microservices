/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    METHOD_NOT_SUPPORTED (80000, "Request method '?' not allowed for this resource: ?", 405),
    AUTHORIZATION_ERROR (80001, "Authorization Error", INTERNAL_SERVER_ERROR),
    NO_ACCESS_TOKEN_FOUND (80002, "Missing access token in the header or payload of the request. " + AUTHORIZATION + ": bearer yourtoken..", BAD_REQUEST),
    UNAUTHORIZED_ACCESS (80003, "Full authentication is required to access this resource", UNAUTHORIZED),
    AUTH_SERVER_ACCESS (80004, "Unable to have access to the Authorization Server Admin, please check your credentials", UNAUTHORIZED),
    INVALID_BASIC_CRED (80005, "Unable to authenticate with the credentials provided", UNAUTHORIZED),
    UNAUTHORIZED_BASIC (80006, "Basic authentication is required to access this resource", UNAUTHORIZED),
    INVALID_BASIC (80007, "Unable to decode Basic authentication", UNAUTHORIZED),
    NO_ACCESS_EXTERNAL_RESOURCE (80008, "Unable to get access to external resource [?]", INTERNAL_SERVER_ERROR),
    NO_VALID_JSON (80009, "Token Endpoint did not return a JSON object: ?", INTERNAL_SERVER_ERROR),
    NO_ACTIVE_AVAILABLE (80010, "Retrospection Endpoint did not return an active element: ?", INTERNAL_SERVER_ERROR),
    NO_SCOPE_AVAILABLE (800011, "No scope available for the access token provided", INTERNAL_SERVER_ERROR),
    NO_SCOPE_FOUND (80012, "Retrospection Endpoint returned an empty scope element", INTERNAL_SERVER_ERROR),
    NO_ROLE_AVAILABLE (80013, "Retrospection Endpoint did not return a 'realm_access' element with a valid role element: ?", INTERNAL_SERVER_ERROR),
    NO_ACTIVE_TOKEN (80014, "Access token is no longer active or valid", UNAUTHORIZED),
    INSUFFICIENT_SCOPE (80015, "Insufficient Scope to access this resource: ?", FORBIDDEN);

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
    AuthorizationMessageErrorCode(int code, String message, Response.Status httpStatus) {
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
    AuthorizationMessageErrorCode(int code, String message, int httpCode) {
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
