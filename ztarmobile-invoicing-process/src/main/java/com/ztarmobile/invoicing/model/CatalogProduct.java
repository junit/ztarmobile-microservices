/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @since 03/10/17
 */
public class CatalogProduct {
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
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (cdma ? 1231 : 1237);
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + (int) (rowId ^ (rowId >>> 32));
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CatalogProduct other = (CatalogProduct) obj;
        if (cdma != other.cdma) {
            return false;
        }
        if (product == null) {
            if (other.product != null) {
                return false;
            }
        } else if (!product.equals(other.product)) {
            return false;
        }
        return rowId == other.rowId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CatalogProduct [rowId=" + rowId + ", product=" + product + ", cdma=" + cdma + "]";
    }
}
