package com.ztarmobile.profile.dao;

import com.ztarmobile.profile.model.Mdn;
import com.ztarmobile.profile.model.UserProfile;

import java.util.List;

public interface MdnDao {

    Mdn save(Mdn mdn);

    List<Mdn> findByUserProfile(UserProfile userProfile);

    Mdn findOne(long mdnId);

    void update(Mdn mdn);

    void delete(Mdn mdnSaved);

}
