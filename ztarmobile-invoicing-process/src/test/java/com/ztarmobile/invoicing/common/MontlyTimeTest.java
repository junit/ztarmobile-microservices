/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

/**
 * Utility to handle some common operations.
 *
 * @author armandorivas
 * @since 03/21/17
 */
public class MontlyTimeTest {

    @Test
    public void toStringString() {
        MontlyTime montlyTime = new MontlyTime();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        montlyTime.setStart(start);
        montlyTime.setEnd(end);

        String expected = "MontlyTime [start=" + start.getTime() + ", end=" + end.getTime() + "]";
        Assert.assertEquals(expected, montlyTime.toString());
    }
}
