package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.model.PaymentProfile;
import com.ztarmobile.profile.model.UserProfile;

import java.util.List;

public interface PaymentProfileDao {

    PaymentProfile save(PaymentProfile paymentProfile);

    List<PaymentProfile> findByUserProfile(UserProfile userProfile);

    PaymentProfile findOne(long paymentProfileId);

    PaymentProfile findOneWithUserProfile(long paymentProfileId);

    void update(PaymentProfile paymentProfile);

    void updateAll(PaymentProfile paymentProfile);

    void delete(PaymentProfile paymentProfileSaved);

}
