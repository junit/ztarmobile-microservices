/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.service.impl;

import static java.util.Arrays.asList;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ztarmobile.account.model.UserAccount;
import com.ztarmobile.account.service.UserAccountService;

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

    @Value("${account.openid.keycloak.users-uri}")
    private String keyCloakUsersUri;

    @Value("${account.openid.context}")
    private String context;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewUserAccount(UserAccount userAccount) {
        System.out.println(keyCloakUsersUri);
        // validates the input parameters.
        // creates a new user in the authorization server.

        String jsonString = createKeycloakUser2(keyCloakUsersUri);
        log.debug("Token Introspection Response: " + jsonString);
    }

    private String createKeycloakUser2(String keyCloakUsersUri2) {
        Keycloak keycloak = Keycloak.getInstance(context, "master", "keycloak", "keycloak", "admin-cli");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue("test123");
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername("testuser");
        user.setFirstName("Testssssss");
        user.setLastName("User");
        user.setCredentials(asList(credential));
        user.setEnabled(true);
        user.setRealmRoles(asList("admin"));

        // Create testuser
        keycloak.realm("demo").users().create(user);
        return null;
    }
}
