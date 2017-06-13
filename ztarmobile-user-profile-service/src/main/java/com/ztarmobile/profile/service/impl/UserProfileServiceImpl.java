/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.profile.service.impl;

import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_NOT_FOUND;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.MDN_NOT_FOUND;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_NOT_FOUND;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_NOT_FOUND;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_NOT_FOUND_BY_ADDRESS;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_NOT_FOUND_BY_PAYMENT_PROFILE;

import com.ztarmobile.profile.dao.AddressDao;
import com.ztarmobile.profile.dao.MdnDao;
import com.ztarmobile.profile.dao.PaymentProfileDao;
import com.ztarmobile.profile.dao.UserProfileDao;
import com.ztarmobile.profile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.profile.exception.ProfileServiceException;
import com.ztarmobile.profile.model.Address;
import com.ztarmobile.profile.model.Mdn;
import com.ztarmobile.profile.model.PaymentProfile;
import com.ztarmobile.profile.model.UserProfile;
import com.ztarmobile.profile.service.UserProfileService;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Direct implementation for the user management.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    /**
     * The user profile repository.
     */
    @Autowired
    private UserProfileDao userProfileRepository;

    /**
     * The MDN entity repository.
     */
    @Autowired
    private MdnDao mdnRepository;

    /**
     * The Address entity repository.
     */
    @Autowired
    private AddressDao addressRepository;

    /**
     * The PaymentProfile entity repository.
     */
    @Autowired
    private PaymentProfileDao paymentProfileRepository;

    /**
     * The validator service.
     */
    @Autowired
    private UserProfileValidatorService validator;

    /**
     * {@inheritDoc}
     */
    @Override
    public long createNewUserProfile(UserProfile userProfile) {
        log.debug("Creating a new user...");

        // validates the user profile
        validator.validateUserProfile(userProfile);

        // saves the user profile...
        UserProfile userSaved = userProfileRepository.save(userProfile);
        log.debug("User Created with id: " + userSaved.getId());
        return userSaved.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserProfile readUserProfile(long userProfileId) {
        log.debug("Reading a paymentProfile...");

        // reads the user profile...
        UserProfile userProfile = userProfileRepository.findOne(userProfileId);
        if (userProfile == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(USER_PROFILE_NOT_FOUND, userProfileId));
        }
        // gets the details for this user profile.
        // gets the MDN associated
        List<Mdn> mdns = mdnRepository.findByUserProfile(userProfile);
        userProfile.setMdns(mdns);

        // gets the addresses associated
        List<Address> addresses = addressRepository.findByUserProfile(userProfile);
        userProfile.setAddresses(addresses);

        // gets the payment profiles associated
        List<PaymentProfile> paymentProfiles = paymentProfileRepository.findByUserProfile(userProfile);
        userProfile.setPaymentProfiles(paymentProfiles);

        log.debug("User Profile read: " + userProfile);
        return userProfile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long createNewMdn(long userProfileId, Mdn mdn) {
        log.debug("Creating a new mdn...");

        // validates the MDN
        validator.validateMdn(mdn);

        // saves the MDN...
        UserProfile userSaved = userProfileRepository.findOne(userProfileId);
        if (userSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(USER_PROFILE_NOT_FOUND, userProfileId));
        }
        mdn.setUserProfile(userSaved);
        Mdn mdnSaved = mdnRepository.save(mdn);
        log.debug("Mdn Created with id: " + mdnSaved);
        return mdnSaved.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mdn readMdn(long mdnId) {
        log.debug("Reading existing mdn...");
        Mdn mdn = mdnRepository.findOne(mdnId);
        if (mdn == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(MDN_NOT_FOUND, mdnId));
        }
        return mdn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mdn updateMdn(long mdnId, Mdn mdn) {
        log.debug("Updating existing mdn...");

        // validates the MDN
        validator.validateMdn(mdn);

        Mdn mdnSaved = mdnRepository.findOne(mdnId);
        if (mdnSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(MDN_NOT_FOUND, mdnId));
        }

        // replaces the existing MDN.
        mdn.setId(mdnSaved.getId());
        mdnRepository.update(mdn);
        log.debug("Mdn Updated");
        return mdn;
    }

    @Override
    public Mdn deleteMdn(long mdnId) {
        log.debug("Deleting existing mdn...");
        Mdn mdnSaved = mdnRepository.findOne(mdnId);
        if (mdnSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(MDN_NOT_FOUND, mdnId));
        }
        // deletes the existing MDN.
        mdnRepository.delete(mdnSaved);
        log.debug("MDN Deleted");
        return mdnSaved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long createNewAddressWithUserProfile(long userProfileId, Address address) {
        log.debug("Creating a new address with user profile...");

        // validates the address
        validator.validateAddress(address);

        // saves the address...
        UserProfile userSaved = userProfileRepository.findOne(userProfileId);
        if (userSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(USER_PROFILE_NOT_FOUND, userProfileId));
        }
        address.setUserProfile(userSaved);
        Address addressSaved = addressRepository.save(address);
        log.debug("Address Created with id: " + addressSaved);
        return addressSaved.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address readAddress(long addressId) {
        log.debug("Reading existing address...");
        Address address = addressRepository.findOne(addressId);
        if (address == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_NOT_FOUND, addressId));
        }
        return address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address updateAddress(long addressId, Address address) {
        log.debug("Updating existing address...");

        // validates the address
        validator.validateAddress(address);

        Address addressSaved = addressRepository.findOne(addressId);
        if (addressSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_NOT_FOUND, addressId));
        }

        // replaces the existing address.
        address.setId(addressSaved.getId());
        addressRepository.update(address);
        log.debug("Address Updated");
        return address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address deleteAddress(long addressId) {
        log.debug("Deleting existing address...");
        Address addressSaved = addressRepository.findOne(addressId);
        if (addressSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_NOT_FOUND, addressId));
        }
        // deletes the existing address.
        addressRepository.delete(addressSaved);
        log.debug("Address Deleted");
        return addressSaved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public long createNewAddressWithPaymentProfile(long paymentProfileId, Address address) {
        log.debug("Creating a new address with payment profile...");

        // validates the address
        validator.validateAddress(address);

        // finds the user profile associated to this payment profile.
        PaymentProfile paymentSaved = paymentProfileRepository.findOneWithUserProfile(paymentProfileId);
        if (paymentSaved == null) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_NOT_FOUND, paymentProfileId));
        }
        if (paymentSaved.getUserProfile() == null) {
            // for some reason the user profile was not found.
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(USER_PROFILE_NOT_FOUND_BY_PAYMENT_PROFILE, paymentProfileId));
        }

        // saves the address with the user profile associated.
        address.setUserProfile(paymentSaved.getUserProfile());
        Address addressSaved = addressRepository.save(address);
        log.debug("Address Created with id: " + addressSaved);

        // now we associate the new address with the payment profile.
        paymentSaved.setAddress(addressSaved);
        paymentProfileRepository.updateAll(paymentSaved);

        return addressSaved.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long createNewPaymentProfileWithUserProfile(long userProfileId, PaymentProfile paymentProfile) {
        log.debug("Creating a new paymentProfile with user profile...");

        // validates the paymentProfile
        validator.validatePaymentProfile(paymentProfile);

        // saves the payment profile...
        UserProfile userSaved = userProfileRepository.findOne(userProfileId);
        if (userSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(USER_PROFILE_NOT_FOUND, userProfileId));
        }
        paymentProfile.setUserProfile(userSaved);
        PaymentProfile paymentProfileSaved = paymentProfileRepository.save(paymentProfile);
        log.debug("Payment Profile Created with id: " + paymentProfileSaved);
        return paymentProfileSaved.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentProfile readPaymentProfile(long paymentProfileId) {
        log.debug("Reading existing paymentProfile...");
        PaymentProfile paymentProfile = paymentProfileRepository.findOne(paymentProfileId);
        if (paymentProfile == null) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_NOT_FOUND, paymentProfileId));
        }
        return paymentProfile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentProfile updatePaymentProfile(long paymentProfileId, PaymentProfile paymentProfile) {
        log.debug("Updating existing paymentProfile...");

        // validates the paymentProfile
        validator.validatePaymentProfile(paymentProfile);

        PaymentProfile paymentProfileSaved = paymentProfileRepository.findOne(paymentProfileId);
        if (paymentProfileSaved == null) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_NOT_FOUND, paymentProfileId));
        }

        // replaces the existing paymentProfile.
        paymentProfile.setId(paymentProfileSaved.getId());
        paymentProfileRepository.update(paymentProfile);
        log.debug("PaymentProfile Updated");
        return paymentProfile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentProfile deletePaymentProfile(long paymentProfileId) {
        log.debug("Deleting existing paymentProfile...");
        PaymentProfile paymentProfileSaved = paymentProfileRepository.findOne(paymentProfileId);
        if (paymentProfileSaved == null) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_NOT_FOUND, paymentProfileId));
        }
        // deletes the existing paymentProfile.
        paymentProfileRepository.delete(paymentProfileSaved);
        log.debug("PaymentProfile Deleted");
        return paymentProfileSaved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long createNewPaymentProfileWithAddress(long addressId, PaymentProfile paymentProfile) {
        log.debug("Creating a new paymentProfile with address...");

        // validates the paymentProfile
        validator.validatePaymentProfile(paymentProfile);

        // saves the payment profile...
        Address addressSaved = addressRepository.findOneWithUserProfile(addressId);
        if (addressSaved == null) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_NOT_FOUND, addressId));
        }
        // now gets the user profile associated with this address
        if (addressSaved.getUserProfile() == null) {
            // for some reason the user profile was not found.
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(USER_PROFILE_NOT_FOUND_BY_ADDRESS, addressId));
        }

        paymentProfile.setAddress(addressSaved);
        paymentProfile.setUserProfile(addressSaved.getUserProfile());
        PaymentProfile paymentProfileSaved = paymentProfileRepository.save(paymentProfile);
        log.debug("Payment Profile Created with id: " + paymentProfileSaved);
        return paymentProfileSaved.getId();
    }
}
