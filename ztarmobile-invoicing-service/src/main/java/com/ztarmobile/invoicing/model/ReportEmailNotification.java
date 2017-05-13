/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.model;

import java.util.List;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 2.0
 */
public class ReportEmailNotification extends EmailNotification {
    private String receiptName;
    private List<EmailAttachment> content;

    /**
     * @return the receiptName
     */
    public String getReceiptName() {
        return receiptName;
    }

    /**
     * @param receiptName
     *            the receiptName to set
     */
    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportEmailNotification [receiptName=" + receiptName + ", content=" + content + "]";
    }
}
