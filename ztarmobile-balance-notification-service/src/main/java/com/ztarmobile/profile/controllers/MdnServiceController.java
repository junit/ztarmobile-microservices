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

import com.ztarmobile.profile.model.Mdn;
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
public class MdnServiceController extends CommonController {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(MdnServiceController.class);

    /**
     * All the mappings.
     */
    private static final String USER_PROFILES = "userProfiles";
    private static final String MDNS = "mdns";

    private static final String MDNS_MAPPING = MDNS + "/{mdn}";
    private static final String MDN_USER_PROF_MAPPING = USER_PROFILES + "/{userProfile}/" + MDNS;

    /**
     * The service associated with the user profile.
     */
    @Autowired
    private UserProfileService userProfileService;

    /**
     * Creates a new MDN for a given user profile.
     * 
     * @param userProfileId
     *            The user profile identifier associated to this MDN.
     * @param mdn
     *            The new MDN.
     * @return The new MDN created.
     */
    @RequestMapping(value = MDN_USER_PROF_MAPPING, consumes = APPLICATION_JSON_VALUE, method = POST)
    public HttpEntity<Mdn> createNewMdn(@PathVariable("userProfile") long userProfileId, @RequestBody Mdn mdn) {
        log.debug("createNewMdn");
        // delegates the work to the service layer.
        long mdnId = userProfileService.createNewMdn(userProfileId, mdn);

        // the service has been created, we add some info in the header.
        String resourceLocation = createBaseUrl(USER_PROFILES, userProfileId);
        resourceLocation += SLASH + MDNS + SLASH + mdnId;
        return new ResponseEntity<Mdn>(mdn, createResponseHeader(resourceLocation), CREATED);
    }

    /**
     * Reads an existing MDN.
     * 
     * @param mdnId
     *            The MDN id for this entity.
     * @return The MDN object.
     */
    @RequestMapping(value = MDNS_MAPPING, method = GET)
    public HttpEntity<Mdn> readMdn(@PathVariable("mdn") long mdnId) {
        log.debug("readMdn");
        // delegates the work to the service layer.
        Mdn mdn = userProfileService.readMdn(mdnId);
        return new ResponseEntity<Mdn>(mdn, OK);
    }

    /**
     * Updates a MDN.
     * 
     * @param mdnId
     *            The MDN to be update.
     * @param mdn
     *            The new updated MDN.
     * @return The updated MDN.
     */
    @RequestMapping(value = MDNS_MAPPING, consumes = APPLICATION_JSON_VALUE, method = PUT)
    public HttpEntity<Mdn> updateMdn(@PathVariable("mdn") long mdnId, @RequestBody Mdn mdn) {
        log.debug("updateMdn");
        // delegates the work to the service layer.
        Mdn updatedMdn = userProfileService.updateMdn(mdnId, mdn);

        // the service has been updated, we add some info in the header.
        String resourceLocation = createBaseUrl(MDNS, mdnId);
        return new ResponseEntity<Mdn>(updatedMdn, createResponseHeader(resourceLocation), OK);
    }

    /**
     * Deletes an existing MDN.
     * 
     * @param mdnId
     *            The MDN identifier.
     * @return The MDN that was deleted.
     */
    @RequestMapping(value = MDNS_MAPPING, method = DELETE)
    public HttpEntity<Mdn> deleteMdn(@PathVariable("mdn") long mdnId) {
        log.debug("deleteMdn");
        // delegates the work to the service layer.
        Mdn deletedMdn = userProfileService.deleteMdn(mdnId);
        return new ResponseEntity<Mdn>(deletedMdn, OK);
    }
}
