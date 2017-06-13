/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This is an special interceptor that is used with the repositories generated
 * automatically by spring rest. Basically, what we do is to invoke the existing
 * interceptos so that we have the same logic.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 3.0
 */
public class RepositoryInterceptor implements HandlerInterceptor {
    /**
     * List of interceptors.
     */
    private List<HandlerInterceptorAdapter> localInterceptors = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // based on the list of interceptors, we loop thought those and call the
        // corresponding method.
        for (HandlerInterceptor handlerInterceptor : localInterceptors) {
            if (handlerInterceptor.preHandle(request, response, handler)) {
                // we process the next interceptor in the list.
                continue;
            }
            // but we stop the flow when one of the interceptors is not valid.
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // as long as the list of interceptos is not empty, we delegate the flow
        // to the corresponding interceptor.
        for (HandlerInterceptor handlerInterceptor : localInterceptors) {
            handlerInterceptor.postHandle(request, response, handlerInterceptor, modelAndView);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // as long as the list of interceptos is not empty, we delegate the flow
        // to the corresponding interceptor.
        for (HandlerInterceptor handlerInterceptor : localInterceptors) {
            handlerInterceptor.afterCompletion(request, response, handlerInterceptor, ex);
        }
    }

    /**
     * Adds handler interceptos into this wrapper object.
     * 
     * @param handlerInterceptorAdapter
     *            An interceptor adapter.
     */
    public void addInterceptor(HandlerInterceptorAdapter handlerInterceptorAdapter) {
        // we make sure we have only valid interceptors in the list.
        if (handlerInterceptorAdapter != null) {
            localInterceptors.add(handlerInterceptorAdapter);
        }
    }
}
