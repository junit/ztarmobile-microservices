/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.profile.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

/**
 * The entity.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@JsonInclude(Include.NON_NULL)
public class UserProfile {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private List<Mdn> mdns;
    private List<Address> addresses;
    private List<PaymentProfile> paymentProfiles;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the mdns
     */
    public List<Mdn> getMdns() {
        return mdns;
    }

    /**
     * @param mdns
     *            the mdns to set
     */
    public void setMdns(List<Mdn> mdns) {
        this.mdns = mdns;
    }

    /**
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses
     *            the addresses to set
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the paymentProfiles
     */
    public List<PaymentProfile> getPaymentProfiles() {
        return paymentProfiles;
    }

    /**
     * @param paymentProfiles
     *            the paymentProfiles to set
     */
    public void setPaymentProfiles(List<PaymentProfile> paymentProfiles) {
        this.paymentProfiles = paymentProfiles;
    }

}
