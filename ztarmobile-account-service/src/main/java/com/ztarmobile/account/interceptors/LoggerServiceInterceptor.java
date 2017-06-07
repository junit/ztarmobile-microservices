/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.account.interceptors;

import static com.ztarmobile.account.controllers.ConstantControllerAttribute.REQUESTED_RESOURCE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.TRACE;

import com.ztarmobile.util.ProtectedResource;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Spring bean to log all the incoming requests and responses.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class LoggerServiceInterceptor extends HandlerInterceptorAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(LoggerServiceInterceptor.class);
    /**
     * The header.
     */
    private static final String HEADER = "X-FORWARDED-FOR";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        ProtectedResource protectedResource = new ProtectedResource();
        protectedResource.setMethod(getRequestedMethod(request.getMethod()));
        protectedResource.setPath(request.getRequestURI());

        log.debug("Request... [" + request.getMethod() + "] " + request.getRequestURI() + getParameters(request));

        // sets the object into the request scope.
        request.setAttribute(REQUESTED_RESOURCE, protectedResource);

        return true;
    }

    /**
     * Gets the requested method.
     * 
     * @param method
     *            The HTTP method.
     * @return The requested method.
     */
    private RequestMethod getRequestedMethod(String method) {
        switch (method) {
        case "GET":
            return GET;
        case "HEAD":
            return HEAD;
        case "POST":
            return POST;
        case "PUT":
            return PUT;
        case "PATCH":
            return PATCH;
        case "DELETE":
            return DELETE;
        case "OPTIONS":
            return OPTIONS;
        case "TRACE":
            return TRACE;
        default:
            throw new IllegalStateException("No valid method was found: " + method);
        }
    }

    private String getParameters(HttpServletRequest request) {
        StringBuffer posted = new StringBuffer();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr + "=");
            if (curr.contains("password") || curr.contains("pass") || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader(HEADER);
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr != null && !ipAddr.equals("")) {
            posted.append("&_psip=" + ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader(HEADER);
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - " + HEADER + " : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
