/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jul 2017.
 */
package com.ztarmobile.notification.tasks;

import static com.ztarmobile.notification.common.CommonUtils.getFormattedDate;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.ztarmobile.notification.model.SubscriberUsage;
import com.ztarmobile.notification.model.ztar.AccountStatus;
import com.ztarmobile.notification.service.UsageNotificationService;
import com.ztarmobile.utils.RestClientUtils;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Performs operations to generate the usage report.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Component
public class NotificationScheduledTask {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduledTask.class);

    /**
     * URI to check balances from Ericsson.
     */
    @Value("${notification.ztarmobile.search.provider-uri}")
    private String providerResource;

    /**
     * Dependency of the usage notification service.
     */
    @Autowired
    private UsageNotificationService usageNotificationService;

    /**
     * This method processes the usage notification.
     */
    @Scheduled(cron = "${notification.cron.activity}")
    public void scheduleUsageNotification() {
        log.debug(">> Requesting usage notification...");
        AccountStatus accountStatus = null;

        List<SubscriberUsage> list = usageNotificationService.getAllSubscriberActivity();
        // for each subscriber, we go to E// to get the balances and more
        // stuff
        log.debug(list.size() + " subscriber were found");

        for (SubscriberUsage detail : list) {
            try {
                // check the balances in Ericsson
                log.debug("Checking balance in Ericsson: " + detail.getMdn());
                accountStatus = getAccountStatusFromProvider(detail.getMdn(), "CAN");
                detail.setAccountBalance(accountStatus.getBalance());
                detail.setDedicatedAccount1(accountStatus.getDedicatedAccounts().get(0).getAccountBalance()); // DA1
                detail.setDedicatedAccount2(accountStatus.getDedicatedAccounts().get(1).getAccountBalance()); // DA2
                detail.setDedicatedAccount4(accountStatus.getDedicatedAccounts().get(3).getAccountBalance()); // DA4
                detail.setActivationDate(getFormattedDate(accountStatus.getActivationDate()));
                detail.setError(false);
            } catch (Throwable ex) {
                detail.setError(true);
                detail.setErrorDescription((ex.getMessage() == null ? "Unknown Error" : ex.getMessage()));
                log.error("An error occured due to: " + ex.toString());
                ex.printStackTrace();
            }
        }

        try {
            usageNotificationService.performNotification(list);
        } catch (Throwable ex) {
            log.error("An error occured while sending the notification due to: " + ex.toString());
            ex.printStackTrace();
        }
        log.debug("<< Ending notifications...");
    }

    /**
     * Retrieves the network status information.
     *
     * @param mdn
     *            The MDN to look up.
     * @return The AccountStatus with network information
     * @throws Exception
     *             When there's an error during the resource call.
     */
    private AccountStatus getAccountStatusFromProvider(String mdn, String market) throws Exception {
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("mdn", mdn);
        params.add("market", market);
        params.add("simple", "true");
        return RestClientUtils.fetch(providerResource, AccountStatus.class, params);
    }
}
