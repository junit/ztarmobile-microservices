/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.controllers;

/**
 * This interface contains all the path definitions for all the repositories.
 * This can be interpreted as the custom paths.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public interface ConstantControllerAttribute {
    /**
     * Attribute name for the introspected token.
     */
    String INTROSPECTED_TOKEN = "introspectedToken";
    /**
     * Attribute name for the requested resource.
     */
    String REQUESTED_RESOURCE = "requestedResource";
}
