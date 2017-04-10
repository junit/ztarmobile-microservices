/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.List;

import com.ztarmobile.invoicing.model.CatalogEmail;

/**
 * DAO to handle the operations for the email's.
 *
 * @author armandorivas
 * @since 03/10/17
 */
public interface CatalogEmailDao {

    /**
     * Gets all the existing email's and their associated products.
     * 
     * @return The list of all the email's or an empty list where no email's
     *         were found.
     */
    List<CatalogEmail> getCatalogEmail();
}
