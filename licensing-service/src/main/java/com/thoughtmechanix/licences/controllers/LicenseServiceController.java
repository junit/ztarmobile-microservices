package com.thoughtmechanix.licences.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtmechanix.licenses.model.License;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    @RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
    public License getLicenses(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        System.out.println("sddssdds=====");
        return new License().withId(licenseId).withProductName("Teleco").withLicenseType("Seat")
                .withOrganizationId("TestOrg");
    }

}
