/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.service.impl;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.ztarmobile.notification.dao.CustomerBalanceDao;
import com.ztarmobile.notification.model.AccountStatus;
import com.ztarmobile.notification.model.CustomerBalance;
import com.ztarmobile.notification.model.NotificationActity;
import com.ztarmobile.notification.service.BalanceNotificationService;
import com.ztarmobile.utils.RestClientUtils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Direct service implementation that calculates and perform the low balance
 * notification process.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Service
public class BalanceNotificationServiceImpl implements BalanceNotificationService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(BalanceNotificationServiceImpl.class);

    /**
     * URI to check balances from Ericsson.
     */
    @Value("${notification.ztarmobile.search.provider-uri}")
    private String providerResource;

    /**
     * Customer Balance DAO.
     */
    @Autowired
    private CustomerBalanceDao customerBalanceDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationActity> getAllAvailableActivity() {
        List<NotificationActity> list = new ArrayList<NotificationActity>();
        list.add(new NotificationActity());
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void performNotification() {
        log.debug(">> Starting the low balance notification process...");
        try {
            AccountStatus accountStatus = getStatus("34343", "33434");
            updateBalanceNotification(accountStatus);
        } catch (Throwable ex) {
            // we handle the error...
        }
        log.debug("<< Ending low balance notification process");
    }

    private void updateBalanceNotification(AccountStatus accountStatus) {
        log.debug("updating customer balance table...");

        CustomerBalance customerBalance = new CustomerBalance();

        customerBalanceDao.updateBalances(customerBalance);
    }

    /**
     * Retrieves the network status information.
     *
     * @param mdn
     *            The mdn to look up.
     * @return The AccountStatus with network information
     */
    public AccountStatus getStatus(String mdn, String market) throws Exception {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("mdn", mdn);
        params.add("market", market);
        params.add("simple", "false");
        return RestClientUtils.fetch(providerResource, AccountStatus.class, params);
    }

}
