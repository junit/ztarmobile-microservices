/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import com.ztarmobile.invoicing.vo.CdrFileVo;

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
     * @return The cdr file or null if this object was not found.
     */
    CdrFileVo getFileProcessed(String fileName);

    /**
     * Saves a record to indicate that a file has been saved.
     * 
     * @param sourceFileName
     *            The source file name.
     * @param targetFileName
     *            The target file name.
     * @param type
     *            The file of the file.
     */
    void saveFileProcessed(String sourceFileName, String targetFileName, char type);
}
