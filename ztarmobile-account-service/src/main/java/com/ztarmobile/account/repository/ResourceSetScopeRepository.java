/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.repository;

import static com.ztarmobile.account.repository.CustomRepositoryPath.RESOURCE_SET_SCOPE_MAPPING;

import com.ztarmobile.account.model.ResourceSetScope;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * The ResourceSetScope repository that exposes the CRUD operations for the user
 * account.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Transactional
@RepositoryRestResource(collectionResourceRel = "scopes", path = RESOURCE_SET_SCOPE_MAPPING)
public interface ResourceSetScopeRepository extends CrudRepository<ResourceSetScope, Long> {

    List<ResourceSetScope> findByScope(String scope);

    @Query("select r from ResourceSetScope r where r.scope = :scope and r.verb = :verb and r.resource = :resource")
    List<ResourceSetScope> findByScopeVerbAndResource(@Param("scope") String scope, @Param("verb") String method,
            @Param("resource") String path);

    @Query("select r from ResourceSetScope r where r.verb = :verb and r.resource = :resource")
    List<ResourceSetScope> findByVerbAndResource(@Param("verb") String method, @Param("resource") String path);
}
