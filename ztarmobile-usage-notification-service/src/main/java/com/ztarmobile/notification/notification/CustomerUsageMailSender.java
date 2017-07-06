/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, Jul 2017.
 */
package com.ztarmobile.notification.notification;

import com.ztarmobile.notification.model.EmailAttachment;
import com.ztarmobile.notification.model.UsageEmailNotification;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
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
 * @version %I%, %G%
 * @since 1.0
 */
@Component
public class CustomerUsageMailSender {

    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(CustomerUsageMailSender.class);

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
    public void sendEmail(UsageEmailNotification email) {
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
    private void createMessageBody(UsageEmailNotification email) {
        if (email.getMessageBody() == null) {
            // creates the body based on a template.
            final Context ctx = new Context();
            ctx.setVariable("plan", email.getBundleId());

            final String htmlContent = this.templateEngine.process("subcriber_usage", ctx);
            // overrides the content of the body...
            email.setMessageBody(htmlContent);
        }
    }
}
