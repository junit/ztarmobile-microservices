/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * Utility to handle some common operations.
 *
 * @author armandorivas
 * @since 03/21/17
 */
public class CommonUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void validateInput() {
        // happy path
        CommonUtils.validateInput("Some input");
        // negative scenario
        CommonUtils.validateInput(null);
    }

    @Test
    public void validateInputWithMessage() {
        // happy path
        CommonUtils.validateInput("Some input", "Some exception");
        // negative scenario
        String sameMessage = "my message";
        try {
            String nullValue = null;
            CommonUtils.validateInput(nullValue, sameMessage);
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals(sameMessage, ex.getMessage());
        }
    }
}
