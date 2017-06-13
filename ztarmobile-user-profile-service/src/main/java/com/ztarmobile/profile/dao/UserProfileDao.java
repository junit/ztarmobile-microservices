package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.model.UserProfile;

public interface UserProfileDao {

    UserProfile findOne(long userProfileId);

    UserProfile findByEmail(UserProfile userProfile);

    UserProfile save(UserProfile userProfile);

}
