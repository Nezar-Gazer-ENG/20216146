package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/send-notification")
    public String sendNotification(@RequestParam String destination, @RequestParam String message) {
        notificationService.sendNotification(destination, message);
        return "Notification sent to " + destination;
    }
}

