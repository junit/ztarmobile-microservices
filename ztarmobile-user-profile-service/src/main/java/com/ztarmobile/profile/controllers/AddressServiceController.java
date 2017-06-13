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

import com.ztarmobile.profile.model.Address;
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
 * @since 3.0
 */
@RestController
public class AddressServiceController extends CommonController {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AddressServiceController.class);

    /**
     * All the mappings.
     */
    private static final String USER_PROFILES = "userProfiles";
    private static final String PAYMENT_PROFILES = "paymentProfiles";
    private static final String ADDRESSES = "addresses";

    private static final String ADDRESS_MAPPING = ADDRESSES + "/{address}";
    private static final String ADDRESS_USER_PROF_MAPPING = USER_PROFILES + "/{userProfile}/" + ADDRESSES;
    private static final String ADDRESS_PAYMENT_PROF_MAPPING = PAYMENT_PROFILES + "/{paymentProfile}/" + ADDRESSES;

    /**
     * The service associated with the user profile.
     */
    @Autowired
    private UserProfileService userProfileService;

    /**
     * Creates a new address for a given user profile.
     * 
     * @param userProfileId
     *            The user profile identifier associated to this address.
     * @param address
     *            The new address.
     * @return The new address created.
     */
    @RequestMapping(value = ADDRESS_USER_PROF_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<Address> createNewAddress(@PathVariable("userProfile") long userProfileId,
            @RequestBody Address address) {
        log.debug("createNewAddress");
        // delegates the work to the service layer.
        long addressId = userProfileService.createNewAddressWithUserProfile(userProfileId, address);

        // the service has been created, we add some info in the header.
        String resourceLocation = createBaseUrl(USER_PROFILES, userProfileId);
        resourceLocation += SLASH + ADDRESSES + SLASH + addressId;
        return new ResponseEntity<Address>(address, createResponseHeader(resourceLocation), CREATED);
    }

    /**
     * Reads an existing address.
     * 
     * @param addressId
     *            The address id for this entity.
     * @return The address object.
     */
    @RequestMapping(value = ADDRESS_MAPPING, method = GET)
    public HttpEntity<Address> readAddress(@PathVariable("address") long addressId) {
        log.debug("readAddress");
        // delegates the work to the service layer.
        Address address = userProfileService.readAddress(addressId);
        return new ResponseEntity<Address>(address, OK);
    }

    /**
     * Updates an address.
     * 
     * @param addressId
     *            The address to be update.
     * @param address
     *            The new updated address.
     * @return The updated address.
     */
    @RequestMapping(value = ADDRESS_MAPPING, consumes = APPLICATION_JSON_VALUE, method = PUT)
    public HttpEntity<Address> updateAddress(@PathVariable("address") long addressId, @RequestBody Address address) {
        log.debug("updateAddress");
        // delegates the work to the service layer.
        Address updatedAddress = userProfileService.updateAddress(addressId, address);

        // the service has been updated, we add some info in the header.
        String resourceLocation = createBaseUrl(ADDRESSES, addressId);
        return new ResponseEntity<Address>(updatedAddress, createResponseHeader(resourceLocation), OK);
    }

    /**
     * Deletes an existing address.
     * 
     * @param addressId
     *            The address identifier.
     * @return The address that was deleted.
     */
    @RequestMapping(value = ADDRESS_MAPPING, method = DELETE)
    public HttpEntity<Address> deleteAddress(@PathVariable("address") long addressId) {
        log.debug("deleteAddress");
        // delegates the work to the service layer.
        Address deletedAddress = userProfileService.deleteAddress(addressId);
        return new ResponseEntity<Address>(deletedAddress, OK);
    }

    /**
     * Creates a new address for a given payment profile.
     * 
     * @param paymentProfileId
     *            The payment profile identifier associated to this address.
     * @param address
     *            The new address.
     * @return The new address created.
     */
    @RequestMapping(value = ADDRESS_PAYMENT_PROF_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<Address> createNewAddressWithPaymentProfile(@PathVariable("paymentProfile") long paymentProfileId,
            @RequestBody Address address) {
        log.debug("createNewAddressWithPaymentProfile");
        // delegates the work to the service layer.
        long addressId = userProfileService.createNewAddressWithPaymentProfile(paymentProfileId, address);

        // the service has been created, we add some info in the header.
        String resourceLocation = createBaseUrl(PAYMENT_PROFILES, paymentProfileId);
        resourceLocation += SLASH + ADDRESSES + SLASH + addressId;

        return new ResponseEntity<Address>(address, createResponseHeader(resourceLocation), CREATED);
    }
}
