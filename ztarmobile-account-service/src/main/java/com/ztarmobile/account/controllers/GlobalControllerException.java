/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.controllers;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.AUTH_SERVER_ACCESS;
import static com.ztarmobile.exception.AuthorizationMessageErrorCode.METHOD_NOT_SUPPORTED;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztarmobile.account.exception.AccountServiceException;
import com.ztarmobile.exception.ErrorResponse;
import com.ztarmobile.exception.HttpMessageErrorCodeResolver;
import com.ztarmobile.openid.connect.security.authorization.AuthorizationServiceException;

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
    public ModelAndView handleMethodNotSupported(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.debug("HTTP Request method " + method + " not supported for path " + uri);

        HttpMessageErrorCodeResolver res = new HttpMessageErrorCodeResolver(METHOD_NOT_SUPPORTED, method, uri);
        AuthorizationServiceException ex = new AuthorizationServiceException(res);

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        errorResponse.setStatus(METHOD_NOT_SUPPORTED.getNumber());
        errorResponse.setError("Unsupported");

        return createJSONResponse(response, errorResponse, METHOD_NOT_ALLOWED.value());
    }

    /**
     * This exception is thrown whenever there's a problem with the
     * authorization server through the use of its API. Usually, when the
     * credentials are incorrect and we try to access to some resource, the API
     * throws <code>NotAuthorizedException</code>. This is the exception we
     * catch and show the user as a problem from the Authorization Server.
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
    @ExceptionHandler(NotAuthorizedException.class)
    public ModelAndView handleAuthServerServiceException(HttpServletRequest request, HttpServletResponse response,
            Exception e) throws IOException {

        log.debug("The credentials for Authorization Server are not correct, check user and pass");

        ErrorResponse errorResponse = new ErrorResponse(AUTH_SERVER_ACCESS.getEvaluatedMessage());
        errorResponse.setStatus(AUTH_SERVER_ACCESS.getNumber());
        errorResponse.setError("Authorization Server Access");

        return createJSONResponse(response, errorResponse, UNAUTHORIZED.value());
    }

    /**
     * When there's an exception of type <code>AccountServiceException</code>,
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
    @ExceptionHandler(AccountServiceException.class)
    public ModelAndView handleAccountServiceException(HttpServletRequest request, HttpServletResponse response,
            Exception e) throws IOException {

        AccountServiceException accountException = (AccountServiceException) e;

        ErrorResponse errorResponse = new ErrorResponse(accountException.getMessage());
        errorResponse.setStatus(accountException.getHttpMessageErrorCode().getNumber());
        errorResponse.setError("User Account Error");

        return createJSONResponse(response, errorResponse, accountException.getHttpMessageErrorCode().getHttpCode());
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
