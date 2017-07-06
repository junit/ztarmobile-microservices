/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jun 2017.
 */
package com.ztarmobile.notification.service.impl;

import static com.ztarmobile.notification.common.ReportHelper.createHeader;
import static com.ztarmobile.notification.common.ReportHelper.createReportName;
import static com.ztarmobile.notification.common.ReportHelper.createRow;

import com.ztarmobile.notification.dao.SubscriberUsageDao;
import com.ztarmobile.notification.model.EmailAttachment;
import com.ztarmobile.notification.model.SubscriberUsage;
import com.ztarmobile.notification.model.UsageEmailNotification;
import com.ztarmobile.notification.notification.CustomerUsageMailSender;
import com.ztarmobile.notification.service.UsageNotificationService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Direct service implementation that performs the notification process.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Service
public class UsageNotificationServiceImpl implements UsageNotificationService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(UsageNotificationServiceImpl.class);

    /**
     * Customer Usage DAO.
     */
    @Autowired
    private SubscriberUsageDao subscriberUsageDao;

    /**
     * Dependency to send notifications.
     */
    @Autowired
    private CustomerUsageMailSender customerUsageMailSender;

    /**
     * The bundle Id.
     */
    @Value("${notification.bundle-id}")
    private String bundleId;

    /**
     * Recipients.
     */
    @Value("${notification.recipients}")
    private String recipients;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubscriberUsage> getAllSubscriberActivity() {
        log.debug("Retrieving subscribers by bundle: " + bundleId);
        return subscriberUsageDao.findUsageByBundle(bundleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performNotification(List<SubscriberUsage> list) {
        log.debug("Sending notification for: " + list.size() + " subscribers");
        List<EmailAttachment> attachments = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder(createHeader());
            for (SubscriberUsage detail : list) {
                // creates the content of the attachment
                sb.append(createRow(detail));
            }
            EmailAttachment report = new EmailAttachment();
            report.setName(createReportName());
            report.setContent(sb.toString().getBytes());
            attachments.add(report);

            // we notify the user...
            UsageEmailNotification notification = new UsageEmailNotification();
            notification.setTo(recipients);
            notification.setBundleId(bundleId);
            notification.setContent(attachments);
            notification.setSubject("Subscriber Usage Report");
            customerUsageMailSender.sendEmail(notification);
        } catch (Throwable ex) {
            log.error(ex.toString());
            ex.printStackTrace();
        }
        log.debug("Notification is done");
    }

}
