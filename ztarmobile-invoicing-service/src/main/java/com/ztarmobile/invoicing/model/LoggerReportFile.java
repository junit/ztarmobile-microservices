/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/17/17
 */
public class LoggerReportFile {
    private long rowId;
    private char statusAllocations;
    private char statusUsage;

    /**
     * @return the rowId
     */
    public long getRowId() {
        return rowId;
    }

    /**
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    /**
     * @return the statusAllocations
     */
    public char getStatusAllocations() {
        return statusAllocations;
    }

    /**
     * @param statusAllocations
     *            the statusAllocations to set
     */
    public void setStatusAllocations(char statusAllocations) {
        this.statusAllocations = statusAllocations;
    }

    /**
     * @return the statusUsage
     */
    public char getStatusUsage() {
        return statusUsage;
    }

    /**
     * @param statusUsage
     *            the statusUsage to set
     */
    public void setStatusUsage(char statusUsage) {
        this.statusUsage = statusUsage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoggerReportFile [rowId=" + rowId + ", statusAllocations=" + statusAllocations + ", statusUsage="
                + statusUsage + "]";
    }

}
