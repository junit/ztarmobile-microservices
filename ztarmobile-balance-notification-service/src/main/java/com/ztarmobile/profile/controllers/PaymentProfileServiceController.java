/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.controllers;

import static com.ztarmobile.profile.common.CommonUtils.SLASH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import com.ztarmobile.profile.model.PaymentProfile;
import com.ztarmobile.profile.service.UserProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller class.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@RestController
public class PaymentProfileServiceController extends CommonController {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentProfileServiceController.class);

    /**
     * All the mappings.
     */
    private static final String USER_PROFILES = "userProfiles";
    private static final String PAYMENT_PROFILES = "paymentProfiles";
    private static final String ADDRESSES = "addresses";

    private static final String PAYMENT_PROFILE_MAPPING = PAYMENT_PROFILES + "/{paymentProfile}";
    private static final String PAYMENT_PROFILE_USER_PROF_MAPPING = USER_PROFILES + "/{userProfile}/"
            + PAYMENT_PROFILES;
    private static final String PAYMENT_PROFILE_ADDRESS_MAPPING = ADDRESSES + "/{address}/" + PAYMENT_PROFILES;

    /**
     * The service associated with the user profile.
     */
    @Autowired
    private UserProfileService userProfileService;

    /**
     * Creates a new payment profile for a given user profile.
     * 
     * @param userProfileId
     *            The user profile identifier associated to this payment
     *            profile.
     * @param paymentProfile
     *            The new payment profile.
     * @return The new payment profile created.
     */
    @RequestMapping(value = PAYMENT_PROFILE_USER_PROF_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<PaymentProfile> createNewPaymentProfile(@PathVariable("userProfile") long userProfileId,
            @RequestBody PaymentProfile paymentProfile) {
        log.debug("createNewPaymentProfile");
        // delegates the work to the service layer.
        long paymentProfileId = userProfileService.createNewPaymentProfileWithUserProfile(userProfileId,
                paymentProfile);

        // the service has been created, we add some info in the header.
        String resourceLocation = createBaseUrl(USER_PROFILES, userProfileId);
        resourceLocation += SLASH + PAYMENT_PROFILES + SLASH + paymentProfileId;
        return new ResponseEntity<PaymentProfile>(paymentProfile, createResponseHeader(resourceLocation), CREATED);
    }

    /**
     * Reads an existing paymentProfile.
     * 
     * @param paymentProfileId
     *            The paymentProfile id for this entity.
     * @return The paymentProfile object.
     */
    @RequestMapping(value = PAYMENT_PROFILE_MAPPING, method = GET)
    public HttpEntity<PaymentProfile> readPaymentProfile(@PathVariable("paymentProfile") long paymentProfileId) {
        log.debug("readPaymentProfile");
        // delegates the work to the service layer.
        PaymentProfile paymentProfile = userProfileService.readPaymentProfile(paymentProfileId);
        return new ResponseEntity<PaymentProfile>(paymentProfile, OK);
    }

    /**
     * Updates an paymentProfile.
     * 
     * @param paymentProfileId
     *            The paymentProfile to be update.
     * @param paymentProfile
     *            The new updated paymentProfile.
     * @return The updated paymentProfile.
     */
    @RequestMapping(value = PAYMENT_PROFILE_MAPPING, consumes = APPLICATION_JSON_VALUE, method = PUT)
    public HttpEntity<PaymentProfile> updatePaymentProfile(@PathVariable("paymentProfile") long paymentProfileId,
            @RequestBody PaymentProfile paymentProfile) {
        log.debug("updatePaymentProfile");
        // delegates the work to the service layer.
        PaymentProfile updatedPaymentProfile = userProfileService.updatePaymentProfile(paymentProfileId,
                paymentProfile);

        // the service has been updated, we add some info in the header.
        String resourceLocation = createBaseUrl(PAYMENT_PROFILES, paymentProfileId);
        return new ResponseEntity<PaymentProfile>(updatedPaymentProfile, createResponseHeader(resourceLocation), OK);
    }

    /**
     * Deletes an existing paymentProfile.
     * 
     * @param paymentProfileId
     *            The paymentProfile identifier.
     * @return The paymentProfile that was deleted.
     */
    @RequestMapping(value = PAYMENT_PROFILE_MAPPING, method = DELETE)
    public HttpEntity<PaymentProfile> deletePaymentProfile(@PathVariable("paymentProfile") long paymentProfileId) {
        log.debug("deletePaymentProfile");
        // delegates the work to the service layer.
        PaymentProfile deletedPaymentProfile = userProfileService.deletePaymentProfile(paymentProfileId);
        return new ResponseEntity<PaymentProfile>(deletedPaymentProfile, OK);
    }

    /**
     * Creates a new payment profile for a given address.
     * 
     * @param addressId
     *            The address identifier associated to this payment.
     * @param paymentProfile
     *            The new payment profile.
     * @return A new payment profile created.
     */
    @RequestMapping(value = PAYMENT_PROFILE_ADDRESS_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<PaymentProfile> createNewPaymentProfileWithAddress(@PathVariable("address") long addressId,
            @RequestBody PaymentProfile paymentProfile) {
        log.debug("createNewPaymentProfileWithAddress");
        // delegates the work to the service layer.
        long paymentProfileId = userProfileService.createNewPaymentProfileWithAddress(addressId, paymentProfile);

        // the service has been created, we add some info in the header.
        String resourceLocation = createBaseUrl(ADDRESSES, addressId);
        resourceLocation += SLASH + PAYMENT_PROFILES + SLASH + paymentProfileId;
        return new ResponseEntity<PaymentProfile>(paymentProfile, createResponseHeader(resourceLocation), CREATED);
    }
}
