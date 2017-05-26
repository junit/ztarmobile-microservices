/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.exception;

import java.util.Arrays;

/**
 * This class creates an error message with dynamic values.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class HttpMessageErrorCodeResolver {
    private HttpMessageErrorCode httpMessageErrorCode;
    private String[] params;

    public HttpMessageErrorCodeResolver(HttpMessageErrorCode httpMessageErrorCode, Object param) {
        this.httpMessageErrorCode = httpMessageErrorCode;
        this.params = new String[1];
        this.params[0] = param.toString();
    }

    public HttpMessageErrorCodeResolver(HttpMessageErrorCode httpMessageErrorCode, String... params) {
        this.httpMessageErrorCode = httpMessageErrorCode;
        this.params = params;
    }

    /**
     * @return the httpMessageErrorCode
     */
    public HttpMessageErrorCode getHttpMessageErrorCode() {
        return httpMessageErrorCode;
    }

    /**
     * @param httpMessageErrorCode
     *            the httpMessageErrorCode to set
     */
    public void setHttpMessageErrorCode(HttpMessageErrorCode httpMessageErrorCode) {
        this.httpMessageErrorCode = httpMessageErrorCode;
    }

    /**
     * @return the params
     */
    public String[] getParams() {
        return params;
    }

    /**
     * @param params
     *            the params to set
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HttpMessageErrorCodeResolver [httpMessageErrorCode=" + httpMessageErrorCode + ", params="
                + Arrays.toString(params) + "]";
    }
}
