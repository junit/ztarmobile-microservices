/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.account.service.impl;

import static com.ztarmobile.exception.AuthorizationMessageErrorCode.INSUFFICIENT_SCOPE;

import com.ztarmobile.account.model.ResourceSetEntity;
import com.ztarmobile.account.model.ScopeEntity;
import com.ztarmobile.account.repository.ResourceSetRepository;
import com.ztarmobile.account.repository.ScopeRepository;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.openid.connect.ResourceSetScopeService;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;
import com.ztarmobile.util.ProtectedResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${account.openid.context}")
    private String context;

    @Value("${account.openid.keycloak.realm}")
    private String keycloakRealm;

    @Value("${account.openid.keycloak.master-realm}")
    private String keycloakMasterRealm;

    @Value("${account.openid.keycloak.username}")
    private String keycloakUsername;

    @Value("${account.openid.keycloak.password}")
    private String keycloakPassword;

    @Value("${account.openid.keycloak.client-id}")
    private String keycloakClientId;

    /**
     * Prefix for the scopes.
     */
    @Value("${account.openid.keycloak.role-prefix}")
    private String keycloakRolePrefix;

    /**
     * ResourceSetRepository to access.
     */
    @Autowired
    private ResourceSetRepository resourceSetRepository;

    /**
     * ScopeRepository to access.
     */
    @Autowired
    private ScopeRepository scopeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateScope(Set<String> scopes, ProtectedResource protectedResource) {
        log.debug("Filtering effective scopes...");

        String method = protectedResource.getMethod().toString();
        String path = protectedResource.getPath();

        List<ResourceSetEntity> access = null;
        for (String scope : scopes) {
            if (scope.startsWith(keycloakRolePrefix)) {
                access = resourceSetRepository.findByScopeVerbAndResource(scope, method, path);
                if (access.isEmpty()) {
                    continue;
                } else {
                    // we found the scope
                    break;
                }
            }
        }
        if (access == null || access.isEmpty()) {
            throw new AuthorizationServiceException(
                    new HttpMessageErrorCodeResolver(INSUFFICIENT_SCOPE, "[" + method + "] " + path));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getScopesByResource(ProtectedResource protectedResource) {
        log.debug("Finding scopes for requested resource...");

        String method = protectedResource.getMethod().toString();
        String path = protectedResource.getPath();

        List<ResourceSetEntity> access = resourceSetRepository.findByVerbAndResource(method, path);
        List<String> scopes = new ArrayList<>();
        for (ResourceSetEntity scope : access) {
            scopes.add(scope.getScope().getScope());
        }
        return scopes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void syncFromRolesToScope() {
        Keycloak kc = null;
        kc = Keycloak.getInstance(context, keycloakMasterRealm, keycloakUsername, keycloakPassword, keycloakClientId);
        RolesResource rolesResource = kc.realm(keycloakRealm).roles();
        for (RoleRepresentation roleRepresentation : rolesResource.list()) {
            if (roleRepresentation.getName().startsWith(keycloakRolePrefix)) {
                ScopeEntity found = scopeRepository.findByScope(roleRepresentation.getName());
                if (found == null) {
                    // saves the scope.
                    ScopeEntity scopeEntity = new ScopeEntity();
                    scopeEntity.setScope(roleRepresentation.getName());
                    scopeEntity.setDescription(roleRepresentation.getDescription());

                    scopeRepository.saveAndFlush(scopeEntity);
                } else {
                    // updates the scope.
                    found.setDescription(roleRepresentation.getDescription());
                    scopeRepository.saveAndFlush(found);
                }
            }
        }
    }
}
