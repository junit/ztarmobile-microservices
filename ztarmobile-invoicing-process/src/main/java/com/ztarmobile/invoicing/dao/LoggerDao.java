/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.Date;
import java.util.List;

import com.ztarmobile.invoicing.model.LoggerCdrFile;
import com.ztarmobile.invoicing.model.LoggerReportFile;
import com.ztarmobile.invoicing.model.LoggerRequest;
import com.ztarmobile.invoicing.model.LoggerStatus;
import com.ztarmobile.invoicing.model.Phase;

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
    LoggerCdrFile getCdrFileProcessed(String fileName);

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
     * @param product
     *            The product.
     * @param reportDate
     *            The report date.
     * 
     * 
     * @return The report file or null if this object was not found.
     */
    LoggerReportFile getReportFileProcessed(String product, Date reportDate);

    /**
     * Saves or updates a record to indicate that a file has been invoiced.
     * 
     * @param product
     *            The product description.
     * @param reportDate
     *            The date when the record was processed.
     * @param phase
     *            The phase.
     * @param byMonth
     *            Whether the table will be updated by day or month, if it's by
     *            month, the days are ignored.
     * @see LoggerDao#saveOrUpdateReportFileProcessed(String, Date, Phase,
     *      boolean, String)
     */
    void saveOrUpdateReportFileProcessed(String product, Date reportDate, Phase phase, boolean byMonth);

    /**
     * Saves or updates a record to indicate that a file has been invoiced but
     * during the process an error occurred.
     * 
     * @param product
     *            The product description.
     * @param reportDate
     *            The date when the record was processed.
     * @param phase
     *            The phase.
     * @param byMonth
     *            Whether the table will be updated by day or month, if it's by
     *            month, the days are ignored.
     * @param errorDescription
     *            Error description.
     * @see LoggerDao#saveOrUpdateReportFileProcessed(String, Date, Phase,
     *      boolean)
     */
    void saveOrUpdateReportFileProcessed(String product, Date reportDate, Phase phase, boolean byMonth,
            String errorDescription);

    /**
     * Saves or updates a record to indicate that the invoicing process has
     * completed or is still in progress.
     * 
     * @param rowId
     *            If this rowId is 0, then the record is inserted, otherwise,
     *            it's updated.
     * @param product
     *            The product description.
     * @param reportDateFrom
     *            The initial date.
     * @param reportDateTo
     *            The end date.
     * @param totalTime
     *            Total time in milliseconds that the process took to complete.
     * @param status
     *            The status of the process.
     * @return The new generated id if it was an insert operation or the amount
     *         of records updated when the operation was update.
     * @see LoggerDao#saveOrUpdateInvoiceProcessed(long, String, Date, Date,
     *      long, LoggerStatus, String, String)
     */
    long saveOrUpdateInvoiceProcessed(long rowId, String product, Date reportDateFrom, Date reportDateTo,
            long totalTime, LoggerStatus status);

    /**
     * Saves or update a record to indicate that the invoicing process has
     * completed or in progress but during the process an error occurred.
     * 
     * @param rowId
     *            If this rowId is 0, then the record is inserted, otherwise,
     *            it's updated.
     * @param product
     *            The product description.
     * @param reportDateFrom
     *            The initial date.
     * @param reportDateTo
     *            The end date.
     * @param totalTime
     *            Total time in milliseconds that the process took to complete.
     * @param status
     *            The status of the process.
     * @param errorDescription
     *            Error description.
     * @param friendlyErrorDescription
     *            A friendly error description with a real non thecnical
     *            meaning.
     * @return The new generated id if it was an insert operation or the amount
     *         of records updated when the operation was update.
     * @see LoggerDao#saveOrUpdateInvoiceProcessed(long, String, Date, Date,
     *      long, LoggerStatus)
     */
    long saveOrUpdateInvoiceProcessed(long rowId, String product, Date reportDateFrom, Date reportDateTo,
            long totalTime, LoggerStatus status, String errorDescription, String friendlyErrorDescription);

    /**
     * Get a list of all the transactions made/requested when the invoicing
     * process ends.
     * 
     * @param max
     *            The maximum of records to be returned.
     * @return List of records.
     */
    List<LoggerRequest> getInvoiceProcessed(int max);

    /**
     * This method test whether the invoicing process is in certain status, if
     * the status matches, then returns true, otherwise returns false.
     * 
     * @param status
     *            The status of the process.
     * @return true, the status matches, otherwise returns false.
     */
    boolean isInvoiceInStatus(LoggerStatus status);
}
