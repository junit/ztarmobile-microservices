/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.exception;

/**
 * This Adds the HTTP status code in the message.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface HttpMessageErrorCode extends MessageErrorCode {
    /**
     * Gets the HTTP code related to the message.
     *
     * @return The HTTP code.
     */
    int getHttpCode();
}
