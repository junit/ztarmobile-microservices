/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.tasks;

import static com.ztarmobile.invoicing.common.DateUtils.getMaximumDayOfMonth;
import static com.ztarmobile.invoicing.common.DateUtils.getMinimunDayOfMonth;
import static com.ztarmobile.invoicing.common.ReportHelper.createHeader;
import static com.ztarmobile.invoicing.common.ReportHelper.createReportName;
import static com.ztarmobile.invoicing.common.ReportHelper.createRow;
import static java.util.Calendar.MONTH;

import com.ztarmobile.invoicing.email.InvoicingMailSender;
import com.ztarmobile.invoicing.model.CatalogEmail;
import com.ztarmobile.invoicing.model.CatalogProduct;
import com.ztarmobile.invoicing.model.EmailAttachment;
import com.ztarmobile.invoicing.model.EmailNotification;
import com.ztarmobile.invoicing.model.EmailProductNotification;
import com.ztarmobile.invoicing.model.InvoicingRequest;
import com.ztarmobile.invoicing.model.ReportDetails;
import com.ztarmobile.invoicing.service.InvoicingService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Performs an schedule task to create the monthly reports.
 *
 * @author armandorivas
 * @since 03/30/17
 */
@Component
public class InvoicingScheduledTask {
    /**
     * The queue name.
     */
    public static final String INVOICING_REQ_QUEUE = "invoicing.requests";

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(InvoicingScheduledTask.class);

    /**
     * The JSM template.
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Dependency of the invoicing service.
     */
    @Autowired
    private InvoicingService invoicingService;

    /**
     * Dependency to send notifications.
     */
    @Autowired
    private InvoicingMailSender invoicingMailSender;

    /**
     * This method receives the request from the queue and process it.
     */
    @Scheduled(cron = "${invoicing.cron.report}")
    public void scheduleMonthlyInvoice() {
        LOG.debug("Requesting montly invoicing report...");

        for (CatalogProduct catalogProduct : invoicingService.getAllAvailableProducts()) {
            LOG.debug("Processing: " + catalogProduct);
            InvoicingRequest invoicingRequest = new InvoicingRequest();
            invoicingRequest.setProduct(catalogProduct.getProduct());
            // we make sure the we run the previous month
            invoicingRequest.setRunPreviousMonth(true);

            // send the request to the queue
            jmsTemplate.convertAndSend(INVOICING_REQ_QUEUE, invoicingRequest);
        }
    }

    /**
     * This method sends notifications.
     */
    @Scheduled(cron = "${invoicing.cron.notification}")
    public void scheduleNotificationInvoice() {
        LOG.debug("Sending invoicing notifications...");

        // calculates the previous month.
        int previousMonth = Calendar.getInstance().get(MONTH) - 1;
        Calendar start = getMinimunDayOfMonth(previousMonth);
        Calendar end = getMaximumDayOfMonth(previousMonth);

        for (CatalogEmail catalogEmail : invoicingService.getAllAvailableEmails()) {
            LOG.debug("Sending notification: " + catalogEmail);

            boolean sendEmail = false;
            String product = null;
            List<EmailAttachment> attachments = new ArrayList<>();

            for (EmailProductNotification notification : invoicingService.getAllProductsByEmail(catalogEmail)) {
                product = notification.getCatalogProduct().getProduct();
                // only notifies those ones enabled
                if (notification.isNotificationEnabled()) {
                    StringBuilder sb = new StringBuilder(createHeader());

                    for (ReportDetails detail : invoicingService.generateReport(start, end, product)) {
                        // creates the content of the attachment
                        sb.append(createRow(detail));
                    }

                    EmailAttachment report = new EmailAttachment();
                    report.setName(createReportName(product, start, end));
                    report.setContent(sb.toString().getBytes());
                    attachments.add(report);
                    // at least one report is available..., we send email
                    sendEmail = true;
                }
            }
            if (sendEmail) {
                String fullName = catalogEmail.getFirstName() + " " + catalogEmail.getLastName();

                // we notify the user...
                EmailNotification notification = new EmailNotification();
                notification.setTo(catalogEmail.getEmail());
                notification.setReceiptName(fullName);
                notification.setContent(attachments);
                invoicingMailSender.sendEmail(notification);
            } else {
                LOG.warn("No email was sent...");
            }
        }
    }
}
