package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Notification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private List<Notification> notifications = new ArrayList<>();
    private long currentId = 1L;

    public Notification createNotification(Notification notification) {
        notification.setId(currentId++);
        notifications.add(notification);
        return notification;
    }

    public Notification getNotification(Long id) {
        return notifications.stream()
                .filter(notification -> notification.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Notification> getAllNotifications() {
        return notifications;
    }

    public Notification updateNotification(Long id, Notification updatedNotification) {
        for (int i = 0; i < notifications.size(); i++) {
            Notification existingNotification = notifications.get(i);
            if (existingNotification.getId().equals(id)) {
                existingNotification.setUserId(updatedNotification.getUserId());
                existingNotification.setMessage(updatedNotification.getMessage());
                existingNotification.setType(updatedNotification.getType());
                existingNotification.setSent(updatedNotification.isSent());
                return existingNotification;
            }
        }
        return null;
    }

    public String deleteNotification(Long id) {
        boolean removed = notifications.removeIf(notification -> notification.getId().equals(id));
        return removed ? "Notification deleted" : "Notification not found";
    }
}
