/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

import static com.ztarmobile.invoicing.common.CommonUtils.toJson;

import org.springframework.hateoas.ResourceSupport;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/27/17
 */
public class Response extends ResourceSupport {
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    private int status = SUCCESS;
    private String description;
    private String detail;

    /**
     * Creates a success response with an empty body.
     */
    public Response() {
        this.detail = "";
    }

    /**
     * Creates a success response with a given object.
     * 
     * @param response
     *            A non null object.
     */
    public Response(Object response) {
        this.detail = toJson(response);
    }

    /**
     * Creates a success response with a given string.
     * 
     * @param response
     *            A non null string.
     */
    public Response(String response) {
        this.description = response;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     *            the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @param detail
     *            the detail to set
     */
    public void setDetail(Object detail) {
        this.detail = toJson(detail);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Response [status=" + status + ", description=" + description + ", detail=" + detail + "]";
    }

}
