/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, August 2017.
 */
package com.ztarmobile.transaction.model;

/**
 * Value object.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
public abstract class EmailNotification {
    /**
     * The subject.
     */
    private String subject;
    /**
     * The receipt.
     */
    private String to;
    /**
     * The sender.
     */
    private String from = "admin@good2gomobile.com";
    /**
     * The message body.
     */
    private String messageBody;

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from
     *            the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody
     *            the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EmailNotification [subject=" + subject + ", to=" + to + ", from=" + from + ", messageBody="
                + messageBody + "]";
    }
}
