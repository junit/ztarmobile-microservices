/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.oauth2.model;

import com.google.gson.JsonObject;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWT;
import com.ztarmobile.oauth2.model.ClientDetailsEntity.AppType;
import com.ztarmobile.oauth2.model.ClientDetailsEntity.AuthMethod;
import com.ztarmobile.oauth2.model.ClientDetailsEntity.SubjectType;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

/**
 * A registered client.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class RegisteredClient {

    // these fields are needed in addition to the ones in ClientDetailsEntity
    private String registrationAccessToken;
    private String registrationClientUri;
    private Date clientSecretExpiresAt;
    private Date clientIdIssuedAt;
    private ClientDetailsEntity client;
    private JsonObject src;

    /**
     *
     */
    public RegisteredClient() {
        this.client = new ClientDetailsEntity();
    }

    /**
     * @param client
     *            The client details entity.
     */
    public RegisteredClient(ClientDetailsEntity client) {
        this.client = client;
    }

    /**
     * @param client
     *            The client details entity.
     * @param registrationAccessToken
     *            The registration access token.
     * @param registrationClientUri
     *            The registration client uri.
     */
    public RegisteredClient(ClientDetailsEntity client, String registrationAccessToken, String registrationClientUri) {
        this.client = client;
        this.registrationAccessToken = registrationAccessToken;
        this.registrationClientUri = registrationClientUri;
    }

    /**
     * @return the client
     */
    public ClientDetailsEntity getClient() {
        return client;
    }

    /**
     * The client.
     * 
     * @param client
     *            The client entity.
     */
    public void setClient(ClientDetailsEntity client) {
        this.client = client;
    }

    /**
     * @return The client description.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getClientDescription()
     */
    public String getClientDescription() {
        return client.getClientDescription();
    }

    /**
     * @param clientDescription
     *            The client description.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setClientDescription(java.lang.String)
     */
    public void setClientDescription(String clientDescription) {
        client.setClientDescription(clientDescription);
    }

    /**
     * @return The refresh token is allowed.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#isAllowRefresh()
     */
    public boolean isAllowRefresh() {
        return client.isAllowRefresh();
    }

    /**
     * @return The refresh token can be used.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#isReuseRefreshToken()
     */
    public boolean isReuseRefreshToken() {
        return client.isReuseRefreshToken();
    }

    /**
     * @param reuseRefreshToken
     *            The refresh token can be used.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setReuseRefreshToken(boolean)
     */
    public void setReuseRefreshToken(boolean reuseRefreshToken) {
        client.setReuseRefreshToken(reuseRefreshToken);
    }

    /**
     * @return The token in seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getIdTokenValiditySeconds()
     */
    public Integer getIdTokenValiditySeconds() {
        return client.getIdTokenValiditySeconds();
    }

    /**
     * @param idTokenValiditySeconds
     *            The token in seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setIdTokenValiditySeconds(java.lang.Integer)
     */
    public void setIdTokenValiditySeconds(Integer idTokenValiditySeconds) {
        client.setIdTokenValiditySeconds(idTokenValiditySeconds);
    }

    /**
     * @return Is dynamically registered.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#isDynamicallyRegistered()
     */
    public boolean isDynamicallyRegistered() {
        return client.isDynamicallyRegistered();
    }

    /**
     * @param dynamicallyRegistered
     *            The client is dynamically registered.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setDynamicallyRegistered(boolean)
     */
    public void setDynamicallyRegistered(boolean dynamicallyRegistered) {
        client.setDynamicallyRegistered(dynamicallyRegistered);
    }

    /**
     * @return Allows introspection.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#isAllowIntrospection()
     */
    public boolean isAllowIntrospection() {
        return client.isAllowIntrospection();
    }

    /**
     * @param allowIntrospection
     *            Allow introspection.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setAllowIntrospection(boolean)
     */
    public void setAllowIntrospection(boolean allowIntrospection) {
        client.setAllowIntrospection(allowIntrospection);
    }

    /**
     * @return Is secret required.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#isSecretRequired()
     */
    public boolean isSecretRequired() {
        return client.isSecretRequired();
    }

    /**
     * @return Is scoped.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#isScoped()
     */
    public boolean isScoped() {
        return client.isScoped();
    }

    /**
     * @return The client id.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getClientId()
     */
    public String getClientId() {
        return client.getClientId();
    }

    /**
     * @param clientId
     *            Sets the client id.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setClientId(java.lang.String)
     */
    public void setClientId(String clientId) {
        client.setClientId(clientId);
    }

    /**
     * @return The client secret.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getClientSecret()
     */
    public String getClientSecret() {
        return client.getClientSecret();
    }

    /**
     * @param clientSecret
     *            The client secret.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setClientSecret(java.lang.String)
     */
    public void setClientSecret(String clientSecret) {
        client.setClientSecret(clientSecret);
    }

    /**
     * @return The scope.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getScope()
     */
    public Set<String> getScope() {
        return client.getScope();
    }

    /**
     * @param scope
     *            The set of scopes.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setScope(java.util.Set)
     */
    public void setScope(Set<String> scope) {
        client.setScope(scope);
    }

    /**
     * @return The set of grand type.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getGrantTypes()
     */
    public Set<String> getGrantTypes() {
        return client.getGrantTypes();
    }

    /**
     * @param grantTypes
     *            The grand types.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setGrantTypes(java.util.Set)
     */
    public void setGrantTypes(Set<String> grantTypes) {
        client.setGrantTypes(grantTypes);
    }

    /**
     * @return The grand types.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getAuthorizedGrantTypes()
     */
    public Set<String> getAuthorizedGrantTypes() {
        return client.getAuthorizedGrantTypes();
    }

    /**
     * @return The set of authorities.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getAuthorities()
     */
    public Set<GrantedAuthority> getAuthorities() {
        return client.getAuthorities();
    }

    /**
     * @param authorities
     *            The set of authorities.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setAuthorities(java.util.Set)
     */
    public void setAuthorities(Set<GrantedAuthority> authorities) {
        client.setAuthorities(authorities);
    }

    /**
     * @return The token in seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getAccessTokenValiditySeconds()
     */
    public Integer getAccessTokenValiditySeconds() {
        return client.getAccessTokenValiditySeconds();
    }

    /**
     * @param accessTokenValiditySeconds
     *            The token in seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setAccessTokenValiditySeconds(java.lang.Integer)
     */
    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        client.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
    }

    /**
     * @return The token in secords.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getRefreshTokenValiditySeconds()
     */
    public Integer getRefreshTokenValiditySeconds() {
        return client.getRefreshTokenValiditySeconds();
    }

    /**
     * @param refreshTokenValiditySeconds
     *            The token in seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setRefreshTokenValiditySeconds(java.lang.Integer)
     */
    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        client.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
    }

    /**
     * @return The redirected uris.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getRedirectUris()
     */
    public Set<String> getRedirectUris() {
        return client.getRedirectUris();
    }

    /**
     * @param redirectUris
     *            The redirect uris.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setRedirectUris(java.util.Set)
     */
    public void setRedirectUris(Set<String> redirectUris) {
        client.setRedirectUris(redirectUris);
    }

    /**
     * @return The registered redirected uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getRegisteredRedirectUri()
     */
    public Set<String> getRegisteredRedirectUri() {
        return client.getRegisteredRedirectUri();
    }

    /**
     * @return The resource ids.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getResourceIds()
     */
    public Set<String> getResourceIds() {
        return client.getResourceIds();
    }

    /**
     * @param resourceIds
     *            The resource ids.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setResourceIds(java.util.Set)
     */
    public void setResourceIds(Set<String> resourceIds) {
        client.setResourceIds(resourceIds);
    }

    /**
     * @return The additional information.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getAdditionalInformation()
     */
    public Map<String, Object> getAdditionalInformation() {
        return client.getAdditionalInformation();
    }

    /**
     * @return The applicationType.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getApplicationType()
     */
    public AppType getApplicationType() {
        return client.getApplicationType();
    }

    /**
     * @param applicationType
     *            The application type.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setApplicationType(com.ztarmobile.oauth2.model.ClientDetailsEntity.AppType)
     */
    public void setApplicationType(AppType applicationType) {
        client.setApplicationType(applicationType);
    }

    /**
     * @return The client name.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getClientName()
     */
    public String getClientName() {
        return client.getClientName();
    }

    /**
     * @param clientName
     *            The client name.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setClientName(java.lang.String)
     */
    public void setClientName(String clientName) {
        client.setClientName(clientName);
    }

    /**
     * @return The AuthMethod.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getTokenEndpointAuthMethod()
     */
    public AuthMethod getTokenEndpointAuthMethod() {
        return client.getTokenEndpointAuthMethod();
    }

    /**
     * @param tokenEndpointAuthMethod
     *            The token auth method.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setTokenEndpointAuthMethod(com.ztarmobile.oauth2.model.ClientDetailsEntity.AuthMethod)
     */
    public void setTokenEndpointAuthMethod(AuthMethod tokenEndpointAuthMethod) {
        client.setTokenEndpointAuthMethod(tokenEndpointAuthMethod);
    }

    /**
     * @return The subject type.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getSubjectType()
     */
    public SubjectType getSubjectType() {
        return client.getSubjectType();
    }

    /**
     * @param subjectType
     *            The subject type.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setSubjectType(com.ztarmobile.oauth2.model.ClientDetailsEntity.SubjectType)
     */
    public void setSubjectType(SubjectType subjectType) {
        client.setSubjectType(subjectType);
    }

    /**
     * @return The list of contacts.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getContacts()
     */
    public Set<String> getContacts() {
        return client.getContacts();
    }

    /**
     * @param contacts
     *            The list of contacts.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setContacts(java.util.Set)
     */
    public void setContacts(Set<String> contacts) {
        client.setContacts(contacts);
    }

    /**
     * @return The logoIri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getLogoUri()
     */
    public String getLogoUri() {
        return client.getLogoUri();
    }

    /**
     * @param logoUri
     *            The logoUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setLogoUri(java.lang.String)
     */
    public void setLogoUri(String logoUri) {
        client.setLogoUri(logoUri);
    }

    /**
     * @return The policyUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getPolicyUri()
     */
    public String getPolicyUri() {
        return client.getPolicyUri();
    }

    /**
     * @param policyUri
     *            The policyUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setPolicyUri(java.lang.String)
     */
    public void setPolicyUri(String policyUri) {
        client.setPolicyUri(policyUri);
    }

    /**
     * @return The client uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getClientUri()
     */
    public String getClientUri() {
        return client.getClientUri();
    }

    /**
     * @param clientUri
     *            The client uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setClientUri(java.lang.String)
     */
    public void setClientUri(String clientUri) {
        client.setClientUri(clientUri);
    }

    /**
     * @return The tosIri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getTosUri()
     */
    public String getTosUri() {
        return client.getTosUri();
    }

    /**
     * @param tosUri
     *            The tosUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setTosUri(java.lang.String)
     */
    public void setTosUri(String tosUri) {
        client.setTosUri(tosUri);
    }

    /**
     * @return The jwksUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getJwksUri()
     */
    public String getJwksUri() {
        return client.getJwksUri();
    }

    /**
     * @param jwksUri
     *            The jwksUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setJwksUri(java.lang.String)
     */
    public void setJwksUri(String jwksUri) {
        client.setJwksUri(jwksUri);
    }

    /**
     * @return The JWKSet.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getJwks()
     */
    public JWKSet getJwks() {
        return client.getJwks();
    }

    /**
     * @param jwks
     *            The JWKSet.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setJwks(com.nimbusds.jose.jwk.JWKSet)
     */
    public void setJwks(JWKSet jwks) {
        client.setJwks(jwks);
    }

    /**
     * @return The sector identifier uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getSectorIdentifierUri()
     */
    public String getSectorIdentifierUri() {
        return client.getSectorIdentifierUri();
    }

    /**
     * @param sectorIdentifierUri
     *            The sector identifier uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setSectorIdentifierUri(java.lang.String)
     */
    public void setSectorIdentifierUri(String sectorIdentifierUri) {
        client.setSectorIdentifierUri(sectorIdentifierUri);
    }

    /**
     * @return The default max age.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getDefaultMaxAge()
     */
    public Integer getDefaultMaxAge() {
        return client.getDefaultMaxAge();
    }

    /**
     * @param defaultMaxAge
     *            The default max age.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setDefaultMaxAge(java.lang.Integer)
     */
    public void setDefaultMaxAge(Integer defaultMaxAge) {
        client.setDefaultMaxAge(defaultMaxAge);
    }

    /**
     * @return The flag.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getRequireAuthTime()
     */
    public Boolean getRequireAuthTime() {
        return client.getRequireAuthTime();
    }

    /**
     * @param requireAuthTime
     *            The flag.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setRequireAuthTime(java.lang.Boolean)
     */
    public void setRequireAuthTime(Boolean requireAuthTime) {
        client.setRequireAuthTime(requireAuthTime);
    }

    /**
     * @return The set of response types.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getResponseTypes()
     */
    public Set<String> getResponseTypes() {
        return client.getResponseTypes();
    }

    /**
     * @param responseTypes
     *            The response types.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setResponseTypes(java.util.Set)
     */
    public void setResponseTypes(Set<String> responseTypes) {
        client.setResponseTypes(responseTypes);
    }

    /**
     * @return The default CR values.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getDefaultACRvalues()
     */
    public Set<String> getDefaultACRvalues() {
        return client.getDefaultACRvalues();
    }

    /**
     * @param defaultACRvalues
     *            The default CR values.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setDefaultACRvalues(java.util.Set)
     */
    public void setDefaultACRvalues(Set<String> defaultACRvalues) {
        client.setDefaultACRvalues(defaultACRvalues);
    }

    /**
     * @return The initiate login uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getInitiateLoginUri()
     */
    public String getInitiateLoginUri() {
        return client.getInitiateLoginUri();
    }

    /**
     * @param initiateLoginUri
     *            The initiate login uri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setInitiateLoginUri(java.lang.String)
     */
    public void setInitiateLoginUri(String initiateLoginUri) {
        client.setInitiateLoginUri(initiateLoginUri);
    }

    /**
     * @return The postLogoutRedirectUris.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getPostLogoutRedirectUris()
     */
    public Set<String> getPostLogoutRedirectUris() {
        return client.getPostLogoutRedirectUris();
    }

    /**
     * @param postLogoutRedirectUri
     *            The postLogoutRedirectUri.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setPostLogoutRedirectUris(Set)
     */
    public void setPostLogoutRedirectUris(Set<String> postLogoutRedirectUri) {
        client.setPostLogoutRedirectUris(postLogoutRedirectUri);
    }

    /**
     * @return The requested uris.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getRequestUris()
     */
    public Set<String> getRequestUris() {
        return client.getRequestUris();
    }

    /**
     * @param requestUris
     *            The requested uris.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setRequestUris(java.util.Set)
     */
    public void setRequestUris(Set<String> requestUris) {
        client.setRequestUris(requestUris);
    }

    /**
     * @return The JWSAlgorithm.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getRequestObjectSigningAlg()
     */
    public JWSAlgorithm getRequestObjectSigningAlg() {
        return client.getRequestObjectSigningAlg();
    }

    /**
     * @param requestObjectSigningAlg
     *            The signing algorithm.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setRequestObjectSigningAlg(com.nimbusds.jose.JWSAlgorithm)
     */
    public void setRequestObjectSigningAlg(JWSAlgorithm requestObjectSigningAlg) {
        client.setRequestObjectSigningAlg(requestObjectSigningAlg);
    }

    /**
     * @return The signed response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getUserInfoSignedResponseAlg()
     */
    public JWSAlgorithm getUserInfoSignedResponseAlg() {
        return client.getUserInfoSignedResponseAlg();
    }

    /**
     * @param userInfoSignedResponseAlg
     *            The signed response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setUserInfoSignedResponseAlg(com.nimbusds.jose.JWSAlgorithm)
     */
    public void setUserInfoSignedResponseAlg(JWSAlgorithm userInfoSignedResponseAlg) {
        client.setUserInfoSignedResponseAlg(userInfoSignedResponseAlg);
    }

    /**
     * @return The encrypted response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getUserInfoEncryptedResponseAlg()
     */
    public JWEAlgorithm getUserInfoEncryptedResponseAlg() {
        return client.getUserInfoEncryptedResponseAlg();
    }

    /**
     * @param userInfoEncryptedResponseAlg
     *            The encrypted response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setUserInfoEncryptedResponseAlg(com.nimbusds.jose.JWEAlgorithm)
     */
    public void setUserInfoEncryptedResponseAlg(JWEAlgorithm userInfoEncryptedResponseAlg) {
        client.setUserInfoEncryptedResponseAlg(userInfoEncryptedResponseAlg);
    }

    /**
     * @return The user info.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getUserInfoEncryptedResponseEnc()
     */
    public EncryptionMethod getUserInfoEncryptedResponseEnc() {
        return client.getUserInfoEncryptedResponseEnc();
    }

    /**
     * @param userInfoEncryptedResponseEnc
     *            The user info.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setUserInfoEncryptedResponseEnc(com.nimbusds.jose.EncryptionMethod)
     */
    public void setUserInfoEncryptedResponseEnc(EncryptionMethod userInfoEncryptedResponseEnc) {
        client.setUserInfoEncryptedResponseEnc(userInfoEncryptedResponseEnc);
    }

    /**
     * @return The signed response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getIdTokenSignedResponseAlg()
     */
    public JWSAlgorithm getIdTokenSignedResponseAlg() {
        return client.getIdTokenSignedResponseAlg();
    }

    /**
     * @param idTokenSignedResponseAlg
     *            The signed response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setIdTokenSignedResponseAlg(com.nimbusds.jose.JWSAlgorithm)
     */
    public void setIdTokenSignedResponseAlg(JWSAlgorithm idTokenSignedResponseAlg) {
        client.setIdTokenSignedResponseAlg(idTokenSignedResponseAlg);
    }

    /**
     * @return The encrypted algorithm.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getIdTokenEncryptedResponseAlg()
     */
    public JWEAlgorithm getIdTokenEncryptedResponseAlg() {
        return client.getIdTokenEncryptedResponseAlg();
    }

    /**
     * @param idTokenEncryptedResponseAlg
     *            The encrypted algorithm.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setIdTokenEncryptedResponseAlg(com.nimbusds.jose.JWEAlgorithm)
     */
    public void setIdTokenEncryptedResponseAlg(JWEAlgorithm idTokenEncryptedResponseAlg) {
        client.setIdTokenEncryptedResponseAlg(idTokenEncryptedResponseAlg);
    }

    /**
     * @return The encrypted response. @see
     *         com.ztarmobile.oauth2.model.ClientDetailsEntity#getIdTokenEncryptedResponseEnc()
     */
    public EncryptionMethod getIdTokenEncryptedResponseEnc() {
        return client.getIdTokenEncryptedResponseEnc();
    }

    /**
     * @param idTokenEncryptedResponseEnc
     *            The encrypted response.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setIdTokenEncryptedResponseEnc(com.nimbusds.jose.EncryptionMethod)
     */
    public void setIdTokenEncryptedResponseEnc(EncryptionMethod idTokenEncryptedResponseEnc) {
        client.setIdTokenEncryptedResponseEnc(idTokenEncryptedResponseEnc);
    }

    /**
     * The token endPoint auth.
     * 
     * @return The JWSAlgorithm
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getTokenEndpointAuthSigningAlg()
     */
    public JWSAlgorithm getTokenEndpointAuthSigningAlg() {
        return client.getTokenEndpointAuthSigningAlg();
    }

    /**
     * @param tokenEndpointAuthSigningAlg
     *            The actual token.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setTokenEndpointAuthSigningAlg(com.nimbusds.jose.JWSAlgorithm)
     */
    public void setTokenEndpointAuthSigningAlg(JWSAlgorithm tokenEndpointAuthSigningAlg) {
        client.setTokenEndpointAuthSigningAlg(tokenEndpointAuthSigningAlg);
    }

    /**
     * @return The date when the client was created.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getCreatedAt()
     */
    public Date getCreatedAt() {
        return client.getCreatedAt();
    }

    /**
     * @param createdAt
     *            The date when the client was created.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setCreatedAt(java.util.Date)
     */
    public void setCreatedAt(Date createdAt) {
        client.setCreatedAt(createdAt);
    }

    /**
     * @return the registrationAccessToken
     */
    public String getRegistrationAccessToken() {
        return registrationAccessToken;
    }

    /**
     * @param registrationAccessToken
     *            the registrationAccessToken to set
     */
    public void setRegistrationAccessToken(String registrationAccessToken) {
        this.registrationAccessToken = registrationAccessToken;
    }

    /**
     * @return the registrationClientUri
     */
    public String getRegistrationClientUri() {
        return registrationClientUri;
    }

    /**
     * @param registrationClientUri
     *            the registrationClientUri to set
     */
    public void setRegistrationClientUri(String registrationClientUri) {
        this.registrationClientUri = registrationClientUri;
    }

    /**
     * @return the clientSecretExpiresAt
     */
    public Date getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    /**
     * @param expiresAt
     *            the clientSecretExpiresAt to set
     */
    public void setClientSecretExpiresAt(Date expiresAt) {
        this.clientSecretExpiresAt = expiresAt;
    }

    /**
     * @return the clientIdIssuedAt
     */
    public Date getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    /**
     * @param issuedAt
     *            the clientIdIssuedAt to set
     */
    public void setClientIdIssuedAt(Date issuedAt) {
        this.clientIdIssuedAt = issuedAt;
    }

    /**
     * @return List of claims.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getClaimsRedirectUris()
     */
    public Set<String> getClaimsRedirectUris() {
        return client.getClaimsRedirectUris();
    }

    /**
     * @param claimsRedirectUris
     *            The list of claims.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setClaimsRedirectUris(java.util.Set)
     */
    public void setClaimsRedirectUris(Set<String> claimsRedirectUris) {
        client.setClaimsRedirectUris(claimsRedirectUris);
    }

    /**
     * @return The JWT.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getSoftwareStatement()
     */
    public JWT getSoftwareStatement() {
        return client.getSoftwareStatement();
    }

    /**
     * @param softwareStatement
     *            The software statement.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setSoftwareStatement(com.nimbusds.jwt.JWT)
     */
    public void setSoftwareStatement(JWT softwareStatement) {
        client.setSoftwareStatement(softwareStatement);
    }

    /**
     * @return The PKCEAlgorithm.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getCodeChallengeMethod()
     */
    public PKCEAlgorithm getCodeChallengeMethod() {
        return client.getCodeChallengeMethod();
    }

    /**
     * @param codeChallengeMethod
     *            The code challenge method.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setCodeChallengeMethod(com.ztarmobile.oauth2.model.PKCEAlgorithm)
     */
    public void setCodeChallengeMethod(PKCEAlgorithm codeChallengeMethod) {
        client.setCodeChallengeMethod(codeChallengeMethod);
    }

    /**
     * @return the src
     */
    public JsonObject getSource() {
        return src;
    }

    /**
     * @param src
     *            the src to set
     */
    public void setSource(JsonObject src) {
        this.src = src;
    }

    /**
     * @return The number of seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getDeviceCodeValiditySeconds()
     */
    public Integer getDeviceCodeValiditySeconds() {
        return client.getDeviceCodeValiditySeconds();
    }

    /**
     * @param deviceCodeValiditySeconds
     *            The number of seconds.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setDeviceCodeValiditySeconds(java.lang.Integer)
     */
    public void setDeviceCodeValiditySeconds(Integer deviceCodeValiditySeconds) {
        client.setDeviceCodeValiditySeconds(deviceCodeValiditySeconds);
    }

    /**
     * @return The software id.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getSoftwareId()
     */
    public String getSoftwareId() {
        return client.getSoftwareId();
    }

    /**
     * @param softwareId
     *            The software id.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setSoftwareId(java.lang.String)
     */
    public void setSoftwareId(String softwareId) {
        client.setSoftwareId(softwareId);
    }

    /**
     * @return The software version.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#getSoftwareVersion()
     */
    public String getSoftwareVersion() {
        return client.getSoftwareVersion();
    }

    /**
     * @param softwareVersion
     *            The software version.
     * @see com.ztarmobile.oauth2.model.ClientDetailsEntity#setSoftwareVersion(java.lang.String)
     */
    public void setSoftwareVersion(String softwareVersion) {
        client.setSoftwareVersion(softwareVersion);
    }

}
