/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class SubscriberTransaction {
    private String mdn;
    private String total;
    private String paymentDate;

    /**
     * @return the mdn
     */
    public String getMdn() {
        return mdn;
    }

    /**
     * @param mdn
     *            the mdn to set
     */
    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return the paymentDate
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate
     *            the paymentDate to set
     */
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SubscriberTransaction [mdn=" + mdn + ", total=" + total + ", paymentDate=" + paymentDate + "]";
    }
}
