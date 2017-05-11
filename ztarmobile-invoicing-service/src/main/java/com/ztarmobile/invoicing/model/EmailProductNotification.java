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
 * @since 04/09/17
 */
public class EmailProductNotification {
    private long rowId;
    private CatalogProduct catalogProduct;
    private boolean notificationEnabled;

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
     * @return the catalogProduct
     */
    public CatalogProduct getCatalogProduct() {
        return catalogProduct;
    }

    /**
     * @param catalogProduct
     *            the catalogProduct to set
     */
    public void setCatalogProduct(CatalogProduct catalogProduct) {
        this.catalogProduct = catalogProduct;
    }

    /**
     * @return the notificationEnabled
     */
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    /**
     * @param notificationEnabled
     *            the notificationEnabled to set
     */
    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EmailProductNotification [rowId=" + rowId + ", catalogProduct=" + catalogProduct
                + ", notificationEnabled=" + notificationEnabled + "]";
    }
}
