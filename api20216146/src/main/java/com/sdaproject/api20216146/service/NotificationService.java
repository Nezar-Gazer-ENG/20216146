package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.Notification;
import com.sdaproject.api20216146.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification); 
    }

    public Notification getNotification(Long id) {
        return notificationRepository.findById(id).orElse(null); 
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll(); 
    }

    public Notification updateNotification(Long id, Notification updatedNotification) {
        Notification existingNotification = getNotification(id);
        if (existingNotification != null) {
            existingNotification.setUserId(updatedNotification.getUserId());
            existingNotification.setMessage(updatedNotification.getMessage());
            existingNotification.setType(updatedNotification.getType());
            existingNotification.setSent(updatedNotification.isSent());
            return notificationRepository.save(existingNotification); 
        }
        return null;
    }

    public String deleteNotification(Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id); 
            return "Notification deleted";
        }
        return "Notification not found";
    }
}
