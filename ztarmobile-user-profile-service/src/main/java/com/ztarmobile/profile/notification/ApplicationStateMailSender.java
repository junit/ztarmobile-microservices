/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, May 2017.
 */
package com.ztarmobile.profile.notification;

import com.ztarmobile.profile.model.ApplicationEmailNotification;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 05/04/17
 */
@Component
public class ApplicationStateMailSender {
    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ApplicationStateMailSender.class);

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
     *            The content of the email.
     */
    @Async
    public void sendNotificationAtStartup(ApplicationEmailNotification email) {
        log.debug("Sending email to: " + email.getTo());

        // create the message body...
        createMessageBody(email);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getFrom());
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getMessageBody(), true);

            mailSender.send(message);
            log.debug("Email sent succesfully to: " + email.getTo() + ", service with errors: " + !email.isSuccess());
        } catch (MessagingException e) {
            log.warn("Unable to send email: " + e);
        }
    }

    /**
     * Creates a message body based on a template.
     * 
     * @param email
     *            The email parameters.
     */
    private void createMessageBody(ApplicationEmailNotification email) {
        if (email.getMessageBody() == null) {
            // creates the body based on a template.
            String templateName = null;
            final Context ctx = new Context();
            if (email.isSuccess()) {
                ctx.setVariable("version", email.getVersion());
                ctx.setVariable("artifact", email.getArtifact());
                ctx.setVariable("name", email.getName());
                ctx.setVariable("description", email.getDescription());
                ctx.setVariable("url", email.getUrl());

                templateName = "startup_success";
            } else {
                ctx.setVariable("reason", email.getReason());

                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                email.getReason().printStackTrace(printWriter);
                printWriter.flush();
                String stackTrace = writer.toString();

                ctx.setVariable("trace", stackTrace);

                templateName = "startup_failure";
            }
            final String htmlContent = this.templateEngine.process(templateName, ctx);
            // overrides the content of the body...
            email.setMessageBody(htmlContent);
        }
    }
}
