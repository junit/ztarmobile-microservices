/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.openid.connect;

import com.ztarmobile.util.ProtectedResource;

import java.util.List;
import java.util.Set;

/**
 * Service that manages the scopes.
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

    /**
     * Gets the scopes associated to this protected resource.
     * 
     * @param protectedResource
     *            The protected resource.
     * @return List of scopes.
     */
    List<String> getScopesByResource(ProtectedResource protectedResource);

    /**
     * Synchronize the roles to scopes.
     */
    void syncFromRolesToScope();
}
