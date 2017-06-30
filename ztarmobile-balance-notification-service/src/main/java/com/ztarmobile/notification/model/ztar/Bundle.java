/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model.ztar;

import java.io.Serializable;
import java.util.Date;

/**
 * The account status.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class Bundle implements Serializable {
    /**
     * the serial number.
     */
    private static final long serialVersionUID = -7121648701135089513L;
    private String bundleId;
    private int bundleRowId;
    private Date bundleRenewalDate;
    private String bundleStatus;
    private int billingRowId;
    private int renewals;

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleRenewalDate(Date bundleRenewalDate) {
        this.bundleRenewalDate = bundleRenewalDate;
    }

    public Date getBundleRenewalDate() {
        return bundleRenewalDate;
    }

    public void setBundleStatus(String bundleStatus) {
        this.bundleStatus = bundleStatus;
    }

    public String getBundleStatus() {
        return bundleStatus;
    }

    public void setBundleRowId(int bundleRowId) {
        this.bundleRowId = bundleRowId;
    }

    public int getBundleRowId() {
        return bundleRowId;
    }

    public void setBillingRowId(int billingRowId) {
        this.billingRowId = billingRowId;
    }

    public int getBillingRowId() {
        return billingRowId;
    }

    public int getRenewals() {
        return renewals;
    }

    public void setRenewals(int renewals) {
        this.renewals = renewals;
    }
}
