/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

import java.util.Date;

/**
 * Value object for the request parameters.
 *
 * @author armandorivas
 * @since 03/29/17
 */
public class InvoicingRequest {
    private Date reportFrom;
    private Date reportTo;
    private String product;
    private boolean rerunInvoicing;

    /**
     * @return the reportFrom
     */
    public Date getReportFrom() {
        return reportFrom;
    }

    /**
     * @param reportFrom
     *            the reportFrom to set
     */
    public void setReportFrom(Date reportFrom) {
        this.reportFrom = reportFrom;
    }

    /**
     * @return the reportTo
     */
    public Date getReportTo() {
        return reportTo;
    }

    /**
     * @param reportTo
     *            the reportTo to set
     */
    public void setReportTo(Date reportTo) {
        this.reportTo = reportTo;
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
     * @return the rerunInvoicing
     */
    public boolean isRerunInvoicing() {
        return rerunInvoicing;
    }

    /**
     * @param rerunInvoicing
     *            the rerunInvoicing to set
     */
    public void setRerunInvoicing(boolean rerunInvoicing) {
        this.rerunInvoicing = rerunInvoicing;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InvoicingRequest [reportFrom=" + reportFrom + ", reportTo=" + reportTo + ", product=" + product
                + ", rerunInvoicing=" + rerunInvoicing + "]";
    }

}
