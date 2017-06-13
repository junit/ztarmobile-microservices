/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.exception;

/**
 * This Adds the message error with an existing code.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface MessageErrorCode extends ErrorCode {
    /**
     * Gets the message description.
     *
     * @return The message.
     */
    String getMessage();

    /**
     * Gets the evaluated message.
     * 
     * @return The evaluated message.
     */
    String getEvaluatedMessage();

    /**
     * Sets the evaluated message description.
     * 
     * @param evaluatedMessage
     *            The evaluated message description.
     */
    void setEvaluatedMessage(String evaluatedMessage);
}
