/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.account.service.impl;

import static com.ztarmobile.exception.AuthorizationMessageErrorCode.INSUFFICIENT_SCOPE;

import com.ztarmobile.account.model.ProtectedResource;
import com.ztarmobile.account.model.ResourceSetScope;
import com.ztarmobile.account.repository.ResourceSetScopeRepository;
import com.ztarmobile.account.service.ResourceSetScopeService;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Direct service implementation to validate the scopes of the client.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Service
public class ResourceSetScopeServiceImpl implements ResourceSetScopeService {
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(ResourceSetScopeServiceImpl.class);

    /**
     * Prefix for the scopes.
     */
    private static final String ZTAR_PREFIX = "ztar_";

    /**
     * ResourceSetScopeRepository to access.
     */
    @Autowired
    private ResourceSetScopeRepository resourceSetScopeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateScope(Set<String> scopes, ProtectedResource protectedResource) {
        log.debug("Filtering effective scopes...");

        String method = protectedResource.getMethod().toString();
        String path = protectedResource.getPath();

        List<ResourceSetScope> access = null;
        for (String scope : scopes) {
            if (scope.startsWith(ZTAR_PREFIX)) {
                access = resourceSetScopeRepository.findByScopeVerbAndResource(scope, method, path);
                if (access.isEmpty()) {
                    continue;
                }
            }
        }
        if (access == null || access.isEmpty()) {
            throw new AuthorizationServiceException(
                    new HttpMessageErrorCodeResolver(INSUFFICIENT_SCOPE, "[" + method + "] " + path));
        }
    }
}
