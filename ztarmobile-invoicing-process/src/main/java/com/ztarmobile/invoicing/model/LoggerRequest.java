/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

import static com.ztarmobile.invoicing.common.DateUtils.MEDIUM;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/22/17
 */
public class LoggerRequest {
    private long rowId;
    private String product;
    @JsonFormat(pattern = MEDIUM)
    private Date from;
    @JsonFormat(pattern = MEDIUM)
    private Date to;
    private long responseTime;
    private char status;
    private boolean availableReport;
    private String errorDescription;
    @JsonFormat(pattern = MEDIUM)
    private Date requestDate;

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
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the from
     */
    public Date getFrom() {
        return from;
    }

    /**
     * @param from
     *            the from to set
     */
    public void setFrom(Date from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public Date getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(Date to) {
        this.to = to;
    }

    /**
     * @return the responseTime
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * @param responseTime
     *            the responseTime to set
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * @return the status
     */
    public char getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(char status) {
        this.status = status;
    }

    /**
     * @return the availableReport
     */
    public boolean isAvailableReport() {
        return availableReport;
    }

    /**
     * @param availableReport
     *            the availableReport to set
     */
    public void setAvailableReport(boolean availableReport) {
        this.availableReport = availableReport;
    }

    /**
     * @return the errorDescription
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * @param errorDescription
     *            the errorDescription to set
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    /**
     * @return the requestDate
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate
     *            the requestDate to set
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoggerRequest [rowId=" + rowId + ", product=" + product + ", from=" + from + ", to=" + to
                + ", responseTime=" + responseTime + ", status=" + status + ", availableReport=" + availableReport
                + ", errorDescription=" + errorDescription + ", requestDate=" + requestDate + "]";
    }
}
