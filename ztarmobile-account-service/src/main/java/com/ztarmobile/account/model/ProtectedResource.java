/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.model;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class ProtectedResource {
    /**
     * The request method.
     */
    private RequestMethod method;
    /**
     * The path.
     */
    private String path;

    /**
     * Empty constructor.
     */
    public ProtectedResource() {
    }

    /**
     * Creates a new protected resource with a method and a path.
     * 
     * @param method
     *            The request method.
     * @param path
     *            The path.
     */
    public ProtectedResource(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    /**
     * @return the method
     */
    public RequestMethod getMethod() {
        return method;
    }

    /**
     * @param method
     *            the method to set
     */
    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProtectedResource [method=" + method + ", path=" + path + "]";
    }
}
