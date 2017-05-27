/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.oauth2.model;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

/**
 * The ClientDetailsEntity.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class ClientDetailsEntity {

    private static final int DEFAULT_ID_TOKEN_VALIDITY_SECONDS = 600;

    private Long id;

    /**
     * Fields from the OAuth2 Dynamic Registration Specification.
     */
    private String clientId = null; // client_id
    private String clientSecret = null; // client_secret
    private Set<String> redirectUris = new HashSet<>(); // redirect_uris
    private String clientName; // client_name
    private String clientUri; // client_uri
    private String logoUri; // logo_uri
    private Set<String> contacts; // contacts
    private String tosUri; // tos_uri
    private AuthMethod tokenEndpointAuthMethod = AuthMethod.SECRET_BASIC; // token_endpoint_auth_method
    private Set<String> scope = new HashSet<>(); // scope
    private Set<String> grantTypes = new HashSet<>(); // grant_types
    private Set<String> responseTypes = new HashSet<>(); // response_types
    private String policyUri;
    private String jwksUri; // URI pointer to keys
    private JWKSet jwks; // public key stored by value
    private String softwareId;
    private String softwareVersion;

    /**
     * Fields from OIDC Client Registration Specification *.
     */
    private AppType applicationType; // application_type
    private String sectorIdentifierUri; // sector_identifier_uri
    private SubjectType subjectType; // subject_type

    private JWSAlgorithm requestObjectSigningAlg = null; // request_object_signing_alg

    private JWSAlgorithm userInfoSignedResponseAlg = null; // user_info_signed_response_alg
    private JWEAlgorithm userInfoEncryptedResponseAlg = null; // user_info_encrypted_response_alg
    private EncryptionMethod userInfoEncryptedResponseEnc = null; // user_info_encrypted_response_enc

    private JWSAlgorithm idTokenSignedResponseAlg = null; // id_token_signed_response_alg
    private JWEAlgorithm idTokenEncryptedResponseAlg = null; // id_token_encrypted_response_alg
    private EncryptionMethod idTokenEncryptedResponseEnc = null; // id_token_encrypted_response_enc

    private JWSAlgorithm tokenEndpointAuthSigningAlg = null; // token_endpoint_auth_signing_alg

    private Integer defaultMaxAge; // default_max_age
    private Boolean requireAuthTime; // require_auth_time
    private Set<String> defaultACRvalues; // default_acr_values

    private String initiateLoginUri; // initiate_login_uri
    private Set<String> postLogoutRedirectUris; // post_logout_redirect_uris

    private Set<String> requestUris; // request_uris

    /**
     * Fields to support the ClientDetails interface *.
     */
    private Set<GrantedAuthority> authorities = new HashSet<>();
    private Integer accessTokenValiditySeconds = 0; // in seconds
    private Integer refreshTokenValiditySeconds = 0; // in seconds
    private Set<String> resourceIds = new HashSet<>();
    private Map<String, Object> additionalInformation = new HashMap<>();

    /**
     * Our own fields *.
     */
    private String clientDescription = ""; // human-readable description
    private boolean reuseRefreshToken = true; // do we let someone reuse a
                                              // refresh token?
    private boolean dynamicallyRegistered = false; // was this client
                                                   // dynamically registered?
    private boolean allowIntrospection = false; // do we let this client call
                                                // the introspection endpoint?
    private Integer idTokenValiditySeconds; // timeout for id tokens
    private Date createdAt; // time the client was created
    private boolean clearAccessTokensOnRefresh = true; // do we clear access
                                                       // tokens on refresh?
    private Integer deviceCodeValiditySeconds; // timeout for device codes

    /**
     * fields for UMA.
     */
    private Set<String> claimsRedirectUris;

    /**
     * Software statement *.
     */
    private JWT softwareStatement;

    /**
     * PKCE *.
     */
    private PKCEAlgorithm codeChallengeMethod;

    public enum AuthMethod {
        SECRET_POST("client_secret_post"), SECRET_BASIC("client_secret_basic"), SECRET_JWT(
                "client_secret_jwt"), PRIVATE_KEY("private_key_jwt"), NONE("none");

        private final String value;

        // map to aid reverse lookup
        private static final Map<String, AuthMethod> lookup = new HashMap<>();

        static {
            for (AuthMethod a : AuthMethod.values()) {
                lookup.put(a.getValue(), a);
            }
        }

        AuthMethod(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AuthMethod getByValue(String value) {
            return lookup.get(value);
        }
    }

    public enum AppType {
        WEB("web"), NATIVE("native");

        private final String value;

        // map to aid reverse lookup
        private static final Map<String, AppType> lookup = new HashMap<>();

        static {
            for (AppType a : AppType.values()) {
                lookup.put(a.getValue(), a);
            }
        }

        AppType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AppType getByValue(String value) {
            return lookup.get(value);
        }
    }

    public enum SubjectType {
        PAIRWISE("pairwise"), PUBLIC("public");

        private final String value;

        // map to aid reverse lookup
        private static final Map<String, SubjectType> lookup = new HashMap<>();

        static {
            for (SubjectType u : SubjectType.values()) {
                lookup.put(u.getValue(), u);
            }
        }

        SubjectType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SubjectType getByValue(String value) {
            return lookup.get(value);
        }
    }

    /**
     * Create a blank ClientDetailsEntity.
     */
    public ClientDetailsEntity() {

    }

    private void prePersist() {
        // make sure that ID tokens always time out, default to 5 minutes
        if (getIdTokenValiditySeconds() == null) {
            setIdTokenValiditySeconds(DEFAULT_ID_TOKEN_VALIDITY_SECONDS);
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the clientDescription
     */
    public String getClientDescription() {
        return clientDescription;
    }

    /**
     * @param clientDescription
     *            Human-readable long description of the client (optional)
     */
    public void setClientDescription(String clientDescription) {
        this.clientDescription = clientDescription;
    }

    /**
     * @return the allowRefresh
     */
    public boolean isAllowRefresh() {
        if (grantTypes != null) {
            return getAuthorizedGrantTypes().contains("refresh_token");
        } else {
            return false; // if there are no grants, we can't be refreshing
                          // them, can we?
        }
    }

    public boolean isReuseRefreshToken() {
        return reuseRefreshToken;
    }

    public void setReuseRefreshToken(boolean reuseRefreshToken) {
        this.reuseRefreshToken = reuseRefreshToken;
    }

    /**
     * Number of seconds ID token is valid for. MUST be a positive integer, can
     * not be null.
     *
     * @return the idTokenValiditySeconds
     */

    public Integer getIdTokenValiditySeconds() {
        return idTokenValiditySeconds;
    }

    /**
     * @param idTokenValiditySeconds
     *            the idTokenValiditySeconds to set
     */
    public void setIdTokenValiditySeconds(Integer idTokenValiditySeconds) {
        this.idTokenValiditySeconds = idTokenValiditySeconds;
    }

    /**
     * @return the dynamicallyRegistered
     */

    public boolean isDynamicallyRegistered() {
        return dynamicallyRegistered;
    }

    /**
     * @param dynamicallyRegistered
     *            the dynamicallyRegistered to set
     */
    public void setDynamicallyRegistered(boolean dynamicallyRegistered) {
        this.dynamicallyRegistered = dynamicallyRegistered;
    }

    /**
     * @return the allowIntrospection
     */

    public boolean isAllowIntrospection() {
        return allowIntrospection;
    }

    /**
     * @param allowIntrospection
     *            the allowIntrospection to set
     */
    public void setAllowIntrospection(boolean allowIntrospection) {
        this.allowIntrospection = allowIntrospection;
    }

    /**
     *
     */

    public boolean isSecretRequired() {
        if (getTokenEndpointAuthMethod() != null && (getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_BASIC)
                || getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_POST)
                || getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_JWT))) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * If the scope list is not null or empty, then this client has been scoped.
     */

    public boolean isScoped() {
        return getScope() != null && !getScope().isEmpty();
    }

    /**
     * @return the clientId
     */

    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId
     *            The OAuth2 client_id, must be unique to this client
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientSecret
     */

    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @param clientSecret
     *            the OAuth2 client_secret (optional)
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * @return the scope
     */

    public Set<String> getScope() {
        return scope;
    }

    /**
     * @param scope
     *            the set of scopes allowed to be issued to this client
     */
    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    /**
     * @return the authorizedGrantTypes
     */

    public Set<String> getGrantTypes() {
        return grantTypes;
    }

    /**
     * @param authorizedGrantTypes
     *            the OAuth2 grant types that this client is allowed to use
     */
    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    /**
     * passthrough for SECOAUTH api.
     */

    public Set<String> getAuthorizedGrantTypes() {
        return getGrantTypes();
    }

    /**
     * @return the authorities
     */

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities
     *            the Spring Security authorities this client is given
     */
    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    /**
     * @param accessTokenTimeout
     *            the accessTokenTimeout to set
     */
    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    /**
     * @param refreshTokenTimeout
     *            Lifetime of refresh tokens, in seconds (optional - leave null
     *            for no timeout)
     */
    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    /**
     * @return the registeredRedirectUri
     */

    public Set<String> getRedirectUris() {
        return redirectUris;
    }

    /**
     * @param registeredRedirectUri
     *            the registeredRedirectUri to set
     */
    public void setRedirectUris(Set<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    /**
     * Pass-through method to fulfill the ClientDetails interface with a bad
     * name.
     */

    public Set<String> getRegisteredRedirectUri() {
        return getRedirectUris();
    }

    /**
     * @return the resourceIds
     */

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    /**
     * @param resourceIds
     *            the resourceIds to set
     */
    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    /**
     * This library does not make use of this field, so it is not stored using
     * our persistence layer.
     *
     * However, it's somehow required by SECOUATH.
     *
     * @return an empty map
     */

    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public AppType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(AppType applicationType) {
        this.applicationType = applicationType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public AuthMethod getTokenEndpointAuthMethod() {
        return tokenEndpointAuthMethod;
    }

    public void setTokenEndpointAuthMethod(AuthMethod tokenEndpointAuthMethod) {
        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public Set<String> getContacts() {
        return contacts;
    }

    public void setContacts(Set<String> contacts) {
        this.contacts = contacts;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getPolicyUri() {
        return policyUri;
    }

    public void setPolicyUri(String policyUri) {
        this.policyUri = policyUri;
    }

    /**
     * @return the clientUrl
     */

    public String getClientUri() {
        return clientUri;
    }

    /**
     * @param clientUrl
     *            the clientUrl to set
     */
    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    /**
     * @return the tosUrl
     */

    public String getTosUri() {
        return tosUri;
    }

    /**
     * @param tosUrl
     *            the tosUrl to set
     */
    public void setTosUri(String tosUri) {
        this.tosUri = tosUri;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    /**
     * @return the jwks
     */

    public JWKSet getJwks() {
        return jwks;
    }

    /**
     * @param jwks
     *            the jwks to set
     */
    public void setJwks(JWKSet jwks) {
        this.jwks = jwks;
    }

    public String getSectorIdentifierUri() {
        return sectorIdentifierUri;
    }

    public void setSectorIdentifierUri(String sectorIdentifierUri) {
        this.sectorIdentifierUri = sectorIdentifierUri;
    }

    public JWSAlgorithm getRequestObjectSigningAlg() {
        return requestObjectSigningAlg;
    }

    public void setRequestObjectSigningAlg(JWSAlgorithm requestObjectSigningAlg) {
        this.requestObjectSigningAlg = requestObjectSigningAlg;
    }

    public JWSAlgorithm getUserInfoSignedResponseAlg() {
        return userInfoSignedResponseAlg;
    }

    public void setUserInfoSignedResponseAlg(JWSAlgorithm userInfoSignedResponseAlg) {
        this.userInfoSignedResponseAlg = userInfoSignedResponseAlg;
    }

    public JWEAlgorithm getUserInfoEncryptedResponseAlg() {
        return userInfoEncryptedResponseAlg;
    }

    public void setUserInfoEncryptedResponseAlg(JWEAlgorithm userInfoEncryptedResponseAlg) {
        this.userInfoEncryptedResponseAlg = userInfoEncryptedResponseAlg;
    }

    public EncryptionMethod getUserInfoEncryptedResponseEnc() {
        return userInfoEncryptedResponseEnc;
    }

    public void setUserInfoEncryptedResponseEnc(EncryptionMethod userInfoEncryptedResponseEnc) {
        this.userInfoEncryptedResponseEnc = userInfoEncryptedResponseEnc;
    }

    public JWSAlgorithm getIdTokenSignedResponseAlg() {
        return idTokenSignedResponseAlg;
    }

    public void setIdTokenSignedResponseAlg(JWSAlgorithm idTokenSignedResponseAlg) {
        this.idTokenSignedResponseAlg = idTokenSignedResponseAlg;
    }

    public JWEAlgorithm getIdTokenEncryptedResponseAlg() {
        return idTokenEncryptedResponseAlg;
    }

    public void setIdTokenEncryptedResponseAlg(JWEAlgorithm idTokenEncryptedResponseAlg) {
        this.idTokenEncryptedResponseAlg = idTokenEncryptedResponseAlg;
    }

    public EncryptionMethod getIdTokenEncryptedResponseEnc() {
        return idTokenEncryptedResponseEnc;
    }

    public void setIdTokenEncryptedResponseEnc(EncryptionMethod idTokenEncryptedResponseEnc) {
        this.idTokenEncryptedResponseEnc = idTokenEncryptedResponseEnc;
    }

    public JWSAlgorithm getTokenEndpointAuthSigningAlg() {
        return tokenEndpointAuthSigningAlg;
    }

    public void setTokenEndpointAuthSigningAlg(JWSAlgorithm tokenEndpointAuthSigningAlg) {
        this.tokenEndpointAuthSigningAlg = tokenEndpointAuthSigningAlg;
    }

    public Integer getDefaultMaxAge() {
        return defaultMaxAge;
    }

    public void setDefaultMaxAge(Integer defaultMaxAge) {
        this.defaultMaxAge = defaultMaxAge;
    }

    public Boolean getRequireAuthTime() {
        return requireAuthTime;
    }

    public void setRequireAuthTime(Boolean requireAuthTime) {
        this.requireAuthTime = requireAuthTime;
    }

    /**
     * @return the responseTypes
     */

    public Set<String> getResponseTypes() {
        return responseTypes;
    }

    /**
     * @param responseTypes
     *            the responseTypes to set
     */
    public void setResponseTypes(Set<String> responseTypes) {
        this.responseTypes = responseTypes;
    }

    /**
     * @return the defaultACRvalues
     */

    public Set<String> getDefaultACRvalues() {
        return defaultACRvalues;
    }

    /**
     * @param defaultACRvalues
     *            the defaultACRvalues to set
     */
    public void setDefaultACRvalues(Set<String> defaultACRvalues) {
        this.defaultACRvalues = defaultACRvalues;
    }

    /**
     * @return the initiateLoginUri
     */

    public String getInitiateLoginUri() {
        return initiateLoginUri;
    }

    /**
     * @param initiateLoginUri
     *            the initiateLoginUri to set
     */
    public void setInitiateLoginUri(String initiateLoginUri) {
        this.initiateLoginUri = initiateLoginUri;
    }

    /**
     * @return the postLogoutRedirectUri
     */

    public Set<String> getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    /**
     * @param postLogoutRedirectUri
     *            the postLogoutRedirectUri to set
     */
    public void setPostLogoutRedirectUris(Set<String> postLogoutRedirectUri) {
        this.postLogoutRedirectUris = postLogoutRedirectUri;
    }

    /**
     * @return the requestUris
     */

    public Set<String> getRequestUris() {
        return requestUris;
    }

    /**
     * @param requestUris
     *            the requestUris to set
     */
    public void setRequestUris(Set<String> requestUris) {
        this.requestUris = requestUris;
    }

    /**
     * @return the createdAt
     */

    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     *            the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Our framework doesn't use this construct, we use WhitelistedSites and
     * ApprovedSites instead.
     */

    public boolean isAutoApprove(String scope) {
        return false;
    }

    /**
     * @return the clearAccessTokensOnRefresh
     */

    public boolean isClearAccessTokensOnRefresh() {
        return clearAccessTokensOnRefresh;
    }

    /**
     * @param clearAccessTokensOnRefresh
     *            the clearAccessTokensOnRefresh to set
     */
    public void setClearAccessTokensOnRefresh(boolean clearAccessTokensOnRefresh) {
        this.clearAccessTokensOnRefresh = clearAccessTokensOnRefresh;
    }

    /**
     * @return the claimsRedirectUris
     */

    public Set<String> getClaimsRedirectUris() {
        return claimsRedirectUris;
    }

    /**
     * @param claimsRedirectUris
     *            the claimsRedirectUris to set
     */
    public void setClaimsRedirectUris(Set<String> claimsRedirectUris) {
        this.claimsRedirectUris = claimsRedirectUris;
    }

    /**
     * @return the softwareStatement
     */

    public JWT getSoftwareStatement() {
        return softwareStatement;
    }

    /**
     * @param softwareStatement
     *            the softwareStatement to set
     */
    public void setSoftwareStatement(JWT softwareStatement) {
        this.softwareStatement = softwareStatement;
    }

    /**
     * @return the codeChallengeMethod
     */

    public PKCEAlgorithm getCodeChallengeMethod() {
        return codeChallengeMethod;
    }

    /**
     * @param codeChallengeMethod
     *            the codeChallengeMethod to set
     */
    public void setCodeChallengeMethod(PKCEAlgorithm codeChallengeMethod) {
        this.codeChallengeMethod = codeChallengeMethod;
    }

    /**
     * @return the deviceCodeValiditySeconds
     */

    public Integer getDeviceCodeValiditySeconds() {
        return deviceCodeValiditySeconds;
    }

    /**
     * @param deviceCodeValiditySeconds
     *            the deviceCodeValiditySeconds to set
     */
    public void setDeviceCodeValiditySeconds(Integer deviceCodeValiditySeconds) {
        this.deviceCodeValiditySeconds = deviceCodeValiditySeconds;
    }

    /**
     * @return the softwareId
     */

    public String getSoftwareId() {
        return softwareId;
    }

    /**
     * @param softwareId
     *            the softwareId to set
     */
    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }

    /**
     * @return the softwareVersion
     */

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    /**
     * @param softwareVersion
     *            the softwareVersion to set
     */
    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

}
