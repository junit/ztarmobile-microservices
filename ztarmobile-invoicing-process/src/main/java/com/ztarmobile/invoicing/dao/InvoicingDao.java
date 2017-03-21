/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.Date;
import java.util.List;

import com.ztarmobile.invoicing.vo.ReportDetailsVo;

/**
 * DAO to handle the operations for the invoicing process.
 *
 * @author armandorivas
 * @since 03/13/17
 */
public interface InvoicingDao {
    /**
     * Cleans up the existing information based on a start and end date
     * including the product.
     * 
     * @param start
     *            The initial date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     */
    void cleanUpInvoicing(Date start, Date end, String product);

    /**
     * Saves the information related to the invoicing based on the initial, end
     * date and a specific product.
     * 
     * @param start
     *            The initial date.
     * @param end
     *            The end date.
     * @param product
     *            The product.
     */
    void saveInvoicing(Date start, Date end, String product);

    /**
     * Return a list of objects for the invoicing report.
     * 
     * @param product
     *            The product.
     * @param start
     *            Initial date.
     * @param end
     *            End date.
     * @return List of objects or null if no information was found.
     */
    List<ReportDetailsVo> generateReport(String product, Date start, Date end);

}
