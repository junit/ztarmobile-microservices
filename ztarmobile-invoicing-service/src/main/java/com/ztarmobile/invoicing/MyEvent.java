package com.ztarmobile.invoicing;

import com.ztarmobile.invoicing.model.ApplicationEmailNotification;
import com.ztarmobile.invoicing.notification.ApplicationStateMailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyEvent implements ApplicationListener<ApplicationFailedEvent> {

    @Autowired
    private ApplicationStateMailSender applicationStateMailSender;

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        System.out.println(">>" + event.getException());
        System.out.println("Something webt wrong=======");
        ApplicationEmailNotification applicationNotif = new ApplicationEmailNotification(false, event.getException());
        applicationStateMailSender.sendNotificationAtStartup(applicationNotif);
    }

}
