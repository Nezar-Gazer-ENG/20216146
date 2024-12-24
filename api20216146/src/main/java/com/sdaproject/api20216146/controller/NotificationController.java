package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Notification;
import com.sdaproject.api20216146.service.NotificationService; // Import the NotificationService
import com.sdaproject.api20216146.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService; 

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/send-notification")
    public String sendNotification(@RequestParam String destination, @RequestParam String message) {
        notificationService.sendNotification(destination, message);
        return "Notification sent to " + destination;
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getUserNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(notifications);
    }
    
}
