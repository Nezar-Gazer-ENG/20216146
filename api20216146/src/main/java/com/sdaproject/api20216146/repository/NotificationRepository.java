package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepository {

    private List<Notification> notifications = new ArrayList<>();
    private long currentId = 1L;

    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(currentId++);
            notifications.add(notification);
        } else {
            // Update existing notification if found
            boolean updated = false;
            for (int i = 0; i < notifications.size(); i++) {
                if (notifications.get(i).getId().equals(notification.getId())) {
                    notifications.set(i, notification);
                    updated = true;
                    break;
                }
            }
            // If not found, treat as new
            if (!updated) {
                notifications.add(notification);
            }
        }
        return notification;
    }

    public Optional<Notification> findById(Long id) {
        return notifications.stream()
                .filter(notification -> notification.getId().equals(id))
                .findFirst();
    }

    public List<Notification> findAll() {
        return notifications;
    }

    public void deleteById(Long id) {
        notifications.removeIf(notification -> notification.getId().equals(id));
    }
}
