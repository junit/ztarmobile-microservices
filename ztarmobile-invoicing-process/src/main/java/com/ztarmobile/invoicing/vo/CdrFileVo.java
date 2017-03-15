/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.vo;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/15/17
 */
public class CdrFileVo {
    private long rowId;
    private String sourceFileName;
    private String targetFileName;

    /**
     * @return the rowId
     */
    public long getRowId() {
        return rowId;
    }

    /**
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    /**
     * @return the sourceFileName
     */
    public String getSourceFileName() {
        return sourceFileName;
    }

    /**
     * @param sourceFileName
     *            the sourceFileName to set
     */
    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    /**
     * @return the targetFileName
     */
    public String getTargetFileName() {
        return targetFileName;
    }

    /**
     * @param targetFileName
     *            the targetFileName to set
     */
    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CdrFileVo [rowId=" + rowId + ", sourceFileName=" + sourceFileName + ", targetFileName=" + targetFileName
                + "]";
    }

}
