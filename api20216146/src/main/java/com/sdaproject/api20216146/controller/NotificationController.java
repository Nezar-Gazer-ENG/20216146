package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Notification;
import com.sdaproject.api20216146.model.NotificationTemplate; 
import com.sdaproject.api20216146.service.NotificationService;
import com.sdaproject.api20216146.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<String> sendNotification(@RequestParam Long userId, @RequestParam String message) {
        try {
            if (userId == null || message == null || message.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid user ID or message.");
            }
            notificationService.sendNotification(userId, message);
            return ResponseEntity.ok("Notification sent to user with ID " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send notification: " + e.getMessage());
        }
    }
    
    @MessageMapping("/user/{userId}/notifications")
    public void handleUserSpecificWebSocketMessage(@RequestParam Long userId, String message) {
        if (userId != null && message != null && !message.isEmpty()) {
            notificationService.sendNotification(userId, message);
    }
}

    @GetMapping("/notifications")
    public ResponseEntity<?> getUserNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(notifications);
    }

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public NotificationTemplate handleWebSocketMessage(String message) {
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setMessage(message);
        notificationTemplate.setTimestamp(System.currentTimeMillis());
        notificationTemplate.setSender("System"); 
        
        return notificationTemplate;
    }
        @PostMapping("/mark-as-read")
        public ResponseEntity<Void> markNotificationsAsRead(@RequestParam Long userId) {
        notificationService.markNotificationsAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
