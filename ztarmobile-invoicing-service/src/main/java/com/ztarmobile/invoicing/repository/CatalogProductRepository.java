/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.repository;

import com.ztarmobile.invoicing.model.CatalogProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Defines a new repository for the products.
 * 
 * @author armandorivas
 * @version %I%, %G%
 * @since 2.0
 */
@RepositoryRestResource(collectionResourceRel = "catalog:products", path = "products")
public interface CatalogProductRepository extends JpaRepository<CatalogProduct, Long> {

}
