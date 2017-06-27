/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.profile.service.impl;

import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_CITY_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_COUNTRY_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_LINE1_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_LINE2_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_LINE3_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_NAME_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_STATE_LEN;
import static com.ztarmobile.profile.common.CommonUtils.ADDRESS_ZIP_LEN;
import static com.ztarmobile.profile.common.CommonUtils.MDN_PHONE_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PAYMENT_PROFILE_ALIAS_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PAYMENT_PROFILE_EXP_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PAYMENT_PROFILE_KEY_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PROFILE_EMAIL_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PROFILE_FIRST_NAME_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PROFILE_LAST_NAME_LEN;
import static com.ztarmobile.profile.common.CommonUtils.PROFILE_PASS_MAX;
import static com.ztarmobile.profile.common.CommonUtils.PROFILE_PASS_MIN;
import static com.ztarmobile.profile.common.CommonUtils.validateEmail;
import static com.ztarmobile.profile.common.CommonUtils.validateExpDate;
import static com.ztarmobile.profile.common.CommonUtils.validatePassword;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_CITY_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_CITY_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_COUNTRY_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_LINE1_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_LINE1_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_LINE2_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_LINE3_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_NAME_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_NAME_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_STATE_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_STATE_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_ZIP_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.ADDRESS_ZIP_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.MDN_PHONE_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.MDN_PHONE_FORMAT;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.MDN_PHONE_M_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_ALIAS_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_ALIAS_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_EXP_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_EXP_FORMAT;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_EXP_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_KEY_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.PAYMENT_PROFILE_KEY_MAX_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_CONTACT_PHONE_FORMAT;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_CONTACT_PHONE_LEN;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_DUPLICATE_PROFILE;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_EMAIL_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_EMAIL_INVALID;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_EMAIL_LENGTH;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_FIRST_NAME_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_FIRST_NAME_LENGTH;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_LAST_NAME_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_LAST_NAME_LENGTH;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_PASSWORD_EMPTY;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_PASSWORD_INVALID;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.USER_PROFILE_PASSWORD_LENGTH;
import static org.springframework.util.StringUtils.hasText;

import com.ztarmobile.profile.dao.UserProfileDao;
import com.ztarmobile.profile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.profile.exception.ProfileServiceException;
import com.ztarmobile.profile.model.Address;
import com.ztarmobile.profile.model.Mdn;
import com.ztarmobile.profile.model.PaymentProfile;
import com.ztarmobile.profile.model.UserProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Validator for the entities.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Service
public class UserProfileValidatorService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserProfileValidatorService.class);

    /**
     * The user profile repository.
     */
    @Autowired
    private UserProfileDao userProfileRepository;

    /**
     * Validates the input parameters.
     * 
     * @param userProfile
     *            The user profile to be validated.
     */
    public void validateUserProfile(final UserProfile userProfile) {
        log.debug("Validating the userProfile");
        if (userProfile == null) {
            throw new ProfileServiceException("User profile object is null");
        }

        // validation for the first name.
        if (!hasText(userProfile.getFirstName())) {
            throw new ProfileServiceException(USER_PROFILE_FIRST_NAME_EMPTY);
        } else if (userProfile.getFirstName().length() > PROFILE_FIRST_NAME_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(USER_PROFILE_FIRST_NAME_LENGTH, PROFILE_FIRST_NAME_LEN));
        }

        // validation for the last name.
        if (!hasText(userProfile.getLastName())) {
            throw new ProfileServiceException(USER_PROFILE_LAST_NAME_EMPTY);
        } else if (userProfile.getLastName().length() > PROFILE_LAST_NAME_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(USER_PROFILE_LAST_NAME_LENGTH, PROFILE_LAST_NAME_LEN));
        }

        // validation for the user email.
        if (!hasText(userProfile.getEmail())) {
            throw new ProfileServiceException(USER_PROFILE_EMAIL_EMPTY);
        } else if (userProfile.getEmail().length() > PROFILE_EMAIL_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(USER_PROFILE_EMAIL_LENGTH, PROFILE_EMAIL_LEN));
        } else if (!validateEmail(userProfile.getEmail())) {
            throw new ProfileServiceException(USER_PROFILE_EMAIL_INVALID);
        }

        // we check whether the user email already exists in the DB.
        UserProfile userProfileFound = userProfileRepository.findByEmail(userProfile);
        if (userProfileFound != null) {
            // we found users with the same email/userName
            log.warn("A user might be duplicated, email: " + userProfile.getEmail());
            throw new ProfileServiceException(USER_PROFILE_DUPLICATE_PROFILE);
        }

        // validation for the password.
        if (!hasText(userProfile.getPassword())) {
            throw new ProfileServiceException(USER_PROFILE_PASSWORD_EMPTY);
        } else if (userProfile.getPassword().length() < PROFILE_PASS_MIN
                || userProfile.getPassword().length() > PROFILE_PASS_MAX) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(USER_PROFILE_PASSWORD_LENGTH,
                    String.valueOf(PROFILE_PASS_MIN), String.valueOf(PROFILE_PASS_MAX)));
        } else if (!validatePassword(userProfile.getPassword())) {
            throw new ProfileServiceException(USER_PROFILE_PASSWORD_INVALID);
        }

        // validation for the contact phone number
        if (userProfile.getContactPhoneNumber() != null) {
            if (userProfile.getContactPhoneNumber().length() != MDN_PHONE_LEN) {
                throw new ProfileServiceException(
                        new HttpMessageErrorCodeResolver(USER_PROFILE_CONTACT_PHONE_LEN, MDN_PHONE_LEN));
            }
            try {
                Long.parseLong(userProfile.getContactPhoneNumber());
            } catch (NumberFormatException e) {
                throw new ProfileServiceException(new HttpMessageErrorCodeResolver(USER_PROFILE_CONTACT_PHONE_FORMAT,
                        userProfile.getContactPhoneNumber(), String.valueOf(MDN_PHONE_LEN)));
            }
        }
    }

    /**
     * Validates the input parameters.
     * 
     * @param address
     *            The address to be validated.
     */
    public void validateAddress(final Address address) {
        log.debug("Validating the address");
        if (address == null) {
            throw new ProfileServiceException("Address object is null");
        }

        // validation for the name.
        if (!hasText(address.getName())) {
            throw new ProfileServiceException(ADDRESS_NAME_EMPTY);
        } else if (address.getName().length() > ADDRESS_NAME_LEN) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_NAME_MAX_LEN, ADDRESS_NAME_LEN));
        }

        // validation for the line1.
        if (!hasText(address.getLine1())) {
            throw new ProfileServiceException(ADDRESS_LINE1_EMPTY);
        } else if (address.getLine1().length() > ADDRESS_LINE1_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(ADDRESS_LINE1_MAX_LEN, ADDRESS_LINE1_LEN));
        }

        // validation for the line2.
        if (address.getLine2() != null && address.getLine2().length() > ADDRESS_LINE2_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(ADDRESS_LINE2_MAX_LEN, ADDRESS_LINE2_LEN));
        }

        // validation for the line3.
        if (address.getLine3() != null && address.getLine3().length() > ADDRESS_LINE3_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(ADDRESS_LINE3_MAX_LEN, ADDRESS_LINE3_LEN));
        }

        // validation for the city.
        if (!hasText(address.getCity())) {
            throw new ProfileServiceException(ADDRESS_CITY_EMPTY);
        } else if (address.getCity().length() > ADDRESS_CITY_LEN) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_CITY_MAX_LEN, ADDRESS_CITY_LEN));
        }

        // validation for the state.
        if (!hasText(address.getState())) {
            throw new ProfileServiceException(ADDRESS_STATE_EMPTY);
        } else if (address.getState().length() > ADDRESS_STATE_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(ADDRESS_STATE_MAX_LEN, ADDRESS_STATE_LEN));
        }

        // validation for the ZIP.
        if (!hasText(address.getZip())) {
            throw new ProfileServiceException(ADDRESS_ZIP_EMPTY);
        } else if (address.getZip().length() > ADDRESS_ZIP_LEN) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(ADDRESS_ZIP_MAX_LEN, ADDRESS_ZIP_LEN));
        }

        // validation for the country.
        if (address.getCountry() != null && address.getCountry().length() > ADDRESS_COUNTRY_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(ADDRESS_COUNTRY_MAX_LEN, ADDRESS_COUNTRY_LEN));
        }
    }

    /**
     * Validates the input parameters.
     * 
     * @param mdn
     *            The MDN to be validated.
     */
    public void validateMdn(final Mdn mdn) {
        log.debug("Validating the mdn");
        if (mdn == null) {
            throw new ProfileServiceException("MDN object is null");
        }

        // validation for the phone number.
        if (!hasText(mdn.getPhoneNumber())) {
            throw new ProfileServiceException(MDN_PHONE_EMPTY);
        } else if (mdn.getPhoneNumber().length() > MDN_PHONE_LEN) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(MDN_PHONE_M_LEN, MDN_PHONE_LEN));
        }
        try {
            Long.parseLong(mdn.getPhoneNumber());
        } catch (NumberFormatException e) {
            throw new ProfileServiceException(new HttpMessageErrorCodeResolver(MDN_PHONE_FORMAT, mdn.getPhoneNumber(),
                    String.valueOf(MDN_PHONE_LEN)));
        }

    }

    /**
     * Validates the input parameters.
     * 
     * @param paymentProfile
     *            The paymentProfile to be validated.
     */
    public void validatePaymentProfile(final PaymentProfile paymentProfile) {
        log.debug("Validating the paymentProfile");
        if (paymentProfile == null) {
            throw new ProfileServiceException("PaymentProfile object is null");
        }

        // validation for the alias.
        if (!hasText(paymentProfile.getAlias())) {
            throw new ProfileServiceException(PAYMENT_PROFILE_ALIAS_EMPTY);
        } else if (paymentProfile.getAlias().length() > PAYMENT_PROFILE_ALIAS_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_ALIAS_MAX_LEN, PAYMENT_PROFILE_ALIAS_LEN));
        }

        // validation for the expiration date.
        if (!hasText(paymentProfile.getExpirationDate())) {
            throw new ProfileServiceException(PAYMENT_PROFILE_EXP_EMPTY);
        } else if (paymentProfile.getExpirationDate().length() != PAYMENT_PROFILE_EXP_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_EXP_MAX_LEN, PAYMENT_PROFILE_EXP_LEN));
        } else if (!validateExpDate(paymentProfile.getExpirationDate())) {
            throw new ProfileServiceException(PAYMENT_PROFILE_EXP_FORMAT);
        }

        // validation for the profile key.
        if (!hasText(paymentProfile.getProfileKey())) {
            throw new ProfileServiceException(PAYMENT_PROFILE_KEY_EMPTY);
        } else if (paymentProfile.getProfileKey().length() > PAYMENT_PROFILE_KEY_LEN) {
            throw new ProfileServiceException(
                    new HttpMessageErrorCodeResolver(PAYMENT_PROFILE_KEY_MAX_LEN, PAYMENT_PROFILE_KEY_LEN));
        }
    }
}
