/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.controllers;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ztarmobile.profile.exception.GlobalMessageErrorCode.UNABLE_COMPLETE_TRANSACTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztarmobile.profile.exception.ErrorResponse;
import com.ztarmobile.profile.exception.ProfileServiceException;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class would contains all the custom exceptions handlers.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@ControllerAdvice
public class GlobalControllerException {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerException.class);

    /**
     * When there's an exception of type <code>ProfileServiceException</code>,
     * the exception handler will convert it into JSON and return it as the
     * final response.
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
    @ExceptionHandler(ProfileServiceException.class)
    public ModelAndView handleProfileServiceException(HttpServletRequest request, HttpServletResponse response,
            Exception e) throws IOException {

        ProfileServiceException profileException = (ProfileServiceException) e;

        ErrorResponse errorResponse = new ErrorResponse(profileException.getMessage());
        errorResponse.setStatus(profileException.getHttpMessageErrorCode().getNumber());
        errorResponse.setError("User Profile Error");

        return createJSONResponse(response, errorResponse, profileException.getHttpMessageErrorCode().getHttpCode());
    }

    /**
     * When there's a generic exception of type <code>Exception</code>, the
     * exception handler will convert it into JSON and return it as the final
     * response.
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
    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnknownException(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws IOException {

        e.printStackTrace();
        log.error(e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(UNABLE_COMPLETE_TRANSACTION.getMessage());
        errorResponse.setStatus(UNABLE_COMPLETE_TRANSACTION.getNumber());
        errorResponse.setError("Unknown Error");

        return createJSONResponse(response, errorResponse, UNABLE_COMPLETE_TRANSACTION.getHttpCode());
    }

    /**
     * Construct a JSON response based on the error response object.
     * 
     * @param response
     *            The HTTP response.
     * @param errorResponse
     *            The error response.
     * @param The
     *            HTTP status.
     * @return a JSON response.
     * @throws IOException
     *             Exception while trying to write the response.
     */
    private ModelAndView createJSONResponse(HttpServletResponse response, ErrorResponse errorResponse, int httpStatus)
            throws IOException {
        response.reset();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setStatus(httpStatus);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        response.getOutputStream().println(mapper.writeValueAsString(errorResponse));
        return new ModelAndView();
    }
}
