/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.service;

import com.ztarmobile.account.model.UserAccount;

/**
 * Service that handles the account management.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface UserAccountService {
    /**
     * Creates a new account for the user, this account will be created as a new
     * user in the Authorization Server.
     * 
     * @param userAccount
     *            A new account.
     */
    void createNewUserAccount(UserAccount userAccount);

}
