/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.dao;

import java.util.List;

import com.ztarmobile.invoicing.model.CatalogProduct;

/**
 * DAO to handle the operations for the products.
 *
 * @author armandorivas
 * @since 03/10/17
 */
public interface CatalogProductDao {
    /**
     * Gets a specific product based on its description.
     * 
     * 
     * @param product
     *            The product.
     * @return The product or null if it was not found.
     */
    CatalogProduct getCatalogProduct(String product);

    /**
     * Gets all the existing products.
     * 
     * @return The list of all the products or an empty list where no products
     *         were found.
     */
    List<CatalogProduct> getCatalogProduct();
}
