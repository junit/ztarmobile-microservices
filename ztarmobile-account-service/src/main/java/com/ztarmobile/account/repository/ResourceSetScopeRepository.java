/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.repository;

import com.ztarmobile.account.model.ResourceSetScope;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * The ResourceSetScope repository that exposes the CRUD operations for the user
 * account.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface ResourceSetScopeRepository extends CrudRepository<ResourceSetScope, Long> {

    List<ResourceSetScope> findByScope(String scope);

    @Query("select r from ResourceSetScope r where r.scope = :scope and r.verb = :verb and r.resource = :resource")
    List<ResourceSetScope> findByScopeVerbAndResource(String scope, String verb, String resource);
}
