/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.controllers;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.METHOD_NOT_SUPPORTED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztarmobile.exception.ErrorResponse;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * This interface contains all the path definitions for all the repositories.
 * This can be interpreted as the custom paths.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@ControllerAdvice
public class GlobalExceptionController {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionController.class);

    /**
     * When the HTTP method is not supported, this method will be fired to state
     * that the method is not allowed.
     * 
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param e
     *            The exception for this transaction.
     * @return The modelAndView
     * @throws IOException
     *             Exception while trying to write the response.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.debug("HTTP Request method " + method + " not supported for path " + uri);

        HttpMessageErrorCodeResolver res = new HttpMessageErrorCodeResolver(METHOD_NOT_SUPPORTED, method, uri);
        AuthorizationServiceException ex = new AuthorizationServiceException(res);

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        errorResponse.setStatus(METHOD_NOT_SUPPORTED.getNumber());
        errorResponse.setError("Unsupported");

        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setStatus(Integer.parseInt(HttpStatus.METHOD_NOT_ALLOWED.toString()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        System.out.println(errorResponse);
        response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
        return new ModelAndView();
    }
}
