/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.model;

import java.util.List;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class TransactionsEmailNotification extends EmailNotification {
    private List<EmailAttachment> content;
    private String vendor;
    private String month;

    /**
     * @return the content
     */
    public List<EmailAttachment> getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(List<EmailAttachment> content) {
        this.content = content;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor
     *            the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month
     *            the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TransactionsEmailNotification [content=" + content + ", vendor=" + vendor + ", month=" + month + "]";
    }
}
