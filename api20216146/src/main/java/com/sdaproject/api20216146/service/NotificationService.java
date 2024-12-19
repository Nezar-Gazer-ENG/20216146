package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Notification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private List<Notification> notifications = new ArrayList<>();

    public Notification createNotification(Notification notification) {
        notifications.add(notification);
        return notification;
    }

    public Notification getNotification(Long id) {
        return notifications.stream().filter(notification -> notification.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Notification> getAllNotifications() {
        return notifications;
    }

    public Notification updateNotification(Long id, Notification updatedNotification) {
        Notification notification = notifications.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
        if (notification != null) {
            notification.setMessage(updatedNotification.getMessage());
            notification.setType(updatedNotification.getType());
            notification.setSent(updatedNotification.isSent());
        }
        return notification;
    }

    public String deleteNotification(Long id) {
        Notification notification = notifications.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
        if (notification != null) {
            notifications.remove(notification);
            return "Notification deleted";
        }
        return "Notification not found";
    }
}
