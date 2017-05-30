/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.account.service;

import com.ztarmobile.account.model.ProtectedResource;

import java.util.Set;

/**
 * Service that calculates and perform the invoicing process.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface ResourceSetScopeService {
    /**
     * Validate the scope of the current request.
     * 
     * @param scopes
     *            The set of allowed scopes.
     * @param protectedResource
     *            The protected resource.
     */
    void validateScope(Set<String> scopes, ProtectedResource protectedResource);
}
