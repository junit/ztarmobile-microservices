/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.service.impl;

import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.DUPLICATE_ACCOUNT;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.EMAIL_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.FIRST_NAME_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.LAST_NAME_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.PASSWORD_EMPTY;
import static com.ztarmobile.account.exception.UserAccountMessageErrorCode.UNABLE_CREATE_ACCOUNT;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.springframework.util.StringUtils.hasText;

import com.ztarmobile.account.exception.AccountServiceException;
import com.ztarmobile.account.model.UserAccount;
import com.ztarmobile.account.service.UserAccountService;

import java.util.List;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
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
        if (!hasText(userAccount.getFirstName())) {
            throw new AccountServiceException(FIRST_NAME_EMPTY);
        }
        if (!hasText(userAccount.getLastName())) {
            throw new AccountServiceException(LAST_NAME_EMPTY);
        }
        if (!hasText(userAccount.getEmail())) {
            throw new AccountServiceException(EMAIL_EMPTY);
        }
        if (!hasText(userAccount.getPassword())) {
            throw new AccountServiceException(PASSWORD_EMPTY);
        }

        String jsonString = createKeycloakUser(userAccount);
        log.debug("Token Introspection Response: " + jsonString);
    }

    private String createKeycloakUser(UserAccount userAccount) {
        Keycloak kc = null;
        kc = Keycloak.getInstance(context, keycloakMasterRealm, keycloakUsername, keycloakPassword, keycloakClientId);

        // we make sure we don't have the same user registered
        List<UserRepresentation> usersFound = kc.realm(keycloakRealm).users().search(userAccount.getEmail());
        if (!usersFound.isEmpty()) {
            // we found users with the same email/username
            throw new AccountServiceException(DUPLICATE_ACCOUNT);
        }

        // we try to create the user now.
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userAccount.getPassword());
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userAccount.getEmail());
        user.setFirstName(userAccount.getFirstName());
        user.setLastName(userAccount.getLastName());
        user.setCredentials(asList(credential));
        user.setEnabled(true);
        user.setRealmRoles(asList("ztar_pix"));

        // Create a keycloak user.
        Response response = kc.realm(keycloakRealm).users().create(user);
        if (response.getStatus() != CREATED.getStatusCode()) {
            log.error("Unable to create a new user in keycloak");
            // couldn't create user.
            throw new AccountServiceException(UNABLE_CREATE_ACCOUNT);
        }
        List<UserRepresentation> userJustFound = kc.realm(keycloakRealm).users().search(userAccount.getEmail());
        UserRepresentation userRepresentation = userJustFound.get(0);
        return null;
    }
}
