package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.model.Address;
import com.ztarmobile.profile.model.UserProfile;

import java.util.List;

public interface AddressDao {

    Address save(Address address);

    List<Address> findByUserProfile(UserProfile userProfile);

    Address findOne(long addressId);

    Address findOneWithUserProfile(long addressId);

    void update(Address address);

    void delete(Address addressSaved);

}
