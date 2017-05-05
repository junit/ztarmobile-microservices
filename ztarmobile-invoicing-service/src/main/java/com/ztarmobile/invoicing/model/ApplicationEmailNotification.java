/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 05/04/17
 */
public class ApplicationEmailNotification extends EmailNotification {
    /**
     * The reason of failure.
     */
    private Throwable reason;
    /**
     * Just a flag to indicate whether the application was success or not.
     */
    private boolean success;

    /**
     * Creates an object that holds the state of the application.
     * 
     * @param success
     *            Was success or not.
     * @param reason
     *            The reason of failure in case success is false, if success is
     *            true, this is ignored.
     */
    public ApplicationEmailNotification(boolean success, Throwable reason) {
        this.reason = reason;
        if (success) {
            this.setSubject("Microservice Invoicing has started successfully!!!");
            this.reason = null;
        } else {
            this.setSubject("Ohh no!!, Microservice Invoicing could not start");
        }
    }

    /**
     * @return the reason
     */
    public Throwable getReason() {
        return reason;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApplicationEmailNotification [reason=" + reason + ", success=" + success + "]";
    }

}
