/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

/**
 * DAO to handle the operations for the cdrs files.
 *
 * @author armandorivas
 * @since 03/14/17
 */
public interface CdrFileDao {
    /**
     * States whether the file has been processed or not.
     * 
     * 
     * @param fileName
     *            The file name.
     * @return true, the file has been processed.
     */
    boolean isFileProcessed(String fileName);

    /**
     * Saves a record to indicate that a file has been saved.
     * 
     * @param fileName
     *            The file name.
     * @param type
     *            The file of the file.
     */
    void saveFileProcessed(String fileName, char type);
}
