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
 * @since 03/10/17
 */
public class CatalogProductVo {
    private long rowId;
    private String product;
    private boolean cdma;

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
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the cdma
     */
    public boolean isCdma() {
        return cdma;
    }

    /**
     * @param cdma
     *            the cdma to set
     */
    public void setCdma(boolean cdma) {
        this.cdma = cdma;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CatalogProductVo [rowId=" + rowId + ", product=" + product + ", cdma=" + cdma + "]";
    }
}
