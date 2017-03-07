/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.service;

import java.util.Calendar;
import java.util.Date;

/**
 * Service to handle the files for the cdrs.
 *
 * @author armandorivas
 * @since 03/02/17
 */
public interface CdrFileService {
    /**
     * This method extracts from the source the cdrs and move them into the
     * target directory.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     */
    void extractCdrs(Date start, Date end);

    /**
     * This method extracts from the source the cdrs and move them into the
     * target directory.
     * 
     * @param start
     *            The start date.
     * @param end
     *            The end date.
     */
    void extractCdrs(Calendar start, Calendar end);

    /**
     * This method extracts from the source the cdrs and move them into the
     * target directory. This process works only in the same year.
     * 
     * @param fromMonth
     *            The initial month.
     * @param toMonth
     *            The final month.
     */
    void extractCdrs(int fromMonth, int toMonth);

    /**
     * This method extracts from the source the cdrs and move them into the
     * target directory. This process works only in the same year.
     * 
     * @param month
     *            An specific month.
     */
    void extractCdrs(int month);

    /**
     * This method extracts from the source the cdrs and move them into the
     * target directory. This process works only in the same year and the
     * previous month.
     */
    void extractCdrs();
}
