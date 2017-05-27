/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.oauth2.model;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.Requirement;

/**
 * The PKCEAlgorithm.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public final class PKCEAlgorithm extends Algorithm {

    private static final long serialVersionUID = 7752852583210088925L;

    public static final PKCEAlgorithm plain = new PKCEAlgorithm("plain", Requirement.REQUIRED);

    public static final PKCEAlgorithm S256 = new PKCEAlgorithm("S256", Requirement.OPTIONAL);

    public PKCEAlgorithm(String name, Requirement req) {
        super(name, req);
    }

    public PKCEAlgorithm(String name) {
        super(name, null);
    }

    public static PKCEAlgorithm parse(final String s) {
        if (s.equals(plain.getName())) {
            return plain;
        } else if (s.equals(S256.getName())) {
            return S256;
        } else {
            return new PKCEAlgorithm(s);
        }
    }
}
