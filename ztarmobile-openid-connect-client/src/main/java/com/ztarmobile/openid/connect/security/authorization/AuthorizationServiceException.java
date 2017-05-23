package com.ztarmobile.openid.connect.security.authorization;

import com.ztarmobile.exception.HttpMessageErrorCode;

/**
 * Thrown if an authorization request could not be processed due to a system
 * problem.
 * <p>
 * This might be thrown if a backend authentication repository is unavailable,
 * for example.
 *
 * @author Ben Alex
 */
public class AuthorizationServiceException extends AuthorizationException {
    // ~ Constructors
    // ===================================================================================================
    private int responseStatus = 401;
    private HttpMessageErrorCode httpMessageErrorCode;

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
}
