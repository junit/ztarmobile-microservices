package com.ztarmobile.notification.jms;

import com.ztarmobile.notification.model.NotificationActity;
import com.ztarmobile.notification.service.BalanceNotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Receives all the messages from the JMS.
 *
 * @author armandorivas
 * @version %I%, %G%
 * @since 1.0
 */
@Component
public class NotificationReceiver {
    /**
     * The queue name.
     */
    public static final String NOTIFICATION_REQ_QUEUE = "notification.requests";

    /**
     * Logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(NotificationReceiver.class);

    /**
     * Dependency of the balance notification service.
     */
    @Autowired
    private BalanceNotificationService balanceNotificationService;

    /**
     * This method receives the request from the queue and process it.
     * 
     * @param request
     *            The request.
     */
    @JmsListener(destination = NOTIFICATION_REQ_QUEUE, containerFactory = "myFactory")
    public void receiveMessage(NotificationActity request) {
        log.debug("Receiving request: " + request);

        try {
            // performs the notification stuff.
            balanceNotificationService.performNotification();
        } catch (Throwable ex) {
            log.debug("Request did not finish correctly :( ");
            // we log the error...
            ex.printStackTrace();
        }
    }
}
