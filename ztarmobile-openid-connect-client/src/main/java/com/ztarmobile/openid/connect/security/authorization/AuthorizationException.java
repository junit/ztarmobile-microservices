package com.ztarmobile.openid.connect.security.authorization;

/**
 * Abstract superclass for all exceptions related to Authentication being
 * invalid for whatever reason.
 *
 * @author Ben Alex
 */
public abstract class AuthorizationException extends RuntimeException {
    // ~ Instance fields
    // ================================================================================================

    private transient Object extraInformation;

    // ~ Constructors
    // ===================================================================================================

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
    }
}
