/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.notification.exception;

/**
 * Exception thown when the value of the balance cannot be calculated.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public class BalanceValueException extends ServiceException {

    /**
     * The serial exception.
     */
    private static final long serialVersionUID = 4082594411545454679L;

    public BalanceValueException(String msg) {
        super(msg);
    }
}
