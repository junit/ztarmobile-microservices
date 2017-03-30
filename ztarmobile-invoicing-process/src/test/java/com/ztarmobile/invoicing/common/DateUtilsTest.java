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
 * Test class for DateUtils
 *
 * @author armandorivas
 * @since 03/30/17
 */
public class DateUtilsTest {

    // @Test
    public void getMinimunDayOfMonth() {
        // happy path
        Calendar calendar1 = DateUtils.getMinimunDayOfMonth(Calendar.JANUARY);
        Assert.assertEquals(1, calendar1.get(Calendar.DAY_OF_MONTH));

        // Another rigurious test :)
        Calendar calendar2 = DateUtils.getMinimunDayOfMonth(Calendar.JANUARY);
        Assert.assertNotEquals(2, calendar2.get(Calendar.DAY_OF_MONTH));
    }

    // @Test
    public void getMaximumDayOfMonth() {
        // happy path
        Calendar calendar1 = DateUtils.getMaximumDayOfMonth(Calendar.JANUARY);
        Assert.assertEquals(31, calendar1.get(Calendar.DAY_OF_MONTH));

    }

    @Test
    public void getMaximum() {
        // happy path
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        calendar.set(Calendar.MONTH, 2);
        System.out.println(calendar.getTime());
    }

}
