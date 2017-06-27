/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.profile.service;

import com.ztarmobile.profile.model.Address;
import com.ztarmobile.profile.model.Mdn;
import com.ztarmobile.profile.model.PaymentProfile;
import com.ztarmobile.profile.model.UserProfile;
import com.ztarmobile.profile.service.impl.FilteringMode;

/**
 * Service that handles the user profile management.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public interface UserProfileService {

    long createNewUserProfile(UserProfile userProfile);

    UserProfile readUserProfile(long userProfileId);

    UserProfile readUserProfileWithFiltering(long userProfileId, FilteringMode... filteringModes);

    long createNewMdn(long userProfileId, Mdn mdn);

    Mdn readMdn(long mdnId);

    Mdn updateMdn(long mdnId, Mdn mdn);

    Mdn deleteMdn(long mdnId);

    long createNewAddressWithUserProfile(long userProfileId, Address address);

    Address readAddress(long addressId);

    Address updateAddress(long addressId, Address address);

    Address deleteAddress(long addressId);

    long createNewAddressWithPaymentProfile(long paymentProfileId, Address address);

    long createNewPaymentProfileWithUserProfile(long userProfileId, PaymentProfile paymentProfile);

    PaymentProfile readPaymentProfile(long paymentProfileId);

    PaymentProfile updatePaymentProfile(long paymentProfileId, PaymentProfile paymentProfile);

    PaymentProfile deletePaymentProfile(long paymentProfileId);

    long createNewPaymentProfileWithAddress(long addressId, PaymentProfile paymentProfile);

}
