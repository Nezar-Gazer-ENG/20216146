package com.sdaproject.api20216146.controller;

import com.sdaproject.api20216146.model.Notification;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private List<Notification> notifications = new ArrayList<>();

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        notifications.add(notification);
        return notification;
    }

    @GetMapping("/{id}")
    public Notification getNotification(@PathVariable Long id) {
        return notifications.stream().filter(notification -> notification.getId().equals(id)).findFirst().orElse(null);
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notifications;
    }

    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable Long id, @RequestBody Notification updatedNotification) {
        Notification notification = notifications.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
        if (notification != null) {
            notification.setMessage(updatedNotification.getMessage());
            notification.setType(updatedNotification.getType());
            notification.setSent(updatedNotification.isSent());
        }
        return notification;
    }

    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable Long id) {
        Notification notification = notifications.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
        if (notification != null) {
            notifications.remove(notification);
            return "Notification deleted";
        }
        return "Notification not found";
    }
}
