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
 * The dedicated account.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class DedicatedAccount implements Serializable {
    public static final int ACCOUNT_ID_VOICE = 1;
    public static final int ACCOUNT_ID_SMS = 2;
    public static final int ACCOUNT_ID_MMS = 3;
    public static final int ACCOUNT_ID_DATA = 4;
    public static final int ACCOUNT_ID_US_IDD_PREFERRED = 5;
    public static final int ACCOUNT_ID_HIGHDATA = 6;
    public static final int ACCOUNT_ID_LOWDATA = 7;
    public static final int ACCOUNT_ID_INTLSMS = 8;
    public static final int ACCOUNT_ID_INTLVOICE = 9;

    /**
     * the serial number.
     */
    private static final long serialVersionUID = -7121648701135089513L;

    private int accountId;
    private String accountBalance;
    private Date expiryDate;
    private String units;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
