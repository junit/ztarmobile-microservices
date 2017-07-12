/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jul 2017.
 */
package com.ztarmobile.notification.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class SubscriberUsage {
    private String mdn;
    private String activationDate;
    private String dedicatedAccount1;
    private String dedicatedAccount2;
    private String dedicatedAccount4;
    private String accountBalance;
    private String serviceClass;
    private boolean error;
    private String errorDescription;

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
     * @return the activationDate
     */
    public String getActivationDate() {
        return activationDate;
    }

    /**
     * @param activationDate
     *            the activationDate to set
     */
    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    /**
     * @return the dedicatedAccount1
     */
    public String getDedicatedAccount1() {
        return dedicatedAccount1;
    }

    /**
     * @param dedicatedAccount1
     *            the dedicatedAccount1 to set
     */
    public void setDedicatedAccount1(String dedicatedAccount1) {
        this.dedicatedAccount1 = dedicatedAccount1;
    }

    /**
     * @return the dedicatedAccount2
     */
    public String getDedicatedAccount2() {
        return dedicatedAccount2;
    }

    /**
     * @param dedicatedAccount2
     *            the dedicatedAccount2 to set
     */
    public void setDedicatedAccount2(String dedicatedAccount2) {
        this.dedicatedAccount2 = dedicatedAccount2;
    }

    /**
     * @return the dedicatedAccount4
     */
    public String getDedicatedAccount4() {
        return dedicatedAccount4;
    }

    /**
     * @param dedicatedAccount4
     *            the dedicatedAccount4 to set
     */
    public void setDedicatedAccount4(String dedicatedAccount4) {
        this.dedicatedAccount4 = dedicatedAccount4;
    }

    /**
     * @return the accountBalance
     */
    public String getAccountBalance() {
        return accountBalance;
    }

    /**
     * @param accountBalance
     *            the accountBalance to set
     */
    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * @return the serviceClass
     */
    public String getServiceClass() {
        return serviceClass;
    }

    /**
     * @param serviceClass
     *            the serviceClass to set
     */
    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(boolean error) {
        this.error = error;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SubscriberUsage [mdn=" + mdn + ", activationDate=" + activationDate + ", dedicatedAccount1="
                + dedicatedAccount1 + ", dedicatedAccount2=" + dedicatedAccount2 + ", dedicatedAccount4="
                + dedicatedAccount4 + ", accountBalance=" + accountBalance + ", serviceClass=" + serviceClass
                + ", error=" + error + ", errorDescription=" + errorDescription + "]";
    }
}
