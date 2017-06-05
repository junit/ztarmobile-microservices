/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.repository;

import static com.ztarmobile.account.repository.CustomRepositoryPath.SCOPE_MAPPING;

import com.ztarmobile.account.model.ScopeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Scope repository that exposes the CRUD operations for the user account.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Transactional
@RepositoryRestResource(collectionResourceRel = "scope", path = SCOPE_MAPPING)
public interface ScopeRepository extends JpaRepository<ScopeEntity, String> {

    ScopeEntity findByScope(@Param("scope") String scope);

}
