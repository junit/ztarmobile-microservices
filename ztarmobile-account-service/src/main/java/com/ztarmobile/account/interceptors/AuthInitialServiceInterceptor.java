/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.account.interceptors;

import static com.ztarmobile.account.controllers.ConstantControllerAttribute.BASIC_AUTHENTICATION;
import static com.ztarmobile.account.controllers.ConstantControllerAttribute.IGNORE_SECURITY;

import com.ztarmobile.account.annotation.EnableBasicAuthentication;
import com.ztarmobile.account.annotation.IgnoreSecurity;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Spring bean to check whether the security is needed or not across the API's.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
@Component
public class AuthInitialServiceInterceptor extends HandlerInterceptorAdapter {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(AuthInitialServiceInterceptor.class);

    /**
     * Package to scan.
     */
    private static final String DEFAULT_CONTROLLER_PACKAGE = "com.ztarmobile.account.controllers";

    /**
     * Based path.
     */
    @Value("${spring.data.rest.base-path}")
    private String basePath;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("Validating Security...");

        // defaults the variables to a false.
        request.setAttribute(BASIC_AUTHENTICATION, Boolean.FALSE);
        request.setAttribute(IGNORE_SECURITY, Boolean.FALSE);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        for (BeanDefinition bd : scanner.findCandidateComponents(DEFAULT_CONTROLLER_PACKAGE)) {
            if (bd.getBeanClassName().endsWith("Controller")) {

                Class<?> clazz = Class.forName(bd.getBeanClassName());
                // each method is tested
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        if (method.isAnnotationPresent(EnableBasicAuthentication.class)
                                && method.isAnnotationPresent(IgnoreSecurity.class)) {
                            // this is not valid
                            throw new IllegalStateException(EnableBasicAuthentication.class + " and "
                                    + IgnoreSecurity.class + " cannot be used together");
                        } else {
                            if (method.isAnnotationPresent(EnableBasicAuthentication.class)) {
                                setSecurityFlagIfSet(request, method, BASIC_AUTHENTICATION);
                            } else if (method.isAnnotationPresent(IgnoreSecurity.class)) {
                                setSecurityFlagIfSet(request, method, IGNORE_SECURITY);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * The security is tested for the request calls.
     * 
     * @param request
     *            The HTTP request.
     * @param method
     *            The current method.
     * @param ignoreSecurity
     *            The request attribute name.
     */
    private void setSecurityFlagIfSet(HttpServletRequest request, Method method, String attributeName) {
        // we need to check the resource requested.
        String cilentRequestUri = request.getRequestURI();
        String clientRequestMethod = request.getMethod();

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = getMethodUri(requestMapping);
        String verb = getMethodVerb(requestMapping);

        if (uri.equals(cilentRequestUri) && verb.equals(clientRequestMethod)) {
            // sets the flag
            request.setAttribute(attributeName, Boolean.TRUE);
            log.warn("*** The following call might not be secured: [" + clientRequestMethod + "] " + cilentRequestUri);
        }
    }

    private String getMethodVerb(RequestMapping requestMapping) {
        RequestMethod methodUri = null;
        RequestMethod[] values = requestMapping.method();
        if (values.length > 0) {
            methodUri = values[0];
        }
        return methodUri.name();
    }

    private String getMethodUri(RequestMapping requestMapping) {
        String methodUri = "";
        String[] values = requestMapping.value();
        if (values.length > 0) {
            methodUri = values[0];
        }
        return basePath + methodUri;
    }
}
