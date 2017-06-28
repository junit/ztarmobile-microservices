/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ztarmobile.notification.service.impl;

import com.ztarmobile.notification.model.NotificationActity;
import com.ztarmobile.notification.service.BalanceNotificationService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 *
 * @author armandorivasarzaluz
 */
@Service
public class BalanceNotificationServiceImpl implements BalanceNotificationService {

    @Override
    public List<NotificationActity> getAllAvailableActivity() {
        return new ArrayList<NotificationActity>();
    }

}
