/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.notification.model;

import java.util.List;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class UsageEmailNotification extends EmailNotification {
    /**
     * The bundle id.
     */
    private String bundleId;
    private List<EmailAttachment> content;

    /**
     * @return the bundleId
     */
    public String getBundleId() {
        return bundleId;
    }

    /**
     * @param bundleId
     *            the bundleId to set
     */
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
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
        return "UsageEmailNotification [bundleId=" + bundleId + "]";
    }
}
