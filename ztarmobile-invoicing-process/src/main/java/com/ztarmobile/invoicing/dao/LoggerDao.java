/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.Date;

import com.ztarmobile.invoicing.vo.LoggerCdrFileVo;
import com.ztarmobile.invoicing.vo.LoggerReportFileVo;

/**
 * DAO to handle the operations for the transactions.
 *
 * @author armandorivas
 * @since 03/14/17
 */
public interface LoggerDao {
    /**
     * States whether the file has been processed or not.
     * 
     * 
     * @param fileName
     *            The file name.
     * @return The CDR file or null if this object was not found.
     */
    LoggerCdrFileVo getCdrFileProcessed(String fileName);

    /**
     * Saves or updates a record to indicate that a file has been loaded
     * successfully.
     * 
     * @param sourceFileName
     *            The source file name.
     * @param targetFileName
     *            The target file name.
     * @param type
     *            The file of the file.
     * @see LoggerDao#saveOrUpdateCdrFileProcessed(String, String, char, String)
     */
    void saveOrUpdateCdrFileProcessed(String sourceFileName, String targetFileName, char type);

    /**
     * Saves or updates a record to indicate that a file has been loaded but
     * during the process an error occurred.
     * 
     * @param sourceFileName
     *            The source file name.
     * @param targetFileName
     *            The target file name.
     * @param type
     *            The file of the file.
     * @param errorDescription
     *            Error description.
     * @see LoggerDao#saveOrUpdateCdrFileProcessed(String, String, char)
     */
    void saveOrUpdateCdrFileProcessed(String sourceFileName, String targetFileName, char type, String errorDescription);

    /**
     * States whether the invoice record has been processed or not.
     * 
     * @return The report file or null if this object was not found.
     */
    LoggerReportFileVo getReportFileProcessed();

    /**
     * Saves or updates a record to indicate that a file has been invoiced.
     * 
     * @param product
     *            The product description.
     * @param invoiceDate
     *            The date when the record was processed.
     */
    void saveOrUpdateReportFileProcessed(String product, Date invoiceDate);

    /**
     * Saves or updates a record to indicate that a file has been invoiced but
     * during the process an error occurred.
     * 
     * @param product
     *            The product description.
     * @param invoiceDate
     *            The date when the record was processed.
     * @param errorDescription
     *            Error description.
     */
    void saveOrUpdateReportFileProcessed(String product, Date invoiceDate, String errorDescription);
}
