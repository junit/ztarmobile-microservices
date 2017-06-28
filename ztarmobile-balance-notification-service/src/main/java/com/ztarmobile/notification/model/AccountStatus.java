/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.model;

import java.util.Date;
import java.util.List;

/**
 * The account status.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class AccountStatus extends BaseModel {
    /**
     * the serial number.
     */
    private static final long serialVersionUID = -7121648701135089513L;
    private String balance;
    private Date airtimeExpiryDate;
    private Date serviceRemovalDate;
    private List<DedicatedAccount> dedicatedAccounts;
    private String networkProviderStatus;
    private String networkProviderStatusReason;
    private String networkProviderStatusDate;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Date getAirtimeExpiryDate() {
        return airtimeExpiryDate;
    }

    public void setAirtimeExpiryDate(Date airtimeExpiryDate) {
        this.airtimeExpiryDate = airtimeExpiryDate;
    }

    public Date getServiceRemovalDate() {
        return serviceRemovalDate;
    }

    public void setServiceRemovalDate(Date serviceRemovalDate) {
        this.serviceRemovalDate = serviceRemovalDate;
    }

    public List<DedicatedAccount> getDedicatedAccounts() {
        return dedicatedAccounts;
    }

    public void setDedicatedAccounts(List<DedicatedAccount> dedicatedAccounts) {
        this.dedicatedAccounts = dedicatedAccounts;
    }

    public String getNetworkProviderStatus() {
        return networkProviderStatus;
    }

    public void setNetworkProviderStatus(String networkProviderStatus) {
        this.networkProviderStatus = networkProviderStatus;
    }

    public String getNetworkProviderStatusReason() {
        return networkProviderStatusReason;
    }

    public void setNetworkProviderStatusReason(String networkProviderStatusReason) {
        this.networkProviderStatusReason = networkProviderStatusReason;
    }

    public String getNetworkProviderStatusDate() {
        return networkProviderStatusDate;
    }

    public void setNetworkProviderStatusDate(String networkProviderStatusDate) {
        this.networkProviderStatusDate = networkProviderStatusDate;
    }
}
