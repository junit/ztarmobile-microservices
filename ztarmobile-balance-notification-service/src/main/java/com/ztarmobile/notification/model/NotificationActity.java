/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.notification.model;

import java.util.Date;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/10/17
 */
public class NotificationActity {
    private String mdn;
    private Date fileDate;

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
     * @return the fileDate
     */
    public Date getFileDate() {
        return fileDate;
    }

    /**
     * @param fileDate
     *            the fileDate to set
     */
    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NotificationActity [mdn=" + mdn + ", fileDate=" + fileDate + "]";
    }
}
