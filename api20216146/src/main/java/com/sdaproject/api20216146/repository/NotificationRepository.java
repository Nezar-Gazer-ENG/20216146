package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationRepository {

    private List<Notification> notifications = new ArrayList<>();

    public Notification save(Notification notification) {
        notifications.add(notification);
        return notification;
    }

    public Notification findById(Long id) {
        return notifications.stream().filter(notification -> notification.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Notification> findAll() {
        return notifications;
    }

    public Notification update(Long id, Notification updatedNotification) {
        Notification notification = notifications.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
        if (notification != null) {
            notification.setMessage(updatedNotification.getMessage());
            notification.setType(updatedNotification.getType());
            notification.setSent(updatedNotification.isSent());
        }
        return notification;
    }

    public String delete(Long id) {
        Notification notification = notifications.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
        if (notification != null) {
            notifications.remove(notification);
            return "Notification deleted";
        }
        return "Notification not found";
    }
}
