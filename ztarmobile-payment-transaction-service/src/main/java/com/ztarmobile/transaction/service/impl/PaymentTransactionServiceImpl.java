/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.service.impl;

import static com.ztarmobile.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.common.DateUtils.getMinimunDayOfMonth;
import static com.ztarmobile.common.DateUtils.setMaximumCalendarDay;
import static com.ztarmobile.common.DateUtils.setMinimumCalendarDay;
import static com.ztarmobile.transaction.common.ReportHelper.createHeader;
import static com.ztarmobile.transaction.common.ReportHelper.createReportName;
import static com.ztarmobile.transaction.common.ReportHelper.createRow;
import static com.ztarmobile.transaction.common.ReportHelper.createTotalRow;
import static java.util.Calendar.MONTH;

import com.ztarmobile.transaction.dao.PaymentTransactionDao;
import com.ztarmobile.transaction.model.EmailAttachment;
import com.ztarmobile.transaction.model.SubscriberTransaction;
import com.ztarmobile.transaction.model.TransactionsEmailNotification;
import com.ztarmobile.transaction.notification.CustomerUsageMailSender;
import com.ztarmobile.transaction.service.PaymentTransactionService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);

    /**
     * Customer Usage DAO.
     */
    @Autowired
    private PaymentTransactionDao paymentTransactionDao;

    /**
     * Dependency to send notifications.
     */
    @Autowired
    private CustomerUsageMailSender customerUsageMailSender;

    /**
     * The bundle Id.
     */
    @Value("${notification.product}")
    private String product;

    /**
     * Recipients.
     */
    @Value("${notification.recipients}")
    private String recipients;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SubscriberTransaction> getAllPaymentActivity() {
        log.debug("Retrieving CC transactions by product: " + product);
        int previousMonth = getEffectiveMonth();

        Calendar calStart = getMinimunDayOfMonth(previousMonth);
        Calendar calEnd = getMaximumDayOfMonth(previousMonth);

        setMinimumCalendarDay(calStart);
        setMaximumCalendarDay(calEnd);
        log.debug("Getting CC Transactions from: " + calStart.getTime() + " - " + calEnd.getTime());

        List<SubscriberTransaction> list = null;
        list = paymentTransactionDao.findCCTransactionsByProduct(calStart.getTime(), calEnd.getTime(), product);

        String monthName = getMonthName(calStart);
        log.debug("Under [" + product + "], we found: " + list.size() + " CC Payment transactions => " + monthName);
        return list;
    }

    private String getMonthName(Calendar calendar) {
        return new SimpleDateFormat("MMMMM").format(calendar.getTime());
    }

    private int getEffectiveMonth() {
        return Calendar.getInstance().get(MONTH) - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performNotification(List<SubscriberTransaction> list) {
        log.debug("Sending notification for: " + list.size() + " subscribers");
        List<EmailAttachment> attachments = new ArrayList<>();
        Calendar calStart = getMinimunDayOfMonth(getEffectiveMonth());

        try {
            StringBuilder sb = new StringBuilder(createHeader());
            for (SubscriberTransaction detail : list) {
                // creates the content of the attachment
                sb.append(createRow(detail));
            }
            sb.append(createTotalRow(list));
            EmailAttachment report = new EmailAttachment();
            report.setName(createReportName());
            report.setContent(sb.toString().getBytes());
            attachments.add(report);

            // we notify the user...
            TransactionsEmailNotification notification = new TransactionsEmailNotification();
            notification.setTo(recipients);
            notification.setVendor(product);
            notification.setMonth(getMonthName(calStart));
            notification.setContent(attachments);
            notification.setSubject("CC Transactions Report");
            customerUsageMailSender.sendEmail(notification);
        } catch (Throwable ex) {
            log.error("Unable to send email notification due to: " + ex.toString());
            ex.printStackTrace();
            throw ex;
        }
        log.debug("Notification is done");
    }
}
