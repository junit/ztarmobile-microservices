/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.service.impl;

import static com.ztarmobile.account.common.CommonUtils.P_MAX;
import static com.ztarmobile.account.common.CommonUtils.P_MIN;
import static com.ztarmobile.account.common.CommonUtils.validateEmail;
import static com.ztarmobile.account.common.CommonUtils.validatePassword;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.DUPLICATE_ACCOUNT;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.EMAIL_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.EMAIL_INVALID;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.EMAIL_LENGTH;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.FIRST_NAME_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.FIRST_NAME_LENGTH;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.LAST_NAME_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.LAST_NAME_LENGTH;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.PASSWORD_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.PASSWORD_INVALID;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.PASSWORD_LENGTH;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.UNABLE_CREATE_ACCOUNT;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.springframework.util.StringUtils.hasText;

import com.ztarmobile.account.exception.AccountServiceException;
import com.ztarmobile.account.model.UserAccount;
import com.ztarmobile.account.service.UserAccountService;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Direct implementation for the user management.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

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

    @Value("${account.openid.keycloak.role-prefix}")
    private String keycloakRolePrefix;

    // value injected manually
    private String clientId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewUserAccount(UserAccount userAccount) {
        // validates the input parameters.
        if (userAccount == null) {
            throw new AccountServiceException("User account object is null");
        }
        // creates a new user in the authorization server.
        // validation for the first name.
        if (!hasText(userAccount.getFirstName())) {
            throw new AccountServiceException(FIRST_NAME_EMPTY);
        } else if (userAccount.getFirstName().length() > 50) {
            throw new AccountServiceException(new HttpMessageErrorCodeResolver(FIRST_NAME_LENGTH, 50));
        }

        // validation for the last name.
        if (!hasText(userAccount.getLastName())) {
            throw new AccountServiceException(LAST_NAME_EMPTY);
        } else if (userAccount.getLastName().length() > 50) {
            throw new AccountServiceException(new HttpMessageErrorCodeResolver(LAST_NAME_LENGTH, 50));
        }

        // validation for the user email.
        if (!hasText(userAccount.getEmail())) {
            throw new AccountServiceException(EMAIL_EMPTY);
        } else if (userAccount.getEmail().length() > 50) {
            throw new AccountServiceException(new HttpMessageErrorCodeResolver(EMAIL_LENGTH, 50));
        } else if (!validateEmail(userAccount.getEmail())) {
            throw new AccountServiceException(EMAIL_INVALID);
        }

        // validation for the password.
        if (!hasText(userAccount.getPassword())) {
            throw new AccountServiceException(PASSWORD_EMPTY);
        } else if (userAccount.getPassword().length() < P_MIN || userAccount.getPassword().length() > P_MAX) {
            throw new AccountServiceException(
                    new HttpMessageErrorCodeResolver(PASSWORD_LENGTH, String.valueOf(P_MIN), String.valueOf(P_MAX)));
        } else if (!validatePassword(userAccount.getPassword())) {
            throw new AccountServiceException(PASSWORD_INVALID);
        }

        UserAccount newUserCreated = createKeycloakUser(userAccount);
        userAccount.setUserId(newUserCreated.getUserId());
        log.debug("User Created: " + newUserCreated);
    }

    /**
     * Creates a new user based on the user account info provided.
     * 
     * @param userAccount
     *            The user account.
     * @return The user created.
     */
    private UserAccount createKeycloakUser(UserAccount userAccount) {
        Keycloak kc = null;
        kc = Keycloak.getInstance(context, keycloakMasterRealm, keycloakUsername, keycloakPassword, keycloakClientId);
        // finds the existing roles for this user based on the client
        // credentials.
        // finds the roles
        List<String> effectiveRoles = findEffectiveRolesByClient(kc);

        List<RoleRepresentation> rolesToAdd = new ArrayList<>();
        for (String effectiveRole : effectiveRoles) {
            rolesToAdd.add(kc.realm(keycloakRealm).roles().get(effectiveRole).toRepresentation());
        }

        // we try to create the user now.
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userAccount.getEmail());
        user.setFirstName(userAccount.getFirstName());
        user.setLastName(userAccount.getLastName());
        user.setEmail(userAccount.getEmail());
        user.setEnabled(true);

        // Create a keycloak user.
        Response response = kc.realm(keycloakRealm).users().create(user);
        String userId = getCreatedId(response);

        // Assign realm roles to user
        kc.realm(keycloakRealm).users().get(userId).roles().realmLevel().add(rolesToAdd);

        // creates the password
        UserResource userResource = kc.realm(keycloakRealm).users().get(userId);
        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(userAccount.getPassword());
        newCredential.setTemporary(false);
        userResource.resetPassword(newCredential);

        userAccount.setUserId(userId);
        return userAccount;
    }

    /**
     * Find the role based on the client configuration. The role must be
     * configure as follows: <keycloakRolePrefix>:nvno:role_name
     * 
     * @param kc
     *            KeyCloak configuration.
     * 
     * @return The list of roles or some exception when the role is not present
     *         in the configuration.
     */
    private List<String> findEffectiveRolesByClient(Keycloak kc) {
        if (clientId == null) {
            throw new IllegalArgumentException("Unsatified condition, clientId must not be null");
        }
        List<String> effectiveRoles = new ArrayList<>();

        ClientResource clientResource = kc.realm(keycloakRealm).clients().get(clientId);
        RoleMappingResource mappingResource = clientResource.getScopeMappings();

        RoleScopeResource roleScopeResource = mappingResource.realmLevel();
        List<RoleRepresentation> list = roleScopeResource.listEffective();
        for (RoleRepresentation roleRepresentation : list) {
            // we only know those roles with a prefix.
            if (roleRepresentation.getName().startsWith(keycloakRolePrefix)) {
                effectiveRoles.add(roleRepresentation.getName());
            }
        }
        return effectiveRoles;
    }

    /**
     * Try to get the user id, if the user could not be created, then an
     * exception is thrown.
     * 
     * @param response
     *            The response after the user was created.
     * @return The userId.
     */
    private String getCreatedId(Response response) {
        URI location = response.getLocation();
        if (response.getStatus() != CREATED.getStatusCode()) {
            if (response.getStatus() == CONFLICT.getStatusCode()) {
                // we found users with the same email/userName
                log.warn("A user might be duplicated ");
                throw new AccountServiceException(DUPLICATE_ACCOUNT);
            }
            log.error("Unable to create a new user in keycloak");
            // couldn't create user.
            throw new AccountServiceException(UNABLE_CREATE_ACCOUNT);
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * Sets the clientId used for this service.
     * 
     * @param clientId
     *            The clientId.
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
