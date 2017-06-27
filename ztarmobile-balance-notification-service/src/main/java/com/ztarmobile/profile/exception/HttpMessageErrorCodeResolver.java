/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.exception;

/**
 * This class creates an error message with dynamic values.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class HttpMessageErrorCodeResolver {
    /**
     * The message error code.
     */
    private GlobalMessageErrorCode httpMessageErrorCode;
    /**
     * The resolved message.
     */
    private String resolvedMessage;

    /**
     * Creates a message error code resolver with one parameter.
     * 
     * @param httpMessageErrorCode
     *            The message error code.
     * @param param
     *            The parameter.
     * @see HttpMessageErrorCodeResolver#HttpMessageErrorCodeResolver(GlobalMessageErrorCode,
     *      String...)
     */
    public HttpMessageErrorCodeResolver(GlobalMessageErrorCode httpMessageErrorCode, Object param) {
        this(httpMessageErrorCode, new String[] { param.toString() });
    }

    /**
     * Creates a message error code resolver with parameters.
     * 
     * @param httpMessageErrorCode
     *            The message error code.
     * @param params
     *            The parameters.
     * @see HttpMessageErrorCodeResolver#HttpMessageErrorCodeResolver(GlobalMessageErrorCode,
     *      Object)
     */
    public HttpMessageErrorCodeResolver(GlobalMessageErrorCode httpMessageErrorCode, String... params) {
        this.resolvedMessage = resolveMessage(httpMessageErrorCode, params);
        this.httpMessageErrorCode = httpMessageErrorCode;
    }

    /**
     * @return the httpMessageErrorCode
     */
    public GlobalMessageErrorCode getHttpMessageErrorCode() {
        return httpMessageErrorCode;
    }

    /**
     * @return the resolvedMessage
     */
    public String getResolvedMessage() {
        return resolvedMessage;
    }

    /**
     * Resolves the message with parameters.
     * 
     * @param httpMessageErrorCode
     *            The httpMessageErrorCode.
     * @return The final message.
     */
    private static String resolveMessage(GlobalMessageErrorCode httpMessageErrorCode, String... params) {
        String originalMessage = httpMessageErrorCode.getMessage();
        StringBuilder finalMessage = new StringBuilder(originalMessage);

        int index = -1;
        for (String param : params) {
            index = finalMessage.indexOf("?");
            if (index == -1) {
                // no question marks were found
                break;
            }
            finalMessage.replace(index, index + 1, param);
        }
        return finalMessage.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HttpMessageErrorCodeResolver [httpMessageErrorCode=" + httpMessageErrorCode + ", resolvedMessage="
                + resolvedMessage + "]";
    }
}
