/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model;

import java.io.Serializable;

/**
 * The base model.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class BaseModel implements Serializable {
    public static final int SUCCESS = 0;
    public static final int UNKNOWN_FATAL = -1;
    public static final int ERICSSON_ERROR = -2;
    public static final int ROGERS_ERROR = -3;
    public static final int APPLICATIVE = 1;

    /**
     * the serial number.
     */
    private static final long serialVersionUID = -5243780844735123314L;
    private int status;
    private String statusDescription;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
