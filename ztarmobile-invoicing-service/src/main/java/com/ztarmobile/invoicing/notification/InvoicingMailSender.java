/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.invoicing.notification;

import com.ztarmobile.invoicing.model.EmailAttachment;
import com.ztarmobile.invoicing.model.ReportEmailNotification;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * Sends notifications thought email.
 *
 * @author armandorivas
 * @since 04/09/17
 */
@Component
public class InvoicingMailSender {

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(InvoicingMailSender.class);

    /**
     * Dependency of the mail sender.
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * The template engine.
     */
    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * Sends the email asynchronously.
     * 
     * @param email
     *            The content of the email with attachments.
     */
    @Async
    public void sendEmail(ReportEmailNotification email) {
        log.debug("Sending email to: " + email.getTo());

        // create the message body...
        createMessageBody(email);

        MimeMessage message = mailSender.createMimeMessage();
        int totalAttachments = 0;
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getMessageBody(), true);

            if (email.getContent() != null) {
                for (EmailAttachment attachment : email.getContent()) {
                    helper.addAttachment(attachment.getName(), new ByteArrayResource(attachment.getContent()));
                    totalAttachments++;
                }
            }

            mailSender.send(message);
            log.debug("Email sent succesfully to: " + email.getTo() + ", with " + totalAttachments + " attachments");
        } catch (MessagingException e) {
            log.warn("Unable to send email: " + e);
        }
    }

    /**
     * Creates a message body.
     * 
     * @param email
     *            The email parameters.
     */
    private void createMessageBody(ReportEmailNotification email) {
        if (email.getMessageBody() == null) {
            // creates the body based on a template.
            final Context ctx = new Context();
            ctx.setVariable("name", email.getReceiptName());

            final String htmlContent = this.templateEngine.process("invoicing", ctx);
            // overrides the content of the body...
            email.setMessageBody(htmlContent);
        }
    }
}
